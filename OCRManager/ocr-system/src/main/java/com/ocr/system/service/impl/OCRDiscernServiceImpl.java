package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.FileTypeUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.ImageBase64;
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
            if (model.getClass_name().equals("IDCardFront") || model.getClass_name().equals("IDCardBack")) {
                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "1");

                if (null == channelType1) {
                    resultData.setMsg("无身份证识别类型权限！");
                    resultData.setType("0");
                    return resultData;
                }
            }
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

        if (!imgType.equals("0")) {
            ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
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

            if (StringUtils.isEmpty(request) || request.equals("[]")) {
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
                        ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "1");

                        if (null == channelType1) {
                            authorityFlag = false;

                            if (authorityStr.indexOf("无身份证识别类型权限") < 0) {
                                authorityStr += "无身份证识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("BankCard")) {
                        ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "2");

                        if (null == channelType1) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无银行卡识别类型权限") < 0) {
                                authorityStr += "无银行卡识别类型权限！";
                            }
                            break;
                        }
                        continue;

                    }
                    if (model.getClass_name().equals("Deposit")) {
                        ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "3");

                        if (null == channelType1) {
                            authorityFlag = false;
                            if (authorityStr.indexOf("无存单识别类型权限") < 0) {
                                authorityStr += "无存单识别类型权限！";
                            }
                            break;
                        }
                        continue;
                    }
                    if (model.getClass_name().equals("PremisesPermit")) {
                        ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "4");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无房本识别类型权限") < 0) {
                                        authorityStr += "无房本识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("ResidenceBooklet")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "6");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无户口本识别类型权限") < 0) {
                                        authorityStr += "无户口本识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("MarriageLicense")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "7");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无结婚证识别类型权限") < 0) {
                                        authorityStr += "无结婚证识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("DrivingLicense")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "8");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无行驶证识别类型权限") < 0) {
                                        authorityStr += "无行驶证识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("DriversLicense")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "9");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无驾驶证识别类型权限") < 0) {
                                        authorityStr += "无驾驶证识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("PlateNumber")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "10");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无车牌号识别类型权限") < 0) {
                                        authorityStr += "无车牌号识别类型权限！";
                                    }
                                    break;
                                }
                                continue;
                            }
                            if (model.getClass_name().equals("BusinessLicense")) {
                                ChannelType channelType1 = iChannelTypeService.selectByNoAndType(channelCode, "11");

                                if (null == channelType1) {
                                    authorityFlag = false;
                                    if (authorityStr.indexOf("无营业执照识别类型权限") < 0) {
                                        authorityStr += "无营业执照识别类型权限！";
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
                            if (StringUtils.isEmpty(idCardFront.getSex()) || StringUtils.isEmpty(idCardFront.getNation()) || StringUtils.isEmpty(idCardFront.getName()) || StringUtils.isEmpty(idCardFront.getBirthday()) || StringUtils.isEmpty(idCardFront.getAddress()) || StringUtils.isEmpty(idCardFront.getIdCardNo())) {
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
                            if (StringUtils.isEmpty(idCardBack.getStartDate()) || StringUtils.isEmpty(idCardBack.getEndDate()) || StringUtils.isEmpty(idCardBack.getAuthority())) {
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
                            if (StringUtils.isEmpty(bankCard.getBankCardNo())) {
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
                            if (StringUtils.isEmpty(deposit.getDepositNo()) || StringUtils.isEmpty(deposit.getAccNo()) || StringUtils.isEmpty(deposit.getAmt()) || StringUtils.isEmpty(deposit.getAmtCapital()) || StringUtils.isEmpty(deposit.getName())) {
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
                            if (StringUtils.isEmpty(premisesPermit.getBuiltArea())||StringUtils.isEmpty(premisesPermit.getCertificateNo())||StringUtils.isEmpty(premisesPermit.getFloorArea())||StringUtils.isEmpty(premisesPermit.getName())||StringUtils.isEmpty(premisesPermit.getLocation())||StringUtils.isEmpty(premisesPermit.getPurpose())||StringUtils.isEmpty(premisesPermit.getLandUse())||StringUtils.isEmpty(premisesPermit.getStructure())) {
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
                            if (StringUtils.isEmpty(residenceBooklet.getAddress())||StringUtils.isEmpty(residenceBooklet.getNativePlace())) {
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
                            if (StringUtils.isEmpty(marriageLicense.getMarriageNo())||StringUtils.isEmpty(marriageLicense.getIdCardNo())||StringUtils.isEmpty(marriageLicense.getName())) {
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
                            if (StringUtils.isEmpty(drivingLicense.getFileNumber())||StringUtils.isEmpty(drivingLicense.getAddress())||StringUtils.isEmpty(drivingLicense.getBrandModel())||StringUtils.isEmpty(drivingLicense.getEngineNumber())||StringUtils.isEmpty(drivingLicense.getGabarite())||StringUtils.isEmpty(drivingLicense.getIdentifyCode())||StringUtils.isEmpty(drivingLicense.getInspectionRecord())||StringUtils.isEmpty(drivingLicense.getIssueDate())||StringUtils.isEmpty(drivingLicense.getIssueUnit())||StringUtils.isEmpty(drivingLicense.getNumber())||StringUtils.isEmpty(drivingLicense.getOwner())||StringUtils.isEmpty(drivingLicense.getPlateNumber())||StringUtils.isEmpty(drivingLicense.getRatifiedMass())||StringUtils.isEmpty(drivingLicense.getRegistrationDate())||StringUtils.isEmpty(drivingLicense.getRemark())||StringUtils.isEmpty(drivingLicense.getTotalMass())||StringUtils.isEmpty(drivingLicense.getTractionMass())||StringUtils.isEmpty(drivingLicense.getUnladenMass())||StringUtils.isEmpty(drivingLicense.getUseNature())||StringUtils.isEmpty(drivingLicense.getVehicleType())) {
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
                            if (StringUtils.isEmpty(driversLicense.getFileNumber())||StringUtils.isEmpty(driversLicense.getAddress())||StringUtils.isEmpty(driversLicense.getBirthDate())||StringUtils.isEmpty(driversLicense.getCardNo())||StringUtils.isEmpty(driversLicense.getDrivingType())||StringUtils.isEmpty(driversLicense.getEndDate())||StringUtils.isEmpty(driversLicense.getIssueDate())||StringUtils.isEmpty(driversLicense.getName())||StringUtils.isEmpty(driversLicense.getNationality())||StringUtils.isEmpty(driversLicense.getRecord())||StringUtils.isEmpty(driversLicense.getSex())||StringUtils.isEmpty(driversLicense.getStartDate())) {
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
                            if (StringUtils.isEmpty(plateNumber.getNumber())) {
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
                            if (StringUtils.isEmpty(businessLicense.getSocialCode())||StringUtils.isEmpty(businessLicense.getAddress())||StringUtils.isEmpty(businessLicense.getBusinessScope())||StringUtils.isEmpty(businessLicense.getBusinessTerm())||StringUtils.isEmpty(businessLicense.getCompanyName())||StringUtils.isEmpty(businessLicense.getLegalPerson())||StringUtils.isEmpty(businessLicense.getRegisterDate())||StringUtils.isEmpty(businessLicense.getRegisteredCapital())||StringUtils.isEmpty(businessLicense.getVertical())
                            ) {
                                flag = false;
                                businessLicense.setFlag("false");
                            }
                            tradeId = iOcrTradeService.insertBusinessLicenseFlag(businessLicense, channelCode, ocrImage.getId(), flag);
                            businessLicense.setTradeId(tradeId);
                            list.add(businessLicense);
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
}
