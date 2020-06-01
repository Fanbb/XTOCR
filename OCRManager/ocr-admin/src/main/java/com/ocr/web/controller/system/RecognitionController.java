package com.ocr.web.controller.system;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.enums.BusinessType;
import com.ocr.common.json.JSONObject;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.http.HttpUtils;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.*;
import com.ocr.system.service.IOcrImageService;
import com.ocr.system.service.IOcrTradeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 渠道 信息操作处理
 *
 * @author ocr
 * @date 2020-05-19
 */
@Controller
@RequestMapping("/system/recognition")
public class RecognitionController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RecognitionController.class);

    private String prefix = "system/recognition";

    @Value("${ocr.profile}")
    private String imgUploadPath;

    @Autowired
    IOcrImageService iOcrImageService;

    @Autowired
    IOcrTradeService iOcrTradeService;

    @RequiresPermissions("system:recognition:view")
    @GetMapping()
    public String recognition() {
        return prefix + "/recognition";
    }

    @GetMapping("/upload")
    public String upload() {
        return prefix + "/fileupload";
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("system:recognition:fileUpload")
    @PostMapping("/fileUpload")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Log(title = "影像上传识别", businessType = BusinessType.OTHER)
    public AjaxResult fileUpload(@RequestParam("file") MultipartFile file, Map mmap) throws IOException {
        String dateStr = DateUtils.datePath();
        String oldFileName = file.getOriginalFilename();
        String sName = oldFileName.substring(oldFileName.lastIndexOf("."));
        Long fileName = System.currentTimeMillis();

        String path = imgUploadPath + "/IMAGE/" + dateStr;
        File pathFile = new File(path);
        log.info("上传路径:" + path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String filePath = path + "/" + fileName + sName;
        //存入影像信息 返回结果msg
        String msg = iOcrImageService.insertOcrImage(fileName, filePath);
        log.info(msg);

        File newFile = new File(filePath);
        file.transferTo(newFile);
        //调取接口进行识别 返回流水号
        String data = "{\"image_type\" :\"1\",\"path\":\"http://192.168.119.26:8080/profile/IMAGE/"+dateStr+"/" + fileName + sName+"\",\"read_image_way\":\"3\"}";
        String request = HttpUtils.sendPost2("http://192.168.119.31:10002/mask", data);
        String json = JSON.parseArray(request).toString();
        RequestModel model = JSONArray.parseObject(json.substring(1, json.length() - 1), RequestModel.class);
        String tradeId = "";
        switch (model.getClass_name()) {
            case "IDCardFront":
                IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
//                if (StringUtils.isNotEmpty(idCardFront.getIdCardNo())) {
                idCardFront.setImgType(model.getClass_name());
                /**
                 * 调用流水存储 返回流水id
                 */
                tradeId = iOcrTradeService.insertIDCardFront(idCardFront, "system", fileName + "");
//                }
                break;

            case "IDCardBack":
                IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                if (StringUtils.isNotEmpty(idCardBack.getStartDate())) {
                    idCardBack.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertIDCardBack(idCardBack, "system", fileName + "");
                }
                break;
            case "BankCard":
                BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                if (StringUtils.isNotEmpty(bankCard.getBankCardNo())) {
                    bankCard.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertBankCard(bankCard, "system", fileName + "");
                }
                break;
            case "Deposit":
                DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                if (StringUtils.isNotEmpty(deposit.getAccNo())) {
                    deposit.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertDeposit(deposit, "system", fileName + "");
                }
                break;
        }

        //上传成功后，需要把文件的名字保存到数据库
        mmap.put("urlImg", filePath);
        mmap.put("ocrTradeId", tradeId);

        return AjaxResult.success(mmap);
    }

    /**
     * 查询字典详细
     */
    @GetMapping("/data/{tradeId}")
    public String detail(@PathVariable("tradeId") String tradeId, ModelMap model) {
        OcrTrade ocrTrade = iOcrTradeService.selectOcrTradeById(tradeId);

        OcrImage ocrImage = iOcrImageService.selectOcrImageById(ocrTrade.getImageId());

        String imgUrl = ocrImage.getLocalPath().replace(imgUploadPath, "/profile/");
        model.addAttribute("imgUrl", imgUrl);


        if (ocrTrade.getImageType().equals("IDCardFront")) {
            IDCardFront idCardFront = JSON.parseObject(ocrTrade.getRemark1(), IDCardFront.class);
            model.put("idCardFront", idCardFront);
            return prefix + "/data/cardFront";
        } else if (ocrTrade.getImageType().equals("IDCardBack")) {
            IDCardBack idCardBack = JSON.parseObject(ocrTrade.getRemark1(), IDCardBack.class);
            model.put("idCardBack", idCardBack);
            return prefix + "/data/cardBack";
        } else if (ocrTrade.getImageType().equals("BankCard")) {
            BankCard bankCard = JSON.parseObject(ocrTrade.getRemark1(), BankCard.class);
            model.put("bankCard", bankCard);
            return prefix + "/data/bankCard";
        } else if (ocrTrade.getImageType().equals("Deposit")) {
            DepositReceipt depositReceipt = JSON.parseObject(ocrTrade.getRemark1(), DepositReceipt.class);
            model.put("depositReceipt", depositReceipt);
            return prefix + "/data/depositReceipt";
        } else {
            return "";
        }

    }


    /**
     * 新增渠道
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


}
