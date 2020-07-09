package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.FileTypeUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.ImageBase64;
import com.ocr.common.utils.http.HttpUtils;
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
import java.util.UUID;


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
    public ResultData videoPlatformDiscern(String batchNumber, String channelCode, String identificationCode, String imgType) {
        ResultData resultData = new ResultData();
        String[] split = identificationCode.split(",");
        List<Object> objects = new ArrayList<>();
        if (split.length > 1) {
            for (String identification : split) {
                DiscernResultData discernResult = new DiscernResultData();
                discernResult.setBatchNumber(batchNumber);
                discernResult.setIdentificationCode(identification);
                switch (imgType) {
                    case "1":
                        List list = new ArrayList();
                        list.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        list.add(new IDCardBack("ee349106-96a4-4837-9a65-f6a97e3e6515", "山东省威海市公安局", "2006.09.16", "2006.36.16", "IDCardBack"));
                        discernResult.setResultData(list);
                        objects.add(discernResult);
                        break;
                    case "2":
                        List list2 = new ArrayList();
                        list2.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                        discernResult.setResultData(list2);
                        objects.add(discernResult);
                        break;
                    case "3":
                        List list3 = new ArrayList();
                        list3.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                        discernResult.setResultData(list3);
                        objects.add(discernResult);
                        break;
                    case "4":
                        List list4 = new ArrayList();
                        list4.add(new PremisesPermit("ee349106-96a4-4837-9a65-f6a97e3e6585", "李四","817810101101281795", "城市住宅/商住", "威海市统一路29号","2000平方米", "136平方米","国有土地使用权","三室一厅", "PremisesPermit"));
                        discernResult.setResultData(list4);
                        objects.add(discernResult);
                        break;
                    default:
                        //通用识别
                        List list5 = new ArrayList();
                        list5.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        list5.add(new IDCardBack("ee349106-96a4-4837-9a65-f6a97e3e6515", "山东省威海市公安局", "2006.09.16", "2006.36.16", "IDCardBack"));
                        list5.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                        list5.add(new PremisesPermit("ee349106-96a4-4837-9a65-f6a97e3e6585", "李四","817810101101281795", "城市住宅/商住", "威海市统一路29号","2000平方米", "136平方米","国有土地使用权","三室一厅", "PremisesPermit"));
                        list5.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                        discernResult.setResultData(list5);
                        objects.add(discernResult);
                        break;
                }
            }
        }else {
                DiscernResultData discernResult = new DiscernResultData();
                discernResult.setBatchNumber(batchNumber);
                discernResult.setIdentificationCode(split[0]);
                switch (imgType) {
                    case "1":
                        List list = new ArrayList();
                        list.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        list.add(new IDCardBack("ee349106-96a4-4837-9a65-f6a97e3e6515", "山东省威海市公安局", "2006.09.16", "2006.36.16", "IDCardBack"));
                        discernResult.setResultData(list);
                        objects.add(discernResult);
                        break;
                    case "2":
                        List list2 = new ArrayList();
                        list2.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                        discernResult.setResultData(list2);
                        objects.add(discernResult);
                        break;
                    case "3":
                        List list3 = new ArrayList();
                        list3.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                        discernResult.setResultData(list3);
                        objects.add(discernResult);
                        break;
                    case "4":
                        List list4 = new ArrayList();
                        list4.add(new PremisesPermit("ee349106-96a4-4837-9a65-f6a97e3e6585", "李四","817810101101281795", "城市住宅/商住", "威海市统一路29号","2000平方米", "136平方米","国有土地使用权","三室一厅", "PremisesPermit"));
                        discernResult.setResultData(list4);
                        objects.add(discernResult);
                        break;
                    default:
                        //通用识别
                        List list5 = new ArrayList();
                        list5.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        list5.add(new PremisesPermit("ee349106-96a4-4837-9a65-f6a97e3e6585", "李四","817810101101281795", "城市住宅/商住", "威海市统一路29号","2000平方米", "136平方米","国有土地使用权","三室一厅", "PremisesPermit"));
                        list5.add(new IDCardBack("ee349106-96a4-4837-9a65-f6a97e3e6515", "山东省威海市公安局", "2006.09.16", "2006.36.16", "IDCardBack"));
                        list5.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                        list5.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                        discernResult.setResultData(list5);
                        objects.add(discernResult);
                        break;
                }
        }
        if (batchNumber.equals("500")){
            resultData.setType("0");
            resultData.setMsg("错误！");
        }else {
            resultData.setType("1");
            resultData.setMsg("识别成功！");
            resultData.setData(objects);
        }

        return resultData;
    }

    @Override
    public ResultData videoPlatformDiscernReal(String batchNumber, String channelCode, String identificationCode, String imgType) {
        //批量影像下载 返回唯一标识对应相应的imgUrl

        List<FileNamesAndPoint> fileNamesAndPoints = new ArrayList<>();
        //进行影像数据录入生成对应影像ID和相关url返回值


        ResultData resultData = new ResultData();
        String[] split = identificationCode.split(",");
        List<Object> objects = new ArrayList<>();
        if (split.length > 1) {
            for (String identification : split) {
                DiscernResultData discernResult = new DiscernResultData();
                discernResult.setBatchNumber(batchNumber);
                discernResult.setIdentificationCode(identification);
                switch (imgType) {
                    case "1":
                        List list = new ArrayList();
                        list.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        discernResult.setResultData(list);
                        objects.add(discernResult);
                        break;
                    case "2":
                        List list2 = new ArrayList();
                        list2.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                        discernResult.setResultData(list2);
                        objects.add(discernResult);
                        break;
                    case "3":
                        List list3 = new ArrayList();
                        list3.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                        discernResult.setResultData(list3);
                        objects.add(discernResult);
                        break;
                    default:
                        //通用识别
                        List list4 = new ArrayList();
                        list4.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                        discernResult.setResultData(list4);
                        objects.add(discernResult);
                        break;
                }
            }
        }else {
            DiscernResultData discernResult = new DiscernResultData();
            discernResult.setBatchNumber(batchNumber);
            discernResult.setIdentificationCode(split[0]);
            switch (imgType) {
                case "1":
                    List list = new ArrayList();
                    list.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                    discernResult.setResultData(list);
                    objects.add(discernResult);
                    break;
                case "2":
                    List list2 = new ArrayList();
                    list2.add(new BankCard("ee349106-96a4-4837-9a65-f6a97e3e6535", "123468956785449",  "BankCard"));
                    discernResult.setResultData(list2);
                    objects.add(discernResult);
                    break;
                case "3":
                    List list3 = new ArrayList();
                    list3.add(new DepositReceipt("ee349106-96a4-4837-9a65-f6a97e3e6585", "妹妹","817810101101281795", "CNY300.00", "叁佰圆整","000000000", "Deposit"));
                    discernResult.setResultData(list3);
                    objects.add(discernResult);
                    break;
                default:
                    //通用识别
                    List list4 = new ArrayList();
                    list4.add(new IDCardFront("ee349106-96a4-4837-9a65-f6a97e3e6525", "张三", "男", "汉族", "新威支行", "412823199605061615", "1996年5月6日", "IDCardFront"));
                    discernResult.setResultData(list4);
                    objects.add(discernResult);
                    break;
            }
        }
        if (batchNumber.equals("500")){
            resultData.setType("0");
            resultData.setMsg("错误！");
        }else {
            resultData.setType("1");
            resultData.setMsg("识别成功！");
            resultData.setData(objects);
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
}
