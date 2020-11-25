package com.ocr.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.enums.BusinessType;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.VideoUtils;
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
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
@RequestMapping("/system/recognitionFiles")
public class RecognitionFilesController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RecognitionFilesController.class);

    private String prefix = "system/recognitionFiles";

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

    @RequiresPermissions("system:recognitionFiles:view")
    @GetMapping()
    public String recognition() {
        return prefix + "/recognitionFiles";
    }

    @GetMapping("/recognitionFilesVideo")
    public String recognitionVideo() {
        return prefix + "/recognitionFiles_video";
    }

    @GetMapping("/upload")
    public String upload() {
        return prefix + "/fileupload";
    }

    /**
     * 文件上传 放弃使用
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("system:recognitionFiles:fileUpload")
    @PostMapping("/fileUpload")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Log(title = "影像上传识别", businessType = BusinessType.OTHER)
    public AjaxResult fileUpload(@RequestParam("file") MultipartFile[] file, Map mmap) throws IOException {
        StringBuilder tradeIds = new StringBuilder();
//        StringBuffer tradeIds = new StringBuffer();
        if (file != null && file.length > 0) {
            try {
                for (int i = 0; i < file.length; i++) {
                    if (!file[i].isEmpty()) {
                        String dateStr = DateUtils.datePath();
                        String imgId = UUID.randomUUID().toString();
                        String oldFileName = file[i].getOriginalFilename();
                        String sName = oldFileName.substring(oldFileName.lastIndexOf("."));
                        //Long fileName = System.currentTimeMillis();
                        String fileName = UUID.randomUUID().toString();
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
                        file[i].transferTo(newFile);
                        //调取接口进行识别 返回流水号
                        String data = "{\"image_type\":\"0\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
                        String request = HttpUtils.sendPost2(ocrUrl, data);

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

                        List<RequestModel2> model2s = JSONArray.parseArray(json, RequestModel2.class);
                        String tradeId = "";
                        for (RequestModel2 model2 : model2s) {
                            List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);
                            for (RequestModel model : models) {
                                String riskFlag = model.getRisk_flag();
                                switch (model.getClass_name()) {
                                    case "IDCardFront":
                                        IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                        idCardFront.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertIDCardFront(idCardFront, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "IDCardBack":
                                        IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                        idCardBack.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertIDCardBack(idCardBack, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "BankCard":
                                        BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                                        bankCard.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertBankCard(bankCard, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "Deposit":
                                        DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                                        deposit.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertDeposit(deposit, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "PremisesPermit":
                                        PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                        premisesPermit.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertPremisesPermit(premisesPermit, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "ResidenceBooklet":
                                        ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                                        residenceBooklet.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertResidenceBooklet(residenceBooklet, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "MarriageLicense":
                                        MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                                        marriageLicense.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertMarriageLicense(marriageLicense, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "DrivingLicense":
                                        DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                                        drivingLicense.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertDrivingLicense(drivingLicense, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "DriversLicense":
                                        DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                                        driversLicense.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertDriversLicense(driversLicense, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "PlateNumber":
                                        PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                                        plateNumber.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertPlateNumber(plateNumber, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "BusinessLicense":
                                        BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                        businessLicense.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertBusinessLicense(businessLicense, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "VatInvoice":
                                        VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                                        vatInvoice.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertVatInvoice(vatInvoice, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "Invoice":
                                        Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                                        invoice.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertInvoice(invoice, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "Itinerary":
                                        Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                                        itinerary.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertItinerary(itinerary, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "RalTicket":
                                        RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                                        ralTicket.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertRalTicket(ralTicket, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "TollInvoice":
                                        TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                                        tollInvoice.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertTollInvoice(tollInvoice, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "QuotaInvoice":
                                        QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                                        quotaInvoice.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertQuotaInvoice(quotaInvoice, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    case "EleInvoice":
                                        EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                                        eleInvoice.setImgType(model.getClass_name());
                                        /**
                                         * 调用流水存储 返回流水id
                                         */
                                        tradeId = iOcrTradeService.insertEleInvoice(eleInvoice, "system", imgId,riskFlag);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                    default:
                                        tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), "system", imgId);
                                        tradeIds.append(tradeId + ",");
                                        break;
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return AjaxResult.error("文件为空");
        }

        mmap.put("tradeIds", tradeIds.deleteCharAt(tradeIds.length() - 1).toString());
        return AjaxResult.success(mmap);
    }

    /**
     * 文件上传，不包括通用文本识别
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("system:recognitionFiles:fileUpload")
    @PostMapping("/fileUpload2")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Log(title = "影像上传识别", businessType = BusinessType.OTHER)
    public AjaxResult fileUpload2(@RequestParam("file") MultipartFile[] file, Map mmap) throws IOException {
//        StringBuffer tradeIds = new StringBuffer();
        StringBuilder tradeIds = new StringBuilder();
        String channelCode = "system";
        if (file != null && file.length > 0) {
            StringBuilder buffer = new StringBuilder();
//            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < file.length; i++) {
                if (!file[i].isEmpty()) {
                    String dateStr = DateUtils.datePath();
                    String imgId = UUID.randomUUID().toString();
                    String oldFileName = file[i].getOriginalFilename();
                    String sName = oldFileName.substring(oldFileName.lastIndexOf("."));
                    //Long fileName = System.currentTimeMillis();
                    String fileName = UUID.randomUUID().toString();
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
                    file[i].transferTo(newFile);
                    String data = "{\"image_type\":\"0\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
                    buffer.append(data + ",");
                }
            }
            String requestData = "{\"data_list\":[" + buffer.deleteCharAt(buffer.length() - 1).toString() + "]}";
            String request = HttpUtils.sendPost2(ocrUrl, requestData);
            log.info("###request******:"+request);
            if (StringUtils.isEmpty(request) || request.equals("[]")) {
                return AjaxResult.error("识别资源占用中，请稍后重试！");
            }
            List<RequestModel2> model2s = JSONArray.parseArray(request, RequestModel2.class);
            //进行影像数据录入生成对应影像ID和相关url返回值
            for (RequestModel2 model2 : model2s) {
                //根据图片路径获取影像信息
                log.info("###filePath******:"+model2.getPath());
                String tradeId;
                OcrImage ocrImage = iOcrImageService.selectOcrImageByFilePath(model2.getPath());
                if (StringUtils.isEmpty(model2.getImage_result()) || model2.getImage_result().equals("[]")) {
                    tradeId = iOcrTradeService.insertNoneTrade(model2.getImage_result(), channelCode, ocrImage.getId());
                    tradeIds.append(tradeId + ",");
                    log.info("OCR识别结果为空");
                } else {
                    ocrImage.setOcrResult(model2.getImage_result());
                    iOcrImageService.updateOcrImage(ocrImage);
                    List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);
                    for (RequestModel model : models) {
                        String riskFlag = model.getRisk_flag();
                        switch (model.getClass_name()) {
                            case "IDCardFront":
                                IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                idCardFront.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                idCardFront.setRiskFlag(model.getRisk_flag());
                                tradeId = iOcrTradeService.insertIDCardFront(idCardFront, channelCode, ocrImage.getId(),riskFlag );
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardBack":
                                IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                idCardBack.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardBack(idCardBack, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BankCard":
                                BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                                bankCard.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBankCard(bankCard, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Deposit":
                                DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                                deposit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDeposit(deposit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PremisesPermit":
                                PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                premisesPermit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPremisesPermit(premisesPermit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "ResidenceBooklet":
                                ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                                residenceBooklet.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertResidenceBooklet(residenceBooklet, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "MarriageLicense":
                                MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                                marriageLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertMarriageLicense(marriageLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DrivingLicense":
                                DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                                drivingLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDrivingLicense(drivingLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DriversLicense":
                                DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                                driversLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDriversLicense(driversLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PlateNumber":
                                PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                                plateNumber.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPlateNumber(plateNumber, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BusinessLicense":
                                BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                businessLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBusinessLicense(businessLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "VatInvoice":
                                VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                                vatInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertVatInvoice(vatInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Invoice":
                                Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                                invoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertInvoice(invoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Itinerary":
                                Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                                itinerary.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertItinerary(itinerary, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "RalTicket":
                                RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                                ralTicket.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertRalTicket(ralTicket, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "TollInvoice":
                                TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                                tollInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertTollInvoice(tollInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "QuotaInvoice":
                                QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                                quotaInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertQuotaInvoice(quotaInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "EleInvoice":
                                EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                                eleInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertEleInvoice(eleInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            default:
                                tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, ocrImage.getId());
                                tradeIds.append(tradeId + ",");
                                break;
                        }
                    }
                }
            }
        } else {
            return AjaxResult.error("文件为空");
        }

        mmap.put("tradeIds", tradeIds.deleteCharAt(tradeIds.length() - 1).toString());
        return AjaxResult.success(mmap);
    }

    /**
     * 文件上传，包括通用文本识别
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("system:recognitionFiles:fileUpload")
    @PostMapping("/fileUploadIncludeGeneralText")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Log(title = "影像上传识别", businessType = BusinessType.OTHER)
    public AjaxResult fileUploadIncludeGeneralText(@RequestParam("file") MultipartFile[] file,HttpServletRequest req, Map mmap) throws IOException {
//        StringBuffer tradeIds = new StringBuffer();
        //图片类型
        String imgType=req.getParameter("imgType");

        StringBuilder tradeIds = new StringBuilder();
        String channelCode = "system";
        if (file != null && file.length > 0) {
            StringBuilder buffer = new StringBuilder();
//            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < file.length; i++) {
                if (!file[i].isEmpty()) {
                    String dateStr = DateUtils.datePath();
                    String imgId = UUID.randomUUID().toString();
                    String oldFileName = file[i].getOriginalFilename();
                    String sName = oldFileName.substring(oldFileName.lastIndexOf("."));
                    //Long fileName = System.currentTimeMillis();
                    String fileName = UUID.randomUUID().toString();
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
                    file[i].transferTo(newFile);

                    String data=null;
                    //新增通用文本识别判断
                    if(imgType.equals("10000")){
                        data = "{\"image_type\":\"10000\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
                    }else{
                        data = "{\"image_type\":\"0\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
                    }

                    buffer.append(data + ",");
                }
            }
            String requestData = "{\"data_list\":[" + buffer.deleteCharAt(buffer.length() - 1).toString() + "]}";
            String request = HttpUtils.sendPost2(ocrUrl, requestData);
            log.info("###request******:"+request);
            if (StringUtils.isEmpty(request) || request.equals("[]")) {
                return AjaxResult.error("识别资源占用中，请稍后重试！");
            }
            List<RequestModel2> model2s = JSONArray.parseArray(request, RequestModel2.class);
            //进行影像数据录入生成对应影像ID和相关url返回值
            for (RequestModel2 model2 : model2s) {
                //根据图片路径获取影像信息
                log.info("###filePath******:"+model2.getPath());
                String tradeId;
                OcrImage ocrImage = iOcrImageService.selectOcrImageByFilePath(model2.getPath());
                if (StringUtils.isEmpty(model2.getImage_result()) || model2.getImage_result().equals("[]")) {
                    tradeId = iOcrTradeService.insertNoneTrade(model2.getImage_result(), channelCode, ocrImage.getId());
                    tradeIds.append(tradeId + ",");
                    log.info("OCR识别结果为空");
                } else {
                    ocrImage.setOcrResult(model2.getImage_result());
                    iOcrImageService.updateOcrImage(ocrImage);
                    List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);
                    for (RequestModel model : models) {
                        String riskFlag = model.getRisk_flag();
                        switch (model.getClass_name()) {
                            case "IDCardFront":
                                IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                idCardFront.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardFront(idCardFront, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardBack":
                                IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                idCardBack.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardBack(idCardBack, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BankCard":
                                BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                                bankCard.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBankCard(bankCard, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Deposit":
                                DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                                deposit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDeposit(deposit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PremisesPermit":
                                PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                premisesPermit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPremisesPermit(premisesPermit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "ResidenceBooklet":
                                ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                                residenceBooklet.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertResidenceBooklet(residenceBooklet, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "MarriageLicense":
                                MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                                marriageLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertMarriageLicense(marriageLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DrivingLicense":
                                DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                                drivingLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDrivingLicense(drivingLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DriversLicense":
                                DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                                driversLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDriversLicense(driversLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PlateNumber":
                                PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                                plateNumber.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPlateNumber(plateNumber, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BusinessLicense":
                                BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                businessLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBusinessLicense(businessLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "VatInvoice":
                                VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                                vatInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertVatInvoice(vatInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Invoice":
                                Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                                invoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertInvoice(invoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Itinerary":
                                Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                                itinerary.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertItinerary(itinerary, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "RalTicket":
                                RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                                ralTicket.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertRalTicket(ralTicket, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "TollInvoice":
                                TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                                tollInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertTollInvoice(tollInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "QuotaInvoice":
                                QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                                quotaInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertQuotaInvoice(quotaInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "EleInvoice":
                                EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                                eleInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertEleInvoice(eleInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "GeneralText":
                                //ChannelTypeConstants
                                GeneralText generalText = JSONArray.parseObject(model.getOcr_result(), GeneralText.class);
                                generalText.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertGeneralText(generalText, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            /*case "IDCardFront_Video"://视频身份证正面
                                IDCardFront video_idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                video_idCardFront.setImgType(model.getClass_name());
                                *//**
                                 * 调用流水存储 返回流水id
                                 *//*
                                tradeId = iOcrTradeService.insertIDCardFront(video_idCardFront, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardBack_Video"://视频身份证反面
                                IDCardBack video_idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                video_idCardBack.setImgType(model.getClass_name());
                                *//**
                                 * 调用流水存储 返回流水id
                                 *//*
                                tradeId = iOcrTradeService.insertIDCardBack(video_idCardBack, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PremisesPermit_Video"://视频房本
                                PremisesPermit video_premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                video_premisesPermit.setImgType(model.getClass_name());
                                *//**
                                 * 调用流水存储 返回流水id
                                 *//*
                                tradeId = iOcrTradeService.insertPremisesPermit(video_premisesPermit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BusinessLicense_Video"://视频营业执照
                                BusinessLicense video_businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                video_businessLicense.setImgType(model.getClass_name());
                                *//**
                                 * 调用流水存储 返回流水id
                                 *//*
                                tradeId = iOcrTradeService.insertBusinessLicense(video_businessLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;*/
                            default:
                                tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, ocrImage.getId());
                                tradeIds.append(tradeId + ",");
                                break;
                        }
                    }
                }
            }
        } else {
            return AjaxResult.error("文件为空");
        }

        mmap.put("tradeIds", tradeIds.deleteCharAt(tradeIds.length() - 1).toString());
        return AjaxResult.success(mmap);
    }


    /**
     * 视频识别文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("system:recognitionFiles:fileUploadVideo")
    @PostMapping("/fileUploadVideo")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Log(title = "影像上传识别", businessType = BusinessType.OTHER)
    public AjaxResult fileUploadVideo(@RequestParam("file") MultipartFile[] file,HttpServletRequest req, Map mmap) throws IOException {
        //图片类型
        String imgType="10001";
        StringBuilder tradeIds = new StringBuilder();
        String channelCode = "system";
        String sName ="";
        if (file != null && file.length > 0) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < file.length; i++) {
                if (!file[i].isEmpty()) {
                    String dateStr = DateUtils.datePath();
                    String imgId = UUID.randomUUID().toString();
                    String oldFileName = file[i].getOriginalFilename();
                    sName = oldFileName.substring(oldFileName.lastIndexOf("."));
                    //Long fileName = System.currentTimeMillis();
                    String fileName = UUID.randomUUID().toString();
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
                    file[i].transferTo(newFile);

                    //从视频中抽取三张图片：start
                    ArrayList<String> pciList =new ArrayList<>();//存储图片路径
                    VideoUtils.videoExtractPic(newFile,path,pciList);
                    for(int k=0;k<pciList.size();k++){
                        //根据返回的图片路径组装
                        String data=null;
                        //视频通用识别判断
                        if(imgType.equals("10001")){
                            data = "{\"image_type\":\"10001\",\"path\":\"" + pciList.get(k) + "\",\"read_image_way\":\"3\"}";
                        }else{
                            data = "{\"image_type\":\"10001\",\"path\":\"" + pciList.get(k) + "\",\"read_image_way\":\"3\"}";
                        }

                        buffer.append(data + ",");
                    }
                    //从视频中抽取三张图片：end
                }
            }

            String requestData = "{\"data_list\":[" + buffer.deleteCharAt(buffer.length() - 1).toString() + "]}";
            String request = HttpUtils.sendPost2(ocrUrl, requestData);

            //身份证反面的测试数据：start
            //String requestTest="[{\"path\":\"http://192.168.110.230:8080/OCR/profile/IMAGE/2020/11/05/7a9dc2d0-5ed5-40f4-84be-0faea924914e.jpg\",\"image_result\":[{\"ocr_result\":{\"endDate\":\"20220903\",\"authority\":\"伊春市公安局西林分局\",\"startDate\":\"20120903\"},\"class_name\":\"IDCardBack\"}]}]";
            //String request= "[{\"path\":\""+relativePathTest+"\",\"image_result\":[{\"ocr_result\":{\"endDate\":\"20220903\",\"authority\":\"伊春市公安局西林分局\",\"startDate\":\"20120903\"},\"class_name\":\"Video_IDCardBack\"}]}]";
            //end

            log.info("###request******:"+request);
            if (StringUtils.isEmpty(request) || request.equals("[]")) {
                return AjaxResult.error("识别资源占用中，请稍后重试！");
            }
            List<RequestModel2> model2s = JSONArray.parseArray(request, RequestModel2.class);
            //进行影像数据录入生成对应影像ID和相关url返回值
            for (RequestModel2 model2 : model2s) {
                //根据图片路径获取影像信息
                log.info("###filePath******:"+model2.getPath());
                String tradeId;
                String oracleImagePath=model2.getPath().substring(0,model2.getPath().indexOf(".jpg")-2)+sName;//路径的生成是固定的，所以可以这样截取
                OcrImage ocrImage = iOcrImageService.selectOcrImageByFilePath(oracleImagePath);
                if (StringUtils.isEmpty(model2.getImage_result()) || model2.getImage_result().equals("[]")) {
                    tradeId = iOcrTradeService.insertNoneTrade(model2.getImage_result(), channelCode, ocrImage.getId());
                    tradeIds.append(tradeId + ",");
                    log.info("OCR识别结果为空");
                } else {
                    ocrImage.setOcrResult(model2.getImage_result());
                    iOcrImageService.updateOcrImage(ocrImage);
                    List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);
                    for (RequestModel model : models) {
                        String riskFlag = model.getRisk_flag();
                        switch (model.getClass_name()) {
                            case "IDCardFront":
                                IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                idCardFront.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardFront(idCardFront, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardBack":
                                IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                idCardBack.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardBack(idCardBack, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BankCard":
                                BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                                bankCard.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBankCard(bankCard, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Deposit":
                                DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                                deposit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDeposit(deposit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PremisesPermit":
                                PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                premisesPermit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPremisesPermit(premisesPermit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "ResidenceBooklet":
                                ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                                residenceBooklet.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertResidenceBooklet(residenceBooklet, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "MarriageLicense":
                                MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                                marriageLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertMarriageLicense(marriageLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DrivingLicense":
                                DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                                drivingLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDrivingLicense(drivingLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "DriversLicense":
                                DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                                driversLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertDriversLicense(driversLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PlateNumber":
                                PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                                plateNumber.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPlateNumber(plateNumber, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BusinessLicense":
                                BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                businessLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBusinessLicense(businessLicense, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "VatInvoice":
                                VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                                vatInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertVatInvoice(vatInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Invoice":
                                Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                                invoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertInvoice(invoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "Itinerary":
                                Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                                itinerary.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertItinerary(itinerary, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "RalTicket":
                                RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                                ralTicket.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertRalTicket(ralTicket, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "TollInvoice":
                                TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                                tollInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertTollInvoice(tollInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "QuotaInvoice":
                                QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                                quotaInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertQuotaInvoice(quotaInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "EleInvoice":
                                EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                                eleInvoice.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertEleInvoice(eleInvoice, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "GeneralText":
                                //ChannelTypeConstants
                                GeneralText generalText = JSONArray.parseObject(model.getOcr_result(), GeneralText.class);
                                generalText.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertGeneralText(generalText, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardFront_Video"://视频身份证正面
                                IDCardFront video_idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                                video_idCardFront.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardFront(video_idCardFront, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "IDCardBack_Video"://视频身份证反面
                                IDCardBack video_idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                                video_idCardBack.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertIDCardBack(video_idCardBack, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "PremisesPermit_Video"://视频房本
                                PremisesPermit video_premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                                video_premisesPermit.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertPremisesPermit(video_premisesPermit, channelCode, ocrImage.getId(),riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            case "BusinessLicense_Video"://视频营业执照
                                BusinessLicense video_businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                                video_businessLicense.setImgType(model.getClass_name());
                                /**
                                 * 调用流水存储 返回流水id
                                 */
                                tradeId = iOcrTradeService.insertBusinessLicense(video_businessLicense, channelCode, ocrImage.getId(), riskFlag);
                                tradeIds.append(tradeId + ",");
                                break;
                            default:
                                tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, ocrImage.getId());
                                tradeIds.append(tradeId + ",");
                                break;
                        }
                    }
                }
            }
        } else {
            return AjaxResult.error("文件为空");
        }

        mmap.put("tradeIds", tradeIds.deleteCharAt(tradeIds.length() - 1).toString());
        return AjaxResult.success(mmap);
    }


    /**
     * 图片识别结果返回
     */
    @GetMapping("/data/{tradeIds}")
    public String detail(@PathVariable("tradeIds") String tradeIds, ModelMap model) {
        model.addAttribute("tradeIds", tradeIds);
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
