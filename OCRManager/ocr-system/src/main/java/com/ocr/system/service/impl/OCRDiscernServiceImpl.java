package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.ImageBase64;
import com.ocr.common.utils.http.HttpUtils;
import com.ocr.system.domain.ChannelType;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.model.*;
import com.ocr.system.service.IChannelTypeService;
import com.ocr.system.service.IOcrImageService;
import com.ocr.system.service.IOcrTradeService;
import com.ocr.system.service.OCRDiscernService;
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
        String path = imgUploadPath + "/IMAGE/" + dateStr + "/" + fileName + ".jpg";
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
            image.setCompTradeId(tradeIds.subSequence(0, tradeIds.length() - 1).toString());
            /**
             * 更新影像数据
             */
            iOcrImageService.updateOcrImage(image);
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
        String dateStr = DateUtils.datePath() + "/" + fileName + ".jpg";
        String path = imgUploadPath + "/IMAGE/" + dateStr;
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
        ChannelType channelType = iChannelTypeService.selectByNoAndType(channelCode, imgType);
        if (!imgType.equals("0")){
            if (null == channelType || !imgType.equals(channelType.getOcrType())) {
                resultData.setMsg("无识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }


        OcrImage image = new OcrImage();
        image.setId(fileName);
        image.setOcrDate(new Date());
        image.setOcrTime(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        image.setLocalPath(path);
        image.setParentId(fileName);
        iOcrImageService.insertOcrImage(image);
        String data = "{\"image_type\" :\""+imgType+"\",\"path\":\""+serverProfile+dateStr+"\",\"read_image_way\":\"3\"}";
        String request = HttpUtils.sendPost2(ocrUrl, data);
        String idCardJson1=request.substring(1,request.length()-1);

//        if (imgType.equals("0")) {
//            //调用通用识别模板接口
//            request= HttpUtils.sendPost2(ocrUrl+imgType,null);
//        } else {
//            //调用类型模板接口
//            if (imgType.equals("1")) {
//                idCardJson1 = "{'class_name':'IDCardFront','ocr_result':{'idCardNo':'412823155648775659','birthday':'1994年3月23日','sex':'男','nation':'汉','name':'张三','address':'河南省郑州市'}}";
//            } else if (imgType.equals("2")) {
//                idCardJson1 = "{'class_name':'BankCard','ocr_result':{'bankCardNo':'5233695648755694122'}}";
//            } else if (imgType.equals("3")) {
//                idCardJson1 = "{'class_name':'Deposit','ocr_result':{'name':'妹妹','accNo':'55694122','amt':'8000','amtCapital':'捌仟元整','depositNo':'52336956487'}}";
//            }
//        }


        RequestModel model = JSONArray.parseObject(idCardJson1, RequestModel.class);
        if (model.getClass_name().equals("IDCardFront") || model.getClass_name().equals("IDCardFront")) {
            if (!imgType.equals("1")&&!imgType.equals("0")) {
                resultData.setMsg("无身份证识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }
        if (model.getClass_name().equals("BankCard")) {
            if (!imgType.equals("2")&&!imgType.equals("0")){
                resultData.setMsg("无银行卡识别类型权限！");
                resultData.setType("0");
                return resultData;
            }

        }
        if (model.getClass_name().equals("Deposit")) {
            if (!imgType.equals("3")&&!imgType.equals("0")){
                resultData.setMsg("无存单识别类型权限！");
                resultData.setType("0");
                return resultData;
            }
        }


        String tradeId = "";

        switch (model.getClass_name()) {
            case "IDCardFront":
                IDCardFront idCardFront = JSONArray.parseObject(model.getOcr_result(), IDCardFront.class);
//                if (StringUtils.isNotEmpty(idCardFront.getIdCardNo())) {
                    idCardFront.setImgType(model.getClass_name());
                    resultData.setData(idCardFront);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertIDCardFront(idCardFront, channelCode, fileName);
//                }
                break;

            case "IDCardBack":
                IDCardBack idCardBack = JSONArray.parseObject(model.getOcr_result(), IDCardBack.class);
                if (StringUtils.isNotEmpty(idCardBack.getStartDate())) {
                    idCardBack.setImgType(model.getClass_name());
                    resultData.setData(idCardBack);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertIDCardBack(idCardBack, channelCode, fileName);
                }
                break;
            case "BankCard":
                BankCard bankCard = JSONArray.parseObject(model.getOcr_result(), BankCard.class);
                if (StringUtils.isNotEmpty(bankCard.getBankCardNo())) {
                    bankCard.setImgType(model.getClass_name());
                    resultData.setData(bankCard);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertBankCard(bankCard, channelCode, fileName);
                }
                break;
            case "Deposit":
                DepositReceipt deposit = JSONArray.parseObject(model.getOcr_result(), DepositReceipt.class);
                if (StringUtils.isNotEmpty(deposit.getAccNo())) {
                    deposit.setImgType(model.getClass_name());
                    resultData.setData(deposit);
                    /**
                     * 调用流水存储 返回流水id
                     */
                    tradeId = iOcrTradeService.insertDeposit(deposit, channelCode, fileName);
                }
                break;
        }

        /**
         * 流水ids
         */
        if (tradeId.length() > 3) {
            /**
             * 更新影像数据
             */
            image.setCompTradeId(tradeId);
            iOcrImageService.updateOcrImage(image);
            resultData.setMsg("识别成功！");
            resultData.setType("1");
        } else {
            resultData.setMsg("识别失败！");
            resultData.setType("0");

        }
        return resultData;
    }
}
