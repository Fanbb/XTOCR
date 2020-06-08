package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.FileTypeUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.ImageBase64;
import com.ocr.common.utils.http.HttpUtils;
import com.ocr.common.utils.security.Md5Utils;
import com.ocr.system.domain.ChannelType;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.*;
import com.ocr.system.service.IChannelTypeService;
import com.ocr.system.service.IOcrImageService;
import com.ocr.system.service.IOcrTradeService;
import com.ocr.system.service.OCRDiscernService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
        String fileName = System.currentTimeMillis() + "";
        String dateStr = DateUtils.datePath();
        String path = imgUploadPath + "/IMAGE/" + dateStr + "/" + fileName;
        if (StringUtils.isNotEmpty(url)) {
            /**
             * 图片url不为空
             */
            str = ImageBase64.imageToBase64ByUrl(url);
            ImageBase64.base64StringToImageSave(str, path);

        } else if (StringUtils.isNotEmpty(str)) {
            /**
             * 图片base64不为空
             */
            ImageBase64.base64StringToImageSave(str, path);

        }
        OcrImage image = new OcrImage();
        image.setId(fileName);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(path);
        image.setType("");
        image.setParentId(fileName);
        iOcrImageService.insertOcrImage(image);

        String idCardJson1 = "[{'class_name':'IDCardFront','ocr_result':{'idCardNo':'412823155648775659','birthday':'1994年3月23日','sex':'男','nation':'汉','name':'张三','address':'河南省郑州市'}},{'class_name':'IDCardFront','ocr_result':{'idCardNo':'412823155648775659','birthday':'1994年3月13日','sex':'男','nation':'汉','name':'张三','address':'河南省郑州市'}}]";
        List<RequestModel> models = JSONArray.parseArray(idCardJson1, RequestModel.class);

        StringBuffer tradeIds = new StringBuffer();

        List list = new ArrayList();
        for (RequestModel model : models) {
            switch (model.getClass_name()) {
                case "IDCardFront":
                    IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                    idCardFront.setImgType(model.getClass_name());
                    list.add(idCardFront);

                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeIds.append(iOcrTradeService.insertIDCardFront(idCardFront, channelCode, fileName) + ",");
                    break;

                case "IDCardBack":
                    IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                    idCardBack.setImgType(model.getClass_name());
                    list.add(idCardBack);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeIds.append(iOcrTradeService.insertIDCardBack(idCardBack, channelCode, fileName) + ",");
                    break;
                case "BankCard":
                    BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                    bankCard.setImgType(model.getClass_name());
                    list.add(bankCard);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeIds.append(iOcrTradeService.insertBankCard(bankCard, channelCode, fileName) + ",");
                    break;
                case "Deposit":
                    DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                    deposit.setImgType(model.getClass_name());
                    list.add(deposit);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeIds.append(iOcrTradeService.insertDeposit(deposit, channelCode, fileName) + ",");
                    break;
            }
        }

        /**
         * 流水ids
         */
        if (tradeIds.length() > 5) {
            resultData.setData(list);
            resultData.setMsg("识别成功！");
            resultData.setType("1");
        } else {
            resultData.setData(list);
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
        String fileName = System.currentTimeMillis() + "";
        String sName="";
        String dateStr = DateUtils.datePath() + "/" + fileName;

        String path= imgUploadPath + "/IMAGE/" + dateStr;
        if (StringUtils.isNotEmpty(url)) {
            /**
             * 图片url不为空
             */
            sName = url.substring(url.lastIndexOf("."));
            str = ImageBase64.imageToBase64ByUrl(url);
            ImageBase64.base64StringToImageSave(str, path+sName);
        } else if (StringUtils.isNotEmpty(str)) {
            /**
             * 图片base64不为空
             */
            sName = "."+FileTypeUtils.getFromBASE64(str);
            ImageBase64.base64StringToImageSave(str, path+sName);
        }
        ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
        if (!imgType.equals("0")) {
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }
        String relativePath = serverProfile+ dateStr;
        String data = "{\"image_type\" :\"" + imgType + "\",\"path\":\"" + relativePath + sName + "\",\"read_image_way\":\"3\"}";
        if(imgType.equals("0")){
            data = "{\"path\":\"" + relativePath + sName + "\",\"read_image_way\":\"3\"}";
        }

        String request = HttpUtils.sendPost2(ocrUrl, data);
        String json = JSON.parseArray(request).toString();

        OcrImage image = new OcrImage();
        image.setId(fileName);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(relativePath+sName);
        image.setParentId(fileName);
        image.setOcrResult(json);
        iOcrImageService.insertOcrImage(image);

        List<RequestModel> models = JSONArray.parseArray(json, RequestModel.class);
        if (models.size() == 0) {
            log.info("OCR识别结果为空");
            OcrTrade ocrTrade = new OcrTrade();
            String id = System.currentTimeMillis() + "";
            ocrTrade.setId(id);
            ocrTrade.setChannel(channelCode);
            ocrTrade.setImageId(fileName);
            ocrTrade.setImageType(imgType);
            ocrTrade.setOcrStatus("1");
            ocrTrade.setOcrPoint("1");
            ocrTrade.setRemark2("0");
            ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
            ocrTrade.setOcrTime(DateUtils.getTimeShort());
            iOcrTradeService.insertOcrTrade(ocrTrade);

            resultData.setMsg("OCR识别结果为空！");
            resultData.setType("0");
            return resultData;
        }
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

            switch (model.getClass_name()) {
                case "IDCardFront":
                    IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
                    idCardFront.setImgType(model.getClass_name());
                    list.add(idCardFront);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    iOcrTradeService.insertIDCardFront(idCardFront, channelCode, fileName);
                    break;
                case "IDCardBack":
                    IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                    if (StringUtils.isNotEmpty(idCardBack.getStartDate())) {
                        idCardBack.setImgType(model.getClass_name());
                        list.add(idCardBack);
                        /**
                         * 调用流水存储 返回流水id
                         */
                        iOcrTradeService.insertIDCardBack(idCardBack, channelCode, fileName);
                    }
                    break;
                case "BankCard":
                    BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                    if (StringUtils.isNotEmpty(bankCard.getBankCardNo())) {
                        bankCard.setImgType(model.getClass_name());
                        list.add(bankCard);
                        /**
                         * 调用流水存储 返回流水id
                         */
                        iOcrTradeService.insertBankCard(bankCard, channelCode, fileName);
                    }
                    break;
                case "Deposit":
                    DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                    if (StringUtils.isNotEmpty(deposit.getDepositNo())) {
                        deposit.setImgType(model.getClass_name());
                        list.add(deposit);
                        /**
                         * 调用流水存储 返回流水id
                         */
                        iOcrTradeService.insertDeposit(deposit, channelCode, fileName);
                    }
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
}
