package com.ocr.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.enums.BusinessType;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 平台影像上传处理
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

    @Value("${ocr.serverProfile}")
    private String serverProfile;

    @Value("${ocr.ocrUrl}")
    private String ocrUrl;

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
        String imgId = UUID.randomUUID().toString();
        String oldFileName = file.getOriginalFilename();
        String sName = oldFileName.substring(oldFileName.lastIndexOf("."));
        Long fileName = System.currentTimeMillis();

        String path = imgUploadPath + "/IMAGE/" + dateStr;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String filePath = path + "/" + fileName + sName;
        String relativePath = serverProfile + dateStr + "/" + fileName + sName;
        //存入影像信息 返回结果msg
        String msg = iOcrImageService.insertOcrImage(imgId, relativePath);
        log.info("影像存储信息" + msg);

        File newFile = new File(filePath);
        file.transferTo(newFile);
        //调取接口进行识别 返回流水号

        String data = "{\"image_type\":\"0\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
        String request = HttpUtils.sendPost2(ocrUrl, data);

        log.info("**data****" + data);
        log.info("**request****" + request);
        if (StringUtils.isEmpty(request) || request.equals("[]")) {
            OcrTrade ocrTrade = new OcrTrade();
            ocrTrade.setId(UUID.randomUUID().toString());
            ocrTrade.setChannel("system");
            ocrTrade.setImageId(imgId);
            ocrTrade.setImageType("None");
            ocrTrade.setImageName("0");
            ocrTrade.setOcrStatus("1");
            ocrTrade.setTickStatus("2");
            ocrTrade.setPlatStatus("1");
            ocrTrade.setRemark2("0");
            ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            ocrTrade.setOcrTime(DateUtils.getTimeShort());
            iOcrTradeService.insertOcrTrade(ocrTrade);
            log.info("OCR识别结果为空");
            mmap.put("imgId", imgId);
            return AjaxResult.success(mmap);
        }
        String json = JSON.parseArray(request).toString();


        /**
         * 识别请求结果更新
         */
        OcrImage ocrImage = new OcrImage();
        ocrImage.setId(imgId);
        ocrImage.setOcrResult(json);
        iOcrImageService.updateOcrImage(ocrImage);

        List<RequestModel> models = JSONArray.parseArray(json, RequestModel.class);

        for (RequestModel model : models) {
            switch (model.getClass_name()) {
                case "IDCardFront":
                    IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                    idCardFront.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    iOcrTradeService.insertIDCardFront(idCardFront, "system", imgId);
                    break;
                case "IDCardBack":
                    IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                    idCardBack.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    iOcrTradeService.insertIDCardBack(idCardBack, "system", imgId);
                    break;
                case "BankCard":
                    BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                    bankCard.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    iOcrTradeService.insertBankCard(bankCard, "system", imgId);
                    break;
                case "Deposit":
                    DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                    deposit.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    iOcrTradeService.insertDeposit(deposit, "system", imgId);
                    break;
                default:
                    iOcrTradeService.insertNoneTrade(model.getOcr_result(), "system", imgId);
                    break;
            }
        }

        mmap.put("imgId", imgId);
        return AjaxResult.success(mmap);
    }

    /**
     * 图片识别结果返回
     */
    @GetMapping("/data/{imgId}")
    public String detail(@PathVariable("imgId") String imgId, ModelMap model) {
        OcrImage ocrImage = iOcrImageService.selectOcrImageById(imgId);
        model.addAttribute("imgUrl", ocrImage.getLocalPath());
        model.addAttribute("imgId", imgId);
        return prefix + "/data/data";
    }


    /**
     * 新增渠道
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


}
