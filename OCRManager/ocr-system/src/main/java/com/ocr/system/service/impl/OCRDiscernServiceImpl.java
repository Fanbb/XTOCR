package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.constant.ChannelTypeConstants;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.exception.file.InvalidExtensionException;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.FileTypeUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.FileUploadUtils;
import com.ocr.common.utils.file.ImageBase64;
//import com.ocr.common.utils.file.VideoUtils;
import com.ocr.common.utils.http.HttpUtils;
import com.ocr.system.domain.ChannelType;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.*;
import com.ocr.system.service.*;
import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.ClientBatchBean;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;
import com.sunyard.ecm.server.bean.BatchBean;
import com.sunyard.ecm.server.bean.BatchFileBean;
import com.sunyard.ecm.server.bean.FileBean;
import com.sunyard.util.TransOptionKey;
import com.sunyard.ws.utils.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;


/**
 * OCR调用业务处理
 *
 * @author ocr
 * @date 2020-05-19
 */
@Service
public class OCRDiscernServiceImpl implements OCRDiscernService {

    private static final Logger log = LoggerFactory.getLogger(OCRDiscernServiceImpl.class);

    @Autowired
    IOcrTradeService iOcrTradeService;

    @Autowired
    IOcrImageService iOcrImageService;

    @Autowired
    private IChannelTypeService iChannelTypeService;

    @Autowired
    private ISysConfigService configService;

    @Value("${ocr.profile}")
    private String imgUploadPath;

    @Value("${ocr.serverProfile}")
    private String serverProfile;

    @Value("${ocr.ocrUrl}")
    private String ocrUrl;

    @Override
    public ResultData runMore(String channelCode, String url, String str) {
        /**
         * 1.获取存储影像 录入数据库影像信息
         * 2.调用ocr接口识别
         * 3.处理ocr识别结果
         *  3.1新增流水数据，录入数据库
         *  3.2更新影像信息
         * 4.返回结果
         */
        ResultData resultData = new ResultData();

        return resultData;
    }

    @Override
    public int modifyResult(String tradeId) {
        OcrTrade ocrTrade = new OcrTrade();
        ocrTrade.setId(tradeId);
        ocrTrade.setPlatStatus("1");
        return iOcrTradeService.updateOcrTrade(ocrTrade);
    }

    @Override
    public ResultData runOneAgain(String channelCode, String imgUrl, String imgStr, String imgType) {
        /**
         * 1.获取存储影像 录入数据库影像信息
         * 2.调用ocr接口识别
         * 3.处理ocr识别结果
         *  3.1新增流水数据，录入数据库
         *  3.2更新影像信息
         * 4.返回结果
         */
        ResultData resultData = new ResultData();
        //String fileName = System.currentTimeMillis() + "";
        String fileName = UUID.randomUUID().toString() + "";
        String sName = "";
        String dateStr = DateUtils.datePath() + "/" + fileName;
        String path = imgUploadPath + "/IMAGE/" + dateStr;
        if (StringUtils.isNotEmpty(imgUrl)) {
            sName = imgUrl.substring(imgUrl.lastIndexOf("."));
            imgStr = ImageBase64.imageToBase64ByUrl(imgUrl);
            ImageBase64.base64StringToImageSave(imgStr, path + sName);
        } else if (StringUtils.isNotEmpty(imgStr)) {
            sName = "." + FileTypeUtils.getFromBASE64(imgStr);
            ImageBase64.base64StringToImageSave(imgStr, path + sName);
        }
        ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
        if (!imgType.equals("0")) {
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }
        String relativePath = serverProfile + dateStr;

        String data = "{\"data_list\":[{\"image_type\" :\"" + imgType + "\",\"path\":\"" + relativePath + sName + "\",\"read_image_way\":\"3\"}]}";
        String request = HttpUtils.sendPost2(ocrUrl, data);
        String imgId = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(request) || request.equals("[]") || JSONArray.parseArray(JSON.parseArray(request).toString(), RequestModel2.class).get(0).getImage_result().equals("[]")) {
            OcrImage image = new OcrImage();
            image.setId(imgId);
            image.setOcrDate(new Date());
            image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            image.setLocalPath(relativePath + sName);
            image.setParentId(imgId);
            iOcrImageService.insertOcrImage(image);//ocr_image
            //判断图片的类型
            String imgName = ChannelTypeConstants.getChannelType().get(imgType);
            /*
            switch (imgType) {
                case "1":
                    imgName = "IDCardFront";
                    break;
                case "2":
                    imgName = "BankCard";
                    break;
                case "3":
                    imgName = "Deposit";
                    break;
                case "4":
                    imgName = "PremisesPermit";
                    break;
                case "6":
                    imgName = "ResidenceBooklet";
                    break;
                case "7":
                    imgName = "MarriageLicense";
                    break;
                case "8":
                    imgName = "DrivingLicense";
                    break;
                case "9":
                    imgName = "DriversLicense";
                    break;
                case "10":
                    imgName = "PlateNumber";
                    break;
                case "11":
                    imgName = "BusinessLicense";
                    break;
                case "12":
                    imgName = "VatInvoice";
                    break;
                case "13":
                    imgName = "Invoice";
                    break;
                case "14":
                    imgName = "Itinerary";
                    break;
                case "15":
                    imgName = "RalTicket";
                    break;
                case "16":
                    imgName = "TollInvoice";
                    break;
                case "17":
                    imgName = "QuotaInvoice";
                    break;
                case "18":
                    imgName = "EleInvoice";
                    break;
                case "10000":
                    imgName = "text";
                    break;
            }
            */
            log.info("OCR识别结果为空");
            OcrTrade ocrTrade = new OcrTrade();
            ocrTrade.setId(UUID.randomUUID().toString());
            ocrTrade.setChannel(channelCode);
            ocrTrade.setImageId(imgId);
            ocrTrade.setImageType(imgName==null ? "None" : imgName);
            ocrTrade.setImageName(imgType);
            ocrTrade.setTickStatus("2");
            ocrTrade.setOcrStatus("1");
            ocrTrade.setPlatStatus("1");
            ocrTrade.setRemark2("0");
            ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            ocrTrade.setOcrTime(DateUtils.getTimeShort());
            iOcrTradeService.insertOcrTrade(ocrTrade);

            resultData.setMsg("OCR识别结果为空！");
            resultData.setType("0");
            return resultData;
        }
        String json = JSON.parseArray(request).toString();

        OcrImage image = new OcrImage();
        image.setId(imgId);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(relativePath + sName);
        image.setParentId(imgId);
        image.setOcrResult(json);
        iOcrImageService.insertOcrImage(image);

        List<RequestModel2> model2s = JSONArray.parseArray(json, RequestModel2.class);
        RequestModel2 model2 = model2s.get(0);
        List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);

        List list = new ArrayList();
        for (RequestModel model : models) {
            if (null == model.getClass_name()) {
                resultData.setMsg("OCR识别结果为空！");
                resultData.setType("0");
                return resultData;
            }

            //判断是否有对应的权限start
            //身份证特殊处理
            if(model.getClass_name().equals(ChannelTypeConstants.getChannelType().get("IDCardFront")) || model.getClass_name().equals(ChannelTypeConstants.getChannelType().get("IDCardBack"))){
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "1");
                if (null == channelType1) {
                    resultData.setMsg("无身份证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            //其他
            if (model.getClass_name().equals(ChannelTypeConstants.getChannelType().get(model.getClass_name()))) {
                String num=ChannelTypeConstants.getChannelType2().get(model.getClass_name());
                String ChannelName=ChannelTypeConstants.getChannelType2().get(num);

                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, num);

                if (null == channelType1) {
                    resultData.setMsg("无"+ChannelName+"识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            //判断是否有对应的权限end

            /*
            if (model.getClass_name().equals("BankCard")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "2");

                if (null == channelType1) {
                    resultData.setMsg("无银行卡识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }

            }
            if (model.getClass_name().equals("Deposit")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "3");

                if (null == channelType1) {
                    resultData.setMsg("无存单识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("PremisesPermit")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "4");

                if (null == channelType1) {
                    resultData.setMsg("无房本识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("ResidenceBooklet")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "6");

                if (null == channelType1) {
                    resultData.setMsg("无户口本识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("MarriageLicense")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "7");

                if (null == channelType1) {
                    resultData.setMsg("无结婚证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("DrivingLicense")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "8");

                if (null == channelType1) {
                    resultData.setMsg("无行驶证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("DriversLicense")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "9");

                if (null == channelType1) {
                    resultData.setMsg("无驾驶证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("PlateNumber")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "10");

                if (null == channelType1) {
                    resultData.setMsg("无车牌号识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("BusinessLicense")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "11");

                if (null == channelType1) {
                    resultData.setMsg("无营业执照识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }

            if (model.getClass_name().equals("VatInvoice")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "12");

                if (null == channelType1) {
                    resultData.setMsg("无增值税发票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("Invoice")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "13");

                if (null == channelType1) {
                    resultData.setMsg("无普通发票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("Itinerary")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "14");

                if (null == channelType1) {
                    resultData.setMsg("无航空运输电子客票行程单识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("RalTicket")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "15");

                if (null == channelType1) {
                    resultData.setMsg("无火车票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("TollInvoice")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "16");

                if (null == channelType1) {
                    resultData.setMsg("无通行费发票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("QuotaInvoice")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "17");

                if (null == channelType1) {
                    resultData.setMsg("无定额发票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("EleInvoice")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "18");

                if (null == channelType1) {
                    resultData.setMsg("无电子发票识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }*/
            String tradeId;
            Boolean flag = true;
            switch (model.getClass_name()) {
                case "IDCardFront":
                    IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                    idCardFront.setImgType(model.getClass_name());
                    idCardFront.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(idCardFront.getSex()) || StringUtils.isEmpty(idCardFront.getNation()) || StringUtils.isEmpty(idCardFront.getName()) || StringUtils.isEmpty(idCardFront.getBirthday()) || StringUtils.isEmpty(idCardFront.getAddress()) || StringUtils.isEmpty(idCardFront.getIdCardNo())) {
                        flag = false;
                        idCardFront.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertIDCardFrontFlag(idCardFront, channelCode, imgId, flag);
                    idCardFront.setTradeId(tradeId);
                    list.add(idCardFront);
                    break;
                case "IDCardBack":
                    IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                    idCardBack.setImgType(model.getClass_name());
                    idCardBack.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(idCardBack.getStartDate()) || StringUtils.isEmpty(idCardBack.getEndDate()) || StringUtils.isEmpty(idCardBack.getAuthority())) {
                        flag = false;
                        idCardBack.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertIDCardBackFlag(idCardBack, channelCode, imgId, flag);
                    idCardBack.setTradeId(tradeId);
                    list.add(idCardBack);
                    break;
                case "BankCard":
                    BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                    bankCard.setImgType(model.getClass_name());
                    bankCard.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(bankCard.getBankCardNo())) {
                        flag = false;
                        bankCard.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertBankCardFlag(bankCard, channelCode, imgId, flag);
                    bankCard.setTradeId(tradeId);
                    list.add(bankCard);
                    break;
                case "Deposit":
                    DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                    deposit.setImgType(model.getClass_name());
                    deposit.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(deposit.getDepositNo()) || StringUtils.isEmpty(deposit.getAccNo()) || StringUtils.isEmpty(deposit.getAmt()) || StringUtils.isEmpty(deposit.getAmtCapital()) || StringUtils.isEmpty(deposit.getName())) {
                        flag = false;
                        deposit.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertDepositFlag(deposit, channelCode, imgId, flag);
                    deposit.setTradeId(tradeId);
                    list.add(deposit);
                    break;
                case "PremisesPermit":
                    PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                    premisesPermit.setImgType(model.getClass_name());
                    premisesPermit.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(premisesPermit.getBuiltArea())||StringUtils.isEmpty(premisesPermit.getCertificateNo())||StringUtils.isEmpty(premisesPermit.getFloorArea())||StringUtils.isEmpty(premisesPermit.getName())||StringUtils.isEmpty(premisesPermit.getLocation())||StringUtils.isEmpty(premisesPermit.getPurpose())||StringUtils.isEmpty(premisesPermit.getLandUse())||StringUtils.isEmpty(premisesPermit.getStructure())) {
                        flag = false;
                        premisesPermit.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertPremisesPermitFlag(premisesPermit, channelCode, imgId, flag);
                    premisesPermit.setTradeId(tradeId);
                    list.add(premisesPermit);
                    break;
                case "ResidenceBooklet":
                    ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                    residenceBooklet.setImgType(model.getClass_name());
                    residenceBooklet.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(residenceBooklet.getAddress())||StringUtils.isEmpty(residenceBooklet.getNativePlace())) {
                        flag = false;
                        residenceBooklet.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertResidenceBookletFlag(residenceBooklet, channelCode, imgId, flag);
                    residenceBooklet.setTradeId(tradeId);
                    list.add(residenceBooklet);
                    break;
                case "MarriageLicense":
                    MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                    marriageLicense.setImgType(model.getClass_name());
                    marriageLicense.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(marriageLicense.getMarriageNo())||StringUtils.isEmpty(marriageLicense.getIdCardNo())||StringUtils.isEmpty(marriageLicense.getName())) {
                        flag = false;
                        marriageLicense.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertMarriageLicenseFlag(marriageLicense, channelCode, imgId, flag);
                    marriageLicense.setTradeId(tradeId);
                    list.add(marriageLicense);
                    break;
                case "DrivingLicense":
                    DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                    drivingLicense.setImgType(model.getClass_name());
                    drivingLicense.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(drivingLicense.getFileNumber())||StringUtils.isEmpty(drivingLicense.getAddress())||StringUtils.isEmpty(drivingLicense.getBrandModel())||StringUtils.isEmpty(drivingLicense.getEngineNumber())||StringUtils.isEmpty(drivingLicense.getGabarite())||StringUtils.isEmpty(drivingLicense.getIdentifyCode())||StringUtils.isEmpty(drivingLicense.getInspectionRecord())||StringUtils.isEmpty(drivingLicense.getIssueDate())||StringUtils.isEmpty(drivingLicense.getIssueUnit())||StringUtils.isEmpty(drivingLicense.getNumber())||StringUtils.isEmpty(drivingLicense.getOwner())||StringUtils.isEmpty(drivingLicense.getPlateNumber())||StringUtils.isEmpty(drivingLicense.getRatifiedMass())||StringUtils.isEmpty(drivingLicense.getRegistrationDate())||StringUtils.isEmpty(drivingLicense.getRemark())||StringUtils.isEmpty(drivingLicense.getTotalMass())||StringUtils.isEmpty(drivingLicense.getTractionMass())||StringUtils.isEmpty(drivingLicense.getUnladenMass())||StringUtils.isEmpty(drivingLicense.getUseNature())||StringUtils.isEmpty(drivingLicense.getVehicleType())) {
                        flag = false;
                        drivingLicense.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertDrivingLicenseFlag(drivingLicense, channelCode, imgId, flag);
                    drivingLicense.setTradeId(tradeId);
                    list.add(drivingLicense);
                    break;
                case "DriversLicense":
                    DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                    driversLicense.setImgType(model.getClass_name());
                    driversLicense.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(driversLicense.getFileNumber())||StringUtils.isEmpty(driversLicense.getAddress())||StringUtils.isEmpty(driversLicense.getBirthDate())||StringUtils.isEmpty(driversLicense.getCardNo())||StringUtils.isEmpty(driversLicense.getDrivingType())||StringUtils.isEmpty(driversLicense.getEndDate())||StringUtils.isEmpty(driversLicense.getIssueDate())||StringUtils.isEmpty(driversLicense.getName())||StringUtils.isEmpty(driversLicense.getNationality())||StringUtils.isEmpty(driversLicense.getRecord())||StringUtils.isEmpty(driversLicense.getSex())||StringUtils.isEmpty(driversLicense.getStartDate())) {
                        flag = false;
                        driversLicense.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertDriversLicenseFlag(driversLicense, channelCode, imgId, flag);
                    driversLicense.setTradeId(tradeId);
                    list.add(driversLicense);
                    break;
                case "PlateNumber":
                    PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                    plateNumber.setImgType(model.getClass_name());
                    plateNumber.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(plateNumber.getNumber())) {
                        flag = false;
                        plateNumber.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertPlateNumberFlag(plateNumber, channelCode, imgId, flag);
                    plateNumber.setTradeId(tradeId);
                    list.add(plateNumber);
                    break;
                case "BusinessLicense":
                    BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                    businessLicense.setImgType(model.getClass_name());
                    businessLicense.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(businessLicense.getSocialCode())||StringUtils.isEmpty(businessLicense.getAddress())||StringUtils.isEmpty(businessLicense.getBusinessScope())||StringUtils.isEmpty(businessLicense.getBusinessTerm())||StringUtils.isEmpty(businessLicense.getCompanyName())||StringUtils.isEmpty(businessLicense.getLegalPerson())||StringUtils.isEmpty(businessLicense.getRegisterDate())||StringUtils.isEmpty(businessLicense.getRegisteredCapital())||StringUtils.isEmpty(businessLicense.getVertical())
                    ) {
                        flag = false;
                        businessLicense.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertBusinessLicenseFlag(businessLicense, channelCode, imgId, flag);
                    businessLicense.setTradeId(tradeId);
                    list.add(businessLicense);
                    break;
                case "VatInvoice":
                    VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                    vatInvoice.setImgType(model.getClass_name());
                    vatInvoice.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(vatInvoice.getCode())||StringUtils.isEmpty(vatInvoice.getIssuedDate())||StringUtils.isEmpty(vatInvoice.getNumber())||StringUtils.isEmpty(vatInvoice.getPurchaserName())||StringUtils.isEmpty(vatInvoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(vatInvoice.getSellerName())||StringUtils.isEmpty(vatInvoice.getSellerTaxpayerNum())||StringUtils.isEmpty(vatInvoice.getValueAddedTax())||StringUtils.isEmpty(vatInvoice.getSubjects())) {
                        flag = false;
                        vatInvoice.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertVatInvoiceFlag(vatInvoice, channelCode, imgId, flag);
                    vatInvoice.setTradeId(tradeId);
                    list.add(vatInvoice);
                    break;
                case "Invoice":
                    Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                    invoice.setImgType(model.getClass_name());
                    invoice.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(invoice.getCode())||StringUtils.isEmpty(invoice.getIssuedDate())||StringUtils.isEmpty(invoice.getNumber())||StringUtils.isEmpty(invoice.getPurchaserName())||StringUtils.isEmpty(invoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(invoice.getSellerName())||StringUtils.isEmpty(invoice.getSellerTaxpayerNum())||StringUtils.isEmpty(invoice.getValueAddedTax())||StringUtils.isEmpty(invoice.getSubjects())) {
                        flag = false;
                        invoice.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertInvoiceFlag(invoice, channelCode, imgId, flag);
                    invoice.setTradeId(tradeId);
                    list.add(invoice);
                    break;
                case "Itinerary":
                    Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                    itinerary.setImgType(model.getClass_name());
                    itinerary.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(itinerary.getAmt())||StringUtils.isEmpty(itinerary.getDate())||StringUtils.isEmpty(itinerary.getStartingStation())||StringUtils.isEmpty(itinerary.getFlight())||StringUtils.isEmpty(itinerary.getName())||StringUtils.isEmpty(itinerary.getSeatLevel())||StringUtils.isEmpty(itinerary.getEndingStation())) {
                        flag = false;
                        itinerary.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertItineraryFlag(itinerary, channelCode, imgId, flag);
                    itinerary.setTradeId(tradeId);
                    list.add(itinerary);
                    break;
                case "RalTicket":
                    RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                    ralTicket.setImgType(model.getClass_name());
                    ralTicket.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(ralTicket.getAmt())||StringUtils.isEmpty(ralTicket.getDate())||StringUtils.isEmpty(ralTicket.getStartingStation())||StringUtils.isEmpty(ralTicket.getName())||StringUtils.isEmpty(ralTicket.getSeatLevel())||StringUtils.isEmpty(ralTicket.getTrainNumber())||StringUtils.isEmpty(ralTicket.getEndingStation())) {
                        flag = false;
                        ralTicket.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertRalTicketFlag(ralTicket, channelCode, imgId, flag);
                    ralTicket.setTradeId(tradeId);
                    list.add(ralTicket);
                    break;
                case "TollInvoice":
                    TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                    tollInvoice.setImgType(model.getClass_name());
                    tollInvoice.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(tollInvoice.getAmt())||StringUtils.isEmpty(tollInvoice.getInvoiceCode())||StringUtils.isEmpty(tollInvoice.getInvoiceNumber())) {
                        flag = false;
                        tollInvoice.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertTollInvoiceFlag(tollInvoice, channelCode, imgId, flag);
                    tollInvoice.setTradeId(tradeId);
                    list.add(tollInvoice);
                    break;
                case "QuotaInvoice":
                    QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                    quotaInvoice.setImgType(model.getClass_name());
                    quotaInvoice.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(quotaInvoice.getAmt())||StringUtils.isEmpty(quotaInvoice.getInvoiceCode())||StringUtils.isEmpty(quotaInvoice.getInvoiceNumber())) {
                        flag = false;
                        quotaInvoice.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertQuotaInvoiceFlag(quotaInvoice, channelCode, imgId, flag);
                    quotaInvoice.setTradeId(tradeId);
                    list.add(quotaInvoice);
                    break;
                case "EleInvoice":
                    EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                    eleInvoice.setImgType(model.getClass_name());
                    eleInvoice.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (StringUtils.isEmpty(eleInvoice.getCode())||StringUtils.isEmpty(eleInvoice.getIssuedDate())||StringUtils.isEmpty(eleInvoice.getNumber())||StringUtils.isEmpty(eleInvoice.getPurchaserName())||StringUtils.isEmpty(eleInvoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(eleInvoice.getSellerName())||StringUtils.isEmpty(eleInvoice.getSellerTaxpayerNum())||StringUtils.isEmpty(eleInvoice.getValueAddedTax())||StringUtils.isEmpty(eleInvoice.getSubjects())) {
                        flag = false;
                        eleInvoice.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertEleInvoiceFlag(eleInvoice, channelCode, imgId, flag);
                    eleInvoice.setTradeId(tradeId);
                    list.add(eleInvoice);
                    break;
                case "GeneralText":
                    GeneralText generalText = JSONArray.parseObject(model.getOcr_result(), GeneralText.class);
                    generalText.setImgType(model.getClass_name());
                    generalText.setFlag("true");
                    /**
                     * 调用流水存储 返回流水id
                     */
                    if (generalText.hasEmptyField()) {
                        flag = false;
                        generalText.setFlag("false");
                    }
                    tradeId = iOcrTradeService.insertGeneralTestFlag(generalText, channelCode, imgId, flag);
                    generalText.setTradeId(tradeId);
                    list.add(generalText);
                    break;
                default:
                    tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, imgId);
                    NoneEnty noneEnty = new NoneEnty();
                    noneEnty.setImgType("None");
                    noneEnty.setTradeId(tradeId);
                    list.add(noneEnty);
                    break;
            }
        }
        resultData.setData(list);
        /**
         * 流水ids
         */
        if (list.size() > 0) {
            resultData.setMsg("识别成功！");
            resultData.setType("1");
        } else {
            resultData.setMsg("识别失败！");
            resultData.setType("0");
        }
        return resultData;
    }


    @Override
    public ResultData runOne(String channelCode, String url, String str, String imgType) {
        /**
         * 1.获取存储影像 录入数据库影像信息
         * 2.调用ocr接口识别
         * 3.处理ocr识别结果
         *  3.1新增流水数据，录入数据库
         *  3.2更新影像信息
         * 4.返回结果
         */
        ResultData resultData = new ResultData();
        //String fileName = System.currentTimeMillis() + "";
        String fileName = UUID.randomUUID().toString();
        String sName = "";
        String dateStr = DateUtils.datePath() + "/" + fileName;

        String path = imgUploadPath + "/IMAGE/" + dateStr;
        if (StringUtils.isNotEmpty(url)) {
            /**
             * 图片url不为空
             */
            sName = url.substring(url.lastIndexOf("."));
            str = ImageBase64.imageToBase64ByUrl(url);
            ImageBase64.base64StringToImageSave(str, path + sName);
        } else if (StringUtils.isNotEmpty(str)) {
            /**
             * 图片base64不为空
             */
            sName = "." + FileTypeUtils.getFromBASE64(str);
            ImageBase64.base64StringToImageSave(str, path + sName);
        }
        ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
        if (!imgType.equals("0")) {
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }
        String relativePath = serverProfile + dateStr;
        String data = "{\"image_type\" :\"" + imgType + "\",\"path\":\"" + relativePath + sName + "\",\"read_image_way\":\"3\"}";

        String request = HttpUtils.sendPost2(ocrUrl, data);
        String imgId = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(request) || request.equals("[]")) {
            OcrImage image = new OcrImage();
            image.setId(imgId);
            image.setOcrDate(new Date());
            image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            image.setLocalPath(relativePath + sName);
            image.setParentId(imgId);
            iOcrImageService.insertOcrImage(image);
            String imgName = "";
            switch (imgType) {
                case "1":
                    imgName = "IDCardFront";
                    break;
                case "2":
                    imgName = "BankCard";
                    break;
                case "3":
                    imgName = "Deposit";
                    break;
            }

            log.info("OCR识别结果为空");
            OcrTrade ocrTrade = new OcrTrade();
            ocrTrade.setId(UUID.randomUUID().toString());
            ocrTrade.setChannel(channelCode);
            ocrTrade.setImageId(imgId);
            ocrTrade.setImageType(imgName);
            ocrTrade.setImageName(imgType);
            ocrTrade.setTickStatus("2");
            ocrTrade.setOcrStatus("1");
            ocrTrade.setPlatStatus("1");
            ocrTrade.setRemark2("0");
            ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            ocrTrade.setOcrTime(DateUtils.getTimeShort());
            iOcrTradeService.insertOcrTrade(ocrTrade);

            resultData.setMsg("OCR识别结果为空！");
            resultData.setType("0");
            return resultData;
        }
        String json = JSON.parseArray(request).toString();

        OcrImage image = new OcrImage();
        image.setId(imgId);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(relativePath + sName);
        image.setParentId(imgId);
        image.setOcrResult(json);
        iOcrImageService.insertOcrImage(image);

        List<RequestModel> models = JSONArray.parseArray(json, RequestModel.class);

        List list = new ArrayList();
        for (RequestModel model : models) {
            if (null == model.getClass_name()) {
                resultData.setMsg("OCR识别结果为空！");
                resultData.setType("0");
                return resultData;
            }
            if (model.getClass_name().equals("IDCardFront") || model.getClass_name().equals("IDCardFront")) {
                if (!imgType.equals("1") && !imgType.equals("0")) {
                    resultData.setMsg("无身份证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            if (model.getClass_name().equals("BankCard")) {
                if (!imgType.equals("2") && !imgType.equals("0")) {
                    resultData.setMsg("无银行卡识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }

            }
            if (model.getClass_name().equals("Deposit")) {
                if (!imgType.equals("3") && !imgType.equals("0")) {
                    resultData.setMsg("无存单识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }

            String tradeId;
            switch (model.getClass_name()) {
                case "IDCardFront":
                    IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                    idCardFront.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertIDCardFront(idCardFront, channelCode, imgId);
                    idCardFront.setTradeId(tradeId);
                    list.add(idCardFront);
                    break;
                case "IDCardBack":
                    IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                    idCardBack.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertIDCardBack(idCardBack, channelCode, imgId);
                    idCardBack.setTradeId(tradeId);
                    list.add(idCardBack);
                    break;
                case "BankCard":
                    BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                    bankCard.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertBankCard(bankCard, channelCode, imgId);
                    bankCard.setTradeId(tradeId);
                    list.add(bankCard);
                    break;
                case "Deposit":
                    DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                    deposit.setImgType(model.getClass_name());
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertDeposit(deposit, channelCode, imgId);
                    deposit.setTradeId(tradeId);
                    list.add(deposit);
                    break;
                default:
                    tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, imgId);
                    NoneEnty noneEnty = new NoneEnty();
                    noneEnty.setImgType("None");
                    noneEnty.setTradeId(tradeId);
                    list.add(noneEnty);
                    break;
            }
        }
        resultData.setData(list);
        /**
         * 流水ids
         */
        if (list.size() > 0) {
            resultData.setMsg("识别成功！");
            resultData.setType("1");
        } else {
            resultData.setMsg("识别失败！");
            resultData.setType("0");
        }
        return resultData;
    }

    /**
     * 查询影像并将影像下载到DOWN_LOAD_FILE_PATH目录下
     */
    private Map queryAndDownload(String batchNumber, String modelCode, String userName, String passWord, String createDate, String filePartName, String identificationCode,int maxCount) {
        Map map = new HashMap();
        SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(configService.selectConfigByKey("sys.screenage.ip"), Integer.parseInt(configService.selectConfigByKey("sys.screenage.port")));
        ClientBatchBean clientBatchBean = new ClientBatchBean();
        clientBatchBean.setModelCode(modelCode);
        clientBatchBean.setUser(userName);
        clientBatchBean.setPassWord(passWord);
        clientBatchBean.getIndex_Object().setContentID(batchNumber);
        clientBatchBean.getIndex_Object().addCustomMap("CREATEDATE", createDate);
        clientBatchBean.setDownLoad(true);
        try {
            String resultMsg = clientApi.queryBatch(clientBatchBean, "WHGroup");
            log.info("#######查询批次返回的信息[" + resultMsg + "]#######");
            String batchStr = resultMsg.split(TransOptionKey.SPLITSYM)[1];
            List<BatchBean> batchBeans = XMLUtil.xml2list(XMLUtil.removeHeadRoot(batchStr), BatchBean.class);
            for (BatchBean batchBean : batchBeans) {
                List<BatchFileBean> fileBeans = batchBean.getDocument_Objects();
                for (BatchFileBean batchFileBean : fileBeans) {
                    List<FileBean> files = batchFileBean.getFiles();
                    if (StringUtils.isEmpty(identificationCode)) {
                        if (files.size()>maxCount){
                            return map;
                        }
                        for (FileBean fileBean : files) {
                            String urlStr = fileBean.getUrl();
                            //String fileName = fileBean.getFileNO() + "_" + System.currentTimeMillis() + "." + fileBean.getFileFormat();
                            String fileName = fileBean.getFileNO() + "_" + UUID.randomUUID().toString() + "." + fileBean.getFileFormat();
                            log.debug("#######文件访问链接为[" + urlStr + "], 文件名为[" + fileName + "]#######");
                            // 调用下载文件方法
                            receiveFileByURL(urlStr, fileName, batchNumber);
                            map.put(fileBean.getFileNO(), fileName);
                        }
                    } else {
                        String[] split = identificationCode.split(",");
                        if (split.length>maxCount){
                            return map;
                        }
                        for (FileBean fileBean : files) {
                            String urlStr = fileBean.getUrl();
                            if (identificationCode.contains(fileBean.getFileNO())) {
                                //String fileName = fileBean.getFileNO() + "_" + System.currentTimeMillis() + "." + fileBean.getFileFormat();
                                String fileName = fileBean.getFileNO() + "_" + UUID.randomUUID().toString() + "." + fileBean.getFileFormat();
                                log.debug("#######文件访问链接为[" + urlStr + "], 文件名为[" + fileName + "]#######");
                                // 调用下载文件方法
                                receiveFileByURL(urlStr, fileName, batchNumber);
                                map.put(fileBean.getFileNO(), fileName);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将文件下载到DOWN_LOAD_FILE_PATH路径下
     *
     * @param urlStr
     * @param fileName
     * @param contentID 批次号
     */
    private void receiveFileByURL(String urlStr, String fileName, String contentID) {
        String path = imgUploadPath + "/IMAGE/" + DateUtils.datePath() + "/" + contentID + "/";
        File file = new File(path + fileName);
        File pareFile = file.getParentFile();
        if (pareFile == null || !pareFile.exists()) {
            log.info("no parefile ,begin to create mkdir,path=" + pareFile.getPath());
            pareFile.mkdirs();
        }
        URL url;
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            url = new URL(urlStr);
            in = url.openStream();
            fos = new FileOutputStream(file);
            if (in != null) {
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("unitedaccess http -- GetFileServer: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("unitedaccess http -- GetFileServer: " + e.toString());
            }
        }
    }

    @Override
    public ResultData videoPlatformDiscernReal(String batchNumber, String channelCode, String identificationCode, String imgType, String userName, String password, String modelCode, String createDate, String filePartName) {
        ResultData resultData = new ResultData();
        //查询渠道的全部识别权限
        ChannelType channelType2 = new ChannelType();
        channelType2.setChannelCode(channelCode);
        List<ChannelType> channelTypes = iChannelTypeService.selectChannelTypeList(channelType2);

        if (!imgType.equals("0")) {
            //ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
            if (!judgeAuth(channelTypes, imgType)) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
//            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
//                resultData.setMsg("无识别类型权限！");
//                resultData.setType("0");
//                return resultData;
//            }
        }

        //批量影像下载 返回唯一标识对应相应的imgUrl
        int maxCount = Integer.parseInt(configService.selectConfigByKey("sys.user.maxCount"));
        Map map = queryAndDownload(batchNumber, modelCode, userName, password, createDate, filePartName, identificationCode,maxCount);
        if (map.size()==0){
            resultData.setMsg("识别失败！影像数量超过"+maxCount+"个！");
            resultData.setType("0");
            return resultData;
        }
//        StringBuffer buffer = new StringBuffer();
        StringBuilder buffer = new StringBuilder();
        for (Object key : map.keySet()) {
            String relativePath = serverProfile + DateUtils.datePath() + "/" + batchNumber + "/" + map.get(key).toString();
            String data = "{\"image_type\":\"" + imgType + "\",\"path\":\"" + relativePath + "\",\"read_image_way\":\"3\"}";
            //存储影像信息
            iOcrImageService.insertOcrImageByFileNoAndFilePath(key.toString(),relativePath);
            buffer.append(data + ",");
        }

        String requestData = "{\"data_list\":[" + buffer.deleteCharAt(buffer.length() - 1).toString() + "]}";
        String request = HttpUtils.sendPost2(ocrUrl, requestData);
        log.info("**&&&request" + request);

        List<RequestModel2> model2s = JSONArray.parseArray(request, RequestModel2.class);

        //进行影像数据录入生成对应影像ID和相关url返回值
        List<ResultDataModel> resultDataModels = new ArrayList<>();
        int i =0;
        //批次内影像是不是都有识别权限：true : 有权限 false：无权限
        boolean authorityFlag = true;
        //记录没有的识别权限的字符串
        String authorityStr = "";
        for (RequestModel2 model2 : model2s) {
            //ResultDataModel dataModel = new ResultDataModel();
            //List list = new ArrayList();
            //根据图片路径获取影像信息
            OcrImage ocrImage = iOcrImageService.selectOcrImageByFilePath(model2.getPath());
            //dataModel.setBatchNumber(batchNumber);
            //dataModel.setIdentificationCode(ocrImage.getCompTradeId());

            if (StringUtils.isEmpty(request) || request.equals("[]") || model2.getImage_result().equals("[]") ) {
                OcrTrade ocrTrade = new OcrTrade();
                ocrTrade.setId(UUID.randomUUID().toString());
                ocrTrade.setChannel(channelCode);
                ocrTrade.setImageId(ocrImage.getId());
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
            }else {
                //ocrImage.setOcrResult(model2.getImage_result());
                //iOcrImageService.updateOcrImage(ocrImage);
                List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);

                for (RequestModel model : models) {
                    if (model.getClass_name().equals("IDCardFront") || model.getClass_name().equals("IDCardBack")) {
                        //ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "1");

                        //if (null == channelType1) {
                        if (!judgeAuth(channelTypes, "1")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无身份证识别类型权限") < 0) {
                                authorityStr += "无身份证识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("BankCard")) {
                        if (!judgeAuth(channelTypes, "2")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无银行卡识别类型权限") < 0) {
                                authorityStr += "无银行卡识别类型权限！";
                            }
                            break;
                        }
                        continue;

                    }
                    if (model.getClass_name().equals("Deposit")) {
                        if (!judgeAuth(channelTypes, "3")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无存单识别类型权限") < 0) {
                                authorityStr += "无存单识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("PremisesPermit")) {
                        if (!judgeAuth(channelTypes, "4")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无房本识别类型权限") < 0) {
                                authorityStr += "无房本识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("ResidenceBooklet")) {
                        if (!judgeAuth(channelTypes, "6")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无户口本识别类型权限") < 0) {
                                authorityStr += "无户口本识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("MarriageLicense")) {
                        if (!judgeAuth(channelTypes, "7")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无结婚证识别类型权限") < 0) {
                                authorityStr += "无结婚证识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("DrivingLicense")) {
                        if (!judgeAuth(channelTypes, "8")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无行驶证识别类型权限") < 0) {
                                authorityStr += "无行驶证识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("DriversLicense")) {
                        if (!judgeAuth(channelTypes, "9")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无驾驶证识别类型权限") < 0) {
                                authorityStr += "无驾驶证识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("PlateNumber")) {
                        if (!judgeAuth(channelTypes, "10")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无车牌号识别类型权限") < 0) {
                                authorityStr += "无车牌号识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("BusinessLicense")) {
                        if (!judgeAuth(channelTypes, "11")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无营业执照识别类型权限") < 0) {
                                authorityStr += "无营业执照识别类型权限！";
                            }
                        break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("VatInvoice")) {
                        if (!judgeAuth(channelTypes, "12")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无增值税发票识别类型权限") < 0) {
                                authorityStr += "无增值税发票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("Invoice")) {
                        if (!judgeAuth(channelTypes, "13")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无普通发票识别类型权限") < 0) {
                                authorityStr += "无普通发票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("Itinerary")) {
                        if (!judgeAuth(channelTypes, "14")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无航空运输电子客票行程单识别类型权限") < 0) {
                                authorityStr += "无航空运输电子客票行程单识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("RalTicket")) {
                        if (!judgeAuth(channelTypes, "15")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无火车票识别类型权限") < 0) {
                                authorityStr += "无火车票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("TollInvoice")) {
                        if (!judgeAuth(channelTypes, "16")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无通行费发票识别类型权限") < 0) {
                                authorityStr += "无通行费发票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("QuotaInvoice")) {
                        if (!judgeAuth(channelTypes, "17")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无通用定额发票识别类型权限") < 0) {
                                authorityStr += "无通用定额发票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("EleInvoice")) {
                        if (!judgeAuth(channelTypes, "18")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无电子发票识别类型权限") < 0) {
                                authorityStr += "无电子发票识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }

                    if (model.getClass_name().equals("GeneralText")) {
                        if (!judgeAuth(channelTypes, "10000")) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无通用文字识别类型权限") < 0) {
                                authorityStr += "无通用文字识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                }
            }
        }

        for (RequestModel2 model2 : model2s) {
            ResultDataModel dataModel = new ResultDataModel();
            List list = new ArrayList();
            //根据图片路径获取影像信息
            OcrImage ocrImage = iOcrImageService.selectOcrImageByFilePath(model2.getPath());
            dataModel.setBatchNumber(batchNumber);
            dataModel.setIdentificationCode(ocrImage.getCompTradeId());


            ocrImage.setOcrResult(model2.getImage_result());
            iOcrImageService.updateOcrImage(ocrImage);
            List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);

            //如果所有影像都有识别权限
            if (authorityFlag && !(StringUtils.isEmpty(request) || request.equals("[]"))) {
                for (RequestModel model : models) {
                    String tradeId;
                    Boolean flag = true;
                    switch (model.getClass_name()) {
                        case "IDCardFront":
                            IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                            idCardFront.setImgType(model.getClass_name());
                            idCardFront.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (idCardFront.hasEmptyField()) {
                                flag = false;
                                idCardFront.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertIDCardFrontFlag(idCardFront, channelCode, ocrImage.getId(), flag);
                            idCardFront.setTradeId(tradeId);
                            list.add(idCardFront);
                            i++;
                            break;
                        case "IDCardBack":
                            IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                            idCardBack.setImgType(model.getClass_name());
                            idCardBack.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (idCardBack.hasEmptyField()) {
                                flag = false;
                                idCardBack.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertIDCardBackFlag(idCardBack, channelCode, ocrImage.getId(), flag);
                            idCardBack.setTradeId(tradeId);
                            list.add(idCardBack);
                            i++;
                            break;
                        case "BankCard":
                            BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                            bankCard.setImgType(model.getClass_name());
                            bankCard.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (bankCard.hasEmptyField()) {
                                flag = false;
                                bankCard.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertBankCardFlag(bankCard, channelCode, ocrImage.getId(), flag);
                            bankCard.setTradeId(tradeId);
                            list.add(bankCard);
                            i++;
                            break;
                        case "Deposit":
                            DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                            deposit.setImgType(model.getClass_name());
                            deposit.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (deposit.hasEmptyField()) {
                                flag = false;
                                deposit.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertDepositFlag(deposit, channelCode, ocrImage.getId(), flag);
                            deposit.setTradeId(tradeId);
                            i++;
                            list.add(deposit);
                            break;
                        case "PremisesPermit":
                            PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
                            premisesPermit.setImgType(model.getClass_name());
                            premisesPermit.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (premisesPermit.hasEmptyField()) {
                                flag = false;
                                premisesPermit.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertPremisesPermitFlag(premisesPermit, channelCode, ocrImage.getId(), flag);
                            premisesPermit.setTradeId(tradeId);
                            list.add(premisesPermit);
                            i++;
                            break;

                        case "ResidenceBooklet":
                            ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
                            residenceBooklet.setImgType(model.getClass_name());
                            residenceBooklet.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (residenceBooklet.hasEmptyField()) {
                                flag = false;
                                residenceBooklet.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertResidenceBookletFlag(residenceBooklet, channelCode, ocrImage.getId(), flag);
                            residenceBooklet.setTradeId(tradeId);
                            list.add(residenceBooklet);
                            i++;
                            break;
                        case "MarriageLicense":
                            MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
                            marriageLicense.setImgType(model.getClass_name());
                            marriageLicense.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (marriageLicense.hasEmptyField()) {
                                flag = false;
                                marriageLicense.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertMarriageLicenseFlag(marriageLicense, channelCode, ocrImage.getId(), flag);
                            marriageLicense.setTradeId(tradeId);
                            list.add(marriageLicense);
                            i++;
                            break;
                        case "DrivingLicense":
                            DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
                            drivingLicense.setImgType(model.getClass_name());
                            drivingLicense.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (drivingLicense.hasEmptyField()) {
                                flag = false;
                                drivingLicense.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertDrivingLicenseFlag(drivingLicense, channelCode, ocrImage.getId(), flag);
                            drivingLicense.setTradeId(tradeId);
                            list.add(drivingLicense);
                            i++;
                            break;
                        case "DriversLicense":
                            DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
                            driversLicense.setImgType(model.getClass_name());
                            driversLicense.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (driversLicense.hasEmptyField()) {
                                flag = false;
                                driversLicense.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertDriversLicenseFlag(driversLicense, channelCode, ocrImage.getId(), flag);
                            driversLicense.setTradeId(tradeId);
                            list.add(driversLicense);
                            i++;
                            break;
                        case "PlateNumber":
                            PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
                            plateNumber.setImgType(model.getClass_name());
                            plateNumber.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (plateNumber.hasEmptyField()) {
                                flag = false;
                                plateNumber.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertPlateNumberFlag(plateNumber, channelCode, ocrImage.getId(), flag);
                            plateNumber.setTradeId(tradeId);
                            list.add(plateNumber);
                            i++;
                            break;
                        case "BusinessLicense":
                            BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
                            businessLicense.setImgType(model.getClass_name());
                            businessLicense.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (businessLicense.hasEmptyField()) {
                                flag = false;
                                businessLicense.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertBusinessLicenseFlag(businessLicense, channelCode, ocrImage.getId(), flag);
                            businessLicense.setTradeId(tradeId);
                            list.add(businessLicense);
                            i++;
                            break;
                        case "VatInvoice":
                            VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
                            vatInvoice.setImgType(model.getClass_name());
                            vatInvoice.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (vatInvoice.hasEmptyField()) {
                                flag = false;
                                vatInvoice.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertVatInvoiceFlag(vatInvoice, channelCode, ocrImage.getId(), flag);
                            vatInvoice.setTradeId(tradeId);
                            list.add(vatInvoice);
                            i++;
                            break;
                        case "Invoice":
                            Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
                            invoice.setImgType(model.getClass_name());
                            invoice.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (invoice.hasEmptyField()) {
                                flag = false;
                                invoice.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertInvoiceFlag(invoice, channelCode, ocrImage.getId(), flag);
                            invoice.setTradeId(tradeId);
                            list.add(invoice);
                            i++;
                            break;
                        case "Itinerary":
                            Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
                            itinerary.setImgType(model.getClass_name());
                            itinerary.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (itinerary.hasEmptyField()) {
                                flag = false;
                                itinerary.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertItineraryFlag(itinerary, channelCode, ocrImage.getId(), flag);
                            itinerary.setTradeId(tradeId);
                            list.add(itinerary);
                            i++;
                            break;
                        case "RalTicket":
                            RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
                            ralTicket.setImgType(model.getClass_name());
                            ralTicket.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (ralTicket.hasEmptyField()) {
                                flag = false;
                                ralTicket.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertRalTicketFlag(ralTicket, channelCode, ocrImage.getId(), flag);
                            ralTicket.setTradeId(tradeId);
                            list.add(ralTicket);
                            i++;
                            break;
                        case "TollInvoice":
                            TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
                            tollInvoice.setImgType(model.getClass_name());
                            tollInvoice.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (tollInvoice.hasEmptyField()) {
                                flag = false;
                                tollInvoice.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertTollInvoiceFlag(tollInvoice, channelCode, ocrImage.getId(), flag);
                            tollInvoice.setTradeId(tradeId);
                            list.add(tollInvoice);
                            i++;
                        case "QuotaInvoice":
                            QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
                            quotaInvoice.setImgType(model.getClass_name());
                            quotaInvoice.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (quotaInvoice.hasEmptyField()) {
                                flag = false;
                                quotaInvoice.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertQuotaInvoiceFlag(quotaInvoice, channelCode, ocrImage.getId(), flag);
                            quotaInvoice.setTradeId(tradeId);
                            list.add(quotaInvoice);
                            i++;
                            break;
                        case "EleInvoice":
                            EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
                            eleInvoice.setImgType(model.getClass_name());
                            eleInvoice.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (eleInvoice.hasEmptyField()) {
                                flag = false;
                                eleInvoice.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertEleInvoiceFlag(eleInvoice, channelCode, ocrImage.getId(), flag);
                            eleInvoice.setTradeId(tradeId);
                            list.add(eleInvoice);
                            i++;
                            break;
                        case "GeneralText":
                            GeneralText generalText = JSONArray.parseObject(model.getOcr_result(), GeneralText.class);
                            generalText.setImgType(model.getClass_name());
                            generalText.setFlag("true");
                            /**
                             * 调用流水存储 返回流水id
                             */
                            if (generalText.hasEmptyField()) {
                                flag = false;
                                generalText.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertGeneralTestFlag(generalText, channelCode, ocrImage.getId(), flag);
                            generalText.setTradeId(tradeId);
                            list.add(generalText);
                            i++;
                            break;
                        default:
                            tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, ocrImage.getId());
                            NoneEnty noneEnty = new NoneEnty();
                            noneEnty.setImgType("None");
                            noneEnty.setTradeId(tradeId);
                            break;
                    }
                }
            }
            dataModel.setResultData(list);
            resultDataModels.add(dataModel);

        }

        if (i==0) {
            resultData.setType("0");
            if (authorityFlag)
                resultData.setMsg("无识别结果！");
            else
                resultData.setMsg(authorityStr);
        } else {
            resultData.setType("1");
            resultData.setMsg("识别成功！");
            resultData.setData(resultDataModels);
        }
        return resultData;
    }


    /**
     * 单图片的对外统一接口（可以上传文件和视频,视频只是存入到本地没有图片提取）
     * channelCode：渠道号
     * imgUrl：图片路径
     * imgStr：图片转码转换成BASE64的文件
     * imgType：图片类型
     */
    @Override
    public ResultData runOneIncludeImageAndVideo(String channelCode, String imgUrl, String imgStr, String imgType)  {
        /**
         * 1.获取存储影像 录入数据库影像信息
         * 2.调用ocr接口识别
         * 3.处理ocr识别结果
         *  3.1新增流水数据，录入数据库
         *  3.2更新影像信息
         * 4.返回结果
         */

        //1.文件上传到指定的目录
        ResultData resultData = new ResultData();
        String fileName="";
        if (StringUtils.isNotEmpty(imgUrl)) {
            try {
                fileName=FileUploadUtils.downloadFile(imgUrl);
                if(fileName.equals("")){
                    resultData.setMsg("图片路径错误！");
                    resultData.setType("0");
                    return resultData;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(StringUtils.isNotEmpty(imgStr)){
            //抄的runoneAgain中的
            //StringBuffer sname=new StringBuffer();
            String sName = "";
            String dateStr = DateUtils.datePath() + "/" + fileName;
            String path = imgUploadPath + "/IMAGE/" + dateStr;
//            sname.append(IdUtils.fastUUID()).append(".").append(FileTypeUtils.getFromBASE64(imgStr));
//            String testname = sname.toString();
            sName = UUID.randomUUID().toString() + "." + FileTypeUtils.getFromBASE64(imgStr);
            ImageBase64.base64StringToImageSave(imgStr, path + sName);
            fileName = dateStr+sName;
        }

        //2.判断是否有识别权限类型
        ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
        if (!imgType.equals("0")) {
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }

        //3.调用OCR识别接口
        String relativePath = serverProfile + fileName;
        String data = "{\"data_list\":[{\"image_type\" :\"" + imgType + "\",\"path\":\"" + relativePath  + "\",\"read_image_way\":\"3\"}]}";
        String request = HttpUtils.sendPost2(ocrUrl, data);

        //4.把该条影像插入到影像表和流水表
        String imgId = UUID.randomUUID().toString();
        OcrImage image = new OcrImage();
        image.setId(imgId);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(relativePath);
        image.setParentId(imgId);

        //图片没有识别出来插入影像和流水
        if (StringUtils.isEmpty(request) || request.equals("[]")) {
            //A 插入影像
            iOcrImageService.insertOcrImage(image);//ocr_image
            //判断图片的类型
            String imgName = ChannelTypeConstants.getChannelType().get(imgType);
            /*
            switch (imgType) {
                case "1":
                    imgName = "IDCardFront";
                    break;
                case "2":
                    imgName = "BankCard";
                    break;
                case "3":
                    imgName = "Deposit";
                    break;
                case "4":
                    imgName = "PremisesPermit";
                    break;
                case "6":
                    imgName = "ResidenceBooklet";
                    break;
                case "7":
                    imgName = "MarriageLicense";
                    break;
                case "8":
                    imgName = "DrivingLicense";
                    break;
                case "9":
                    imgName = "DriversLicense";
                    break;
                case "10":
                    imgName = "PlateNumber";
                    break;
                case "11":
                    imgName = "BusinessLicense";
                    break;
                case "12":
                    imgName = "VatInvoice";
                    break;
                case "13":
                    imgName = "Invoice";
                    break;
                case "14":
                    imgName = "Itinerary";
                    break;
                case "15":
                    imgName = "RalTicket";
                    break;
                case "16":
                    imgName = "TollInvoice";
                    break;
                case "17":
                    imgName = "QuotaInvoice";
                    break;
                case "18":
                    imgName = "EleInvoice";
                    break;
                case "10000":
                    imgName = "text";
                    break;
            }
            */
            log.info("OCR识别结果为空");
            //B 插入流水
            OcrTrade ocrTrade = new OcrTrade();
            ocrTrade.setId(UUID.randomUUID().toString());
            ocrTrade.setChannel(channelCode);
            ocrTrade.setImageId(imgId);
            ocrTrade.setImageType(imgName);
            ocrTrade.setImageName(imgType);
            ocrTrade.setTickStatus("2");
            ocrTrade.setOcrStatus("1");
            ocrTrade.setPlatStatus("1");
            ocrTrade.setRemark2("0");
            ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            ocrTrade.setOcrTime(DateUtils.getTimeShort());
            iOcrTradeService.insertOcrTrade(ocrTrade);

            resultData.setMsg("OCR识别结果为空！");
            resultData.setType("0");
            return resultData;
        }
        //图片识别出来插入影像和流水
        String json = JSON.parseArray(request).toString();
        image.setOcrResult(json);//唯一多出来的
        iOcrImageService.insertOcrImage(image);//插入影像

        List<RequestModel2> model2s = JSONArray.parseArray(json, RequestModel2.class);
        RequestModel2 model2 = model2s.get(0);
        List<RequestModel> models = JSONArray.parseArray(model2.getImage_result(), RequestModel.class);

        List list = new ArrayList();
        for (RequestModel model : models) {
            if (null == model.getClass_name()) {
                resultData.setMsg("OCR识别结果为空！");
                resultData.setType("0");
                return resultData;
            }

            //判断是否有对应的权限start
            //身份证特殊处理
            if(model.getClass_name().equals(ChannelTypeConstants.getChannelType().get("IDCardFront")) || model.getClass_name().equals(ChannelTypeConstants.getChannelType().get("IDCardBack"))){
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "1");
                if (null == channelType1) {
                    resultData.setMsg("无身份证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            //其他
            if (model.getClass_name().equals(ChannelTypeConstants.getChannelType().get(model.getClass_name()))) {
                String num=ChannelTypeConstants.getChannelType2().get(model.getClass_name());
                String ChannelName=ChannelTypeConstants.getChannelType2().get(num);

                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, num);
                if (null == channelType1) {
                    resultData.setMsg("无"+ChannelName+"识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
            //判断是否有对应的权限end

            //插入到流水表中
            insertTrace(channelCode,imgId,list,model);
        }
        resultData.setData(list);
        /**
         * 流水ids
         */
        if (list.size() > 0) {
            resultData.setMsg("识别成功！");
            resultData.setType("1");
        } else {
            resultData.setMsg("识别失败！");
            resultData.setType("0");
        }
        return resultData;

    }


    private void insertTrace(String channelCode,String imgId,List list,RequestModel model){
        String tradeId;
        Boolean flag = true;
        if(model.getClass_name().equals("IDCardFront") || model.getClass_name().equals("IDCardFront_Video")){
            IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
            idCardFront.setImgType(model.getClass_name());
            idCardFront.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (idCardFront.hasEmptyField()) {
                flag = false;
                idCardFront.setFlag("false");
            }
            tradeId = iOcrTradeService.insertIDCardFrontFlag(idCardFront, channelCode, imgId, flag);
            idCardFront.setTradeId(tradeId);
            list.add(idCardFront);
        }else if(model.getClass_name().equals("IDCardBack") || model.getClass_name().equals("IDCardBack_Video")){
            IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
            idCardBack.setImgType(model.getClass_name());
            idCardBack.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (idCardBack.hasEmptyField()) {
                flag = false;
                idCardBack.setFlag("false");
            }
            tradeId = iOcrTradeService.insertIDCardBackFlag(idCardBack, channelCode, imgId, flag);
            idCardBack.setTradeId(tradeId);
            list.add(idCardBack);
        }else if(model.getClass_name().equals("BankCard")){
            BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
            bankCard.setImgType(model.getClass_name());
            bankCard.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(bankCard.getBankCardNo())) {
                flag = false;
                bankCard.setFlag("false");
            }
            tradeId = iOcrTradeService.insertBankCardFlag(bankCard, channelCode, imgId, flag);
            bankCard.setTradeId(tradeId);
            list.add(bankCard);
        }else if(model.getClass_name().equals("Deposit")){
            DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
            deposit.setImgType(model.getClass_name());
            deposit.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(deposit.getDepositNo()) || StringUtils.isEmpty(deposit.getAccNo()) || StringUtils.isEmpty(deposit.getAmt()) || StringUtils.isEmpty(deposit.getAmtCapital()) || StringUtils.isEmpty(deposit.getName())) {
                flag = false;
                deposit.setFlag("false");
            }
            tradeId = iOcrTradeService.insertDepositFlag(deposit, channelCode, imgId, flag);
            deposit.setTradeId(tradeId);
            list.add(deposit);
        }else if(model.getClass_name().equals("PremisesPermit") || model.getClass_name().equals("PremisesPermit_Video")){
            PremisesPermit premisesPermit = JSONArray.parseObject(model.getOcr_result(), PremisesPermit.class);
            premisesPermit.setImgType(model.getClass_name());
            premisesPermit.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(premisesPermit.getBuiltArea())||StringUtils.isEmpty(premisesPermit.getCertificateNo())||StringUtils.isEmpty(premisesPermit.getFloorArea())||StringUtils.isEmpty(premisesPermit.getName())||StringUtils.isEmpty(premisesPermit.getLocation())||StringUtils.isEmpty(premisesPermit.getPurpose())||StringUtils.isEmpty(premisesPermit.getLandUse())||StringUtils.isEmpty(premisesPermit.getStructure())) {
                flag = false;
                premisesPermit.setFlag("false");
            }
            tradeId = iOcrTradeService.insertPremisesPermitFlag(premisesPermit, channelCode, imgId, flag);
            premisesPermit.setTradeId(tradeId);
            list.add(premisesPermit);
        }else if(model.getClass_name().equals("ResidenceBooklet")){
            ResidenceBooklet residenceBooklet = JSONArray.parseObject(model.getOcr_result(), ResidenceBooklet.class);
            residenceBooklet.setImgType(model.getClass_name());
            residenceBooklet.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(residenceBooklet.getAddress())||StringUtils.isEmpty(residenceBooklet.getNativePlace())) {
                flag = false;
                residenceBooklet.setFlag("false");
            }
            tradeId = iOcrTradeService.insertResidenceBookletFlag(residenceBooklet, channelCode, imgId, flag);
            residenceBooklet.setTradeId(tradeId);
            list.add(residenceBooklet);
        }else if(model.getClass_name().equals("MarriageLicense")){
            MarriageLicense marriageLicense = JSONArray.parseObject(model.getOcr_result(), MarriageLicense.class);
            marriageLicense.setImgType(model.getClass_name());
            marriageLicense.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(marriageLicense.getMarriageNo())||StringUtils.isEmpty(marriageLicense.getIdCardNo())||StringUtils.isEmpty(marriageLicense.getName())) {
                flag = false;
                marriageLicense.setFlag("false");
            }
            tradeId = iOcrTradeService.insertMarriageLicenseFlag(marriageLicense, channelCode, imgId, flag);
            marriageLicense.setTradeId(tradeId);
            list.add(marriageLicense);
        }else if(model.getClass_name().equals("DrivingLicense")){
            DrivingLicense drivingLicense = JSONArray.parseObject(model.getOcr_result(), DrivingLicense.class);
            drivingLicense.setImgType(model.getClass_name());
            drivingLicense.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(drivingLicense.getFileNumber())||StringUtils.isEmpty(drivingLicense.getAddress())||StringUtils.isEmpty(drivingLicense.getBrandModel())||StringUtils.isEmpty(drivingLicense.getEngineNumber())||StringUtils.isEmpty(drivingLicense.getGabarite())||StringUtils.isEmpty(drivingLicense.getIdentifyCode())||StringUtils.isEmpty(drivingLicense.getInspectionRecord())||StringUtils.isEmpty(drivingLicense.getIssueDate())||StringUtils.isEmpty(drivingLicense.getIssueUnit())||StringUtils.isEmpty(drivingLicense.getNumber())||StringUtils.isEmpty(drivingLicense.getOwner())||StringUtils.isEmpty(drivingLicense.getPlateNumber())||StringUtils.isEmpty(drivingLicense.getRatifiedMass())||StringUtils.isEmpty(drivingLicense.getRegistrationDate())||StringUtils.isEmpty(drivingLicense.getRemark())||StringUtils.isEmpty(drivingLicense.getTotalMass())||StringUtils.isEmpty(drivingLicense.getTractionMass())||StringUtils.isEmpty(drivingLicense.getUnladenMass())||StringUtils.isEmpty(drivingLicense.getUseNature())||StringUtils.isEmpty(drivingLicense.getVehicleType())) {
                flag = false;
                drivingLicense.setFlag("false");
            }
            tradeId = iOcrTradeService.insertDrivingLicenseFlag(drivingLicense, channelCode, imgId, flag);
            drivingLicense.setTradeId(tradeId);
            list.add(drivingLicense);
        }else if(model.getClass_name().equals("DriversLicense")){
            DriversLicense driversLicense = JSONArray.parseObject(model.getOcr_result(), DriversLicense.class);
            driversLicense.setImgType(model.getClass_name());
            driversLicense.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(driversLicense.getFileNumber())||StringUtils.isEmpty(driversLicense.getAddress())||StringUtils.isEmpty(driversLicense.getBirthDate())||StringUtils.isEmpty(driversLicense.getCardNo())||StringUtils.isEmpty(driversLicense.getDrivingType())||StringUtils.isEmpty(driversLicense.getEndDate())||StringUtils.isEmpty(driversLicense.getIssueDate())||StringUtils.isEmpty(driversLicense.getName())||StringUtils.isEmpty(driversLicense.getNationality())||StringUtils.isEmpty(driversLicense.getRecord())||StringUtils.isEmpty(driversLicense.getSex())||StringUtils.isEmpty(driversLicense.getStartDate())) {
                flag = false;
                driversLicense.setFlag("false");
            }
            tradeId = iOcrTradeService.insertDriversLicenseFlag(driversLicense, channelCode, imgId, flag);
            driversLicense.setTradeId(tradeId);
            list.add(driversLicense);
        }else if(model.getClass_name().equals("PlateNumber")){
            PlateNumber plateNumber = JSONArray.parseObject(model.getOcr_result(), PlateNumber.class);
            plateNumber.setImgType(model.getClass_name());
            plateNumber.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(plateNumber.getNumber())) {
                flag = false;
                plateNumber.setFlag("false");
            }
            tradeId = iOcrTradeService.insertPlateNumberFlag(plateNumber, channelCode, imgId, flag);
            plateNumber.setTradeId(tradeId);
            list.add(plateNumber);
        }else if(model.getClass_name().equals("BusinessLicense") || model.getClass_name().equals("BusinessLicense_Video")){
            BusinessLicense businessLicense = JSONArray.parseObject(model.getOcr_result(), BusinessLicense.class);
            businessLicense.setImgType(model.getClass_name());
            businessLicense.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(businessLicense.getSocialCode())||StringUtils.isEmpty(businessLicense.getAddress())||StringUtils.isEmpty(businessLicense.getBusinessScope())||StringUtils.isEmpty(businessLicense.getBusinessTerm())||StringUtils.isEmpty(businessLicense.getCompanyName())||StringUtils.isEmpty(businessLicense.getLegalPerson())||StringUtils.isEmpty(businessLicense.getRegisterDate())||StringUtils.isEmpty(businessLicense.getRegisteredCapital())||StringUtils.isEmpty(businessLicense.getVertical())
            ) {
                flag = false;
                businessLicense.setFlag("false");
            }
            tradeId = iOcrTradeService.insertBusinessLicenseFlag(businessLicense, channelCode, imgId, flag);
            businessLicense.setTradeId(tradeId);
            list.add(businessLicense);
        }else if(model.getClass_name().equals("VatInvoice")){
            VatInvoice vatInvoice = JSONArray.parseObject(model.getOcr_result(), VatInvoice.class);
            vatInvoice.setImgType(model.getClass_name());
            vatInvoice.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(vatInvoice.getCode())||StringUtils.isEmpty(vatInvoice.getIssuedDate())||StringUtils.isEmpty(vatInvoice.getNumber())||StringUtils.isEmpty(vatInvoice.getPurchaserName())||StringUtils.isEmpty(vatInvoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(vatInvoice.getSellerName())||StringUtils.isEmpty(vatInvoice.getSellerTaxpayerNum())||StringUtils.isEmpty(vatInvoice.getValueAddedTax())||StringUtils.isEmpty(vatInvoice.getSubjects())) {
                flag = false;
                vatInvoice.setFlag("false");
            }
            tradeId = iOcrTradeService.insertVatInvoiceFlag(vatInvoice, channelCode, imgId, flag);
            vatInvoice.setTradeId(tradeId);
            list.add(vatInvoice);
        }else if(model.getClass_name().equals("Invoice")){
            Invoice invoice = JSONArray.parseObject(model.getOcr_result(), Invoice.class);
            invoice.setImgType(model.getClass_name());
            invoice.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(invoice.getCode())||StringUtils.isEmpty(invoice.getIssuedDate())||StringUtils.isEmpty(invoice.getNumber())||StringUtils.isEmpty(invoice.getPurchaserName())||StringUtils.isEmpty(invoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(invoice.getSellerName())||StringUtils.isEmpty(invoice.getSellerTaxpayerNum())||StringUtils.isEmpty(invoice.getValueAddedTax())||StringUtils.isEmpty(invoice.getSubjects())) {
                flag = false;
                invoice.setFlag("false");
            }
            tradeId = iOcrTradeService.insertInvoiceFlag(invoice, channelCode, imgId, flag);
            invoice.setTradeId(tradeId);
            list.add(invoice);
        }else if(model.getClass_name().equals("Itinerary")){
            Itinerary itinerary = JSONArray.parseObject(model.getOcr_result(), Itinerary.class);
            itinerary.setImgType(model.getClass_name());
            itinerary.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(itinerary.getAmt())||StringUtils.isEmpty(itinerary.getDate())||StringUtils.isEmpty(itinerary.getStartingStation())||StringUtils.isEmpty(itinerary.getFlight())||StringUtils.isEmpty(itinerary.getName())||StringUtils.isEmpty(itinerary.getSeatLevel())||StringUtils.isEmpty(itinerary.getEndingStation())) {
                flag = false;
                itinerary.setFlag("false");
            }
            tradeId = iOcrTradeService.insertItineraryFlag(itinerary, channelCode, imgId, flag);
            itinerary.setTradeId(tradeId);
            list.add(itinerary);
        }else if(model.getClass_name().equals("RalTicket")){
            RalTicket ralTicket = JSONArray.parseObject(model.getOcr_result(), RalTicket.class);
            ralTicket.setImgType(model.getClass_name());
            ralTicket.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(ralTicket.getAmt())||StringUtils.isEmpty(ralTicket.getDate())||StringUtils.isEmpty(ralTicket.getStartingStation())||StringUtils.isEmpty(ralTicket.getName())||StringUtils.isEmpty(ralTicket.getSeatLevel())||StringUtils.isEmpty(ralTicket.getTrainNumber())||StringUtils.isEmpty(ralTicket.getEndingStation())) {
                flag = false;
                ralTicket.setFlag("false");
            }
            tradeId = iOcrTradeService.insertRalTicketFlag(ralTicket, channelCode, imgId, flag);
            ralTicket.setTradeId(tradeId);
            list.add(ralTicket);
        }else if(model.getClass_name().equals("TollInvoice")){
            TollInvoice tollInvoice = JSONArray.parseObject(model.getOcr_result(), TollInvoice.class);
            tollInvoice.setImgType(model.getClass_name());
            tollInvoice.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(tollInvoice.getAmt())||StringUtils.isEmpty(tollInvoice.getInvoiceCode())||StringUtils.isEmpty(tollInvoice.getInvoiceNumber())) {
                flag = false;
                tollInvoice.setFlag("false");
            }
            tradeId = iOcrTradeService.insertTollInvoiceFlag(tollInvoice, channelCode, imgId, flag);
            tollInvoice.setTradeId(tradeId);
            list.add(tollInvoice);
        }else if(model.getClass_name().equals("QuotaInvoice")){
            QuotaInvoice quotaInvoice = JSONArray.parseObject(model.getOcr_result(), QuotaInvoice.class);
            quotaInvoice.setImgType(model.getClass_name());
            quotaInvoice.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(quotaInvoice.getAmt())||StringUtils.isEmpty(quotaInvoice.getInvoiceCode())||StringUtils.isEmpty(quotaInvoice.getInvoiceNumber())) {
                flag = false;
                quotaInvoice.setFlag("false");
            }
            tradeId = iOcrTradeService.insertQuotaInvoiceFlag(quotaInvoice, channelCode, imgId, flag);
            quotaInvoice.setTradeId(tradeId);
            list.add(quotaInvoice);
        }else if(model.getClass_name().equals("EleInvoice")){
            EleInvoice eleInvoice = JSONArray.parseObject(model.getOcr_result(), EleInvoice.class);
            eleInvoice.setImgType(model.getClass_name());
            eleInvoice.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (StringUtils.isEmpty(eleInvoice.getCode())||StringUtils.isEmpty(eleInvoice.getIssuedDate())||StringUtils.isEmpty(eleInvoice.getNumber())||StringUtils.isEmpty(eleInvoice.getPurchaserName())||StringUtils.isEmpty(eleInvoice.getPurchaserTaxpayerNum())||StringUtils.isEmpty(eleInvoice.getSellerName())||StringUtils.isEmpty(eleInvoice.getSellerTaxpayerNum())||StringUtils.isEmpty(eleInvoice.getValueAddedTax())||StringUtils.isEmpty(eleInvoice.getSubjects())) {
                flag = false;
                eleInvoice.setFlag("false");
            }
            tradeId = iOcrTradeService.insertEleInvoiceFlag(eleInvoice, channelCode, imgId, flag);
            eleInvoice.setTradeId(tradeId);
            list.add(eleInvoice);
        }else if(model.getClass_name().equals("GeneralText")){
            GeneralText generalText = JSONArray.parseObject(model.getOcr_result(), GeneralText.class);
            generalText.setImgType(model.getClass_name());
            generalText.setFlag("true");
            /**
             * 调用流水存储 返回流水id
             */
            if (generalText.hasEmptyField()) {
                flag = false;
                generalText.setFlag("false");
            }
            tradeId = iOcrTradeService.insertGeneralTestFlag(generalText, channelCode, imgId, flag);
            generalText.setTradeId(tradeId);
            list.add(generalText);
        }else{
            tradeId = iOcrTradeService.insertNoneTrade(model.getOcr_result(), channelCode, imgId);
            NoneEnty noneEnty = new NoneEnty();
            noneEnty.setImgType("None");
            noneEnty.setTradeId(tradeId);
            list.add(noneEnty);
        }

    }


    /**
     * @return true 有权限 false 无权限
     * @param channelTypes 渠道权限集合
     * @param imgType 需要判断的权限
     * */
    private boolean judgeAuth (List<ChannelType> channelTypes, String imgType) {
        for (ChannelType channelType : channelTypes) {
            if (channelType != null && channelType.getOcrType() != null) {
                if (channelType.getOcrType().equals(imgType)) {
                    return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }
}
