package com.ocr.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ocr.common.constant.ChannelTypeConstants;
import com.ocr.common.core.text.Convert;
import com.ocr.common.utils.DateUtils;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.mapper.OcrTradeMapper;
import com.ocr.system.model.*;
import com.ocr.system.service.IOcrTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 识别流水 服务层实现
 *
 * @author ocr
 * @date 2020-05-20
 */
@Service
public class OcrTradeServiceImpl implements IOcrTradeService {
    @Autowired
    private OcrTradeMapper ocrTradeMapper;

    /**
     * 查询识别流水信息
     *
     * @param id 识别流水ID
     * @return 识别流水信息
     */
    @Override
    public OcrTrade selectOcrTradeById(String id) {
        return ocrTradeMapper.selectOcrTradeById(id);
    }

    /**
     * 查询识别流水列表
     *
     * @param ocrTrade 识别流水信息
     * @return 识别流水集合
     */
    @Override
    public List<OcrTrade> selectOcrTradeList(OcrTrade ocrTrade) {
        return ocrTradeMapper.selectOcrTradeList(ocrTrade);
    }

    /**
     * 新增识别流水
     *
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
    @Override
    public int insertOcrTrade(OcrTrade ocrTrade) {
        return ocrTradeMapper.insertOcrTrade(ocrTrade);
    }

    /**
     * 修改识别流水
     *
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
    @Override
    public int updateOcrTrade(OcrTrade ocrTrade) {
        return ocrTradeMapper.updateOcrTrade(ocrTrade);
    }

    /**
     * 删除识别流水对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOcrTradeByIds(String ids) {
        return ocrTradeMapper.deleteOcrTradeByIds(Convert.toStrArray(ids));
    }

    /**
     * 身份证正面流水
     * @param idCardFront
     * @param channelCode 渠道
     * @param imgId       影像id
     * @return
     */
    @Override
    public String insertIDCardFront(IDCardFront idCardFront, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, idCardFront.getImgType(), idCardFront.getIdCardNo(), JSON.toJSONString(idCardFront),riskFlag);
    }

    /**
     * 身份证反面流水
     * @param idCardBack
     * @param channelCode
     * @param imgId       影像id
     * @return
     */
    @Override
    public String insertIDCardBack(IDCardBack idCardBack, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, idCardBack.getImgType(), null, JSON.toJSONString(idCardBack), riskFlag);
    }

    /**
     * 银行卡流水
     * @param bankCard
     * @param channelCode 渠道
     * @param imgId       影像id
     * @return
     */
    @Override
    public String insertBankCard(BankCard bankCard, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, bankCard.getImgType(), bankCard.getBankCardNo(), JSON.toJSONString(bankCard), riskFlag);
    }

    /**
     * 存单流水
     * @param depositReceipt
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertDeposit(DepositReceipt depositReceipt, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, depositReceipt.getImgType(), depositReceipt.getDepositNo(), JSON.toJSONString(depositReceipt), riskFlag);
    }

    @Override
    public String insertNoneTrade(String result, String channelCode, String imgId) {
        String tradeId = UUID.randomUUID().toString();
        OcrTrade ocrTrade = new OcrTrade();
        ocrTrade.setId(tradeId);
        ocrTrade.setChannel(channelCode);
        ocrTrade.setImageId(imgId);
        ocrTrade.setImageType("None");
        ocrTrade.setImageName("0");
        ocrTrade.setOcrStatus("1");
        ocrTrade.setTickStatus("2");
        ocrTrade.setPlatStatus("1");
        ocrTrade.setRemark2("0");
        ocrTrade.setRemark1(result);
        ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        ocrTrade.setOcrTime(DateUtils.getTimeShort());
        ocrTradeMapper.insertOcrTrade(ocrTrade);
        return tradeId;
    }

    @Override
    public List<OcrTrade> selectOcrTradeListByIds(String tradeIds) {
        return ocrTradeMapper.selectOcrTradeListByIds(Convert.toStrArray(tradeIds));
    }

    @Override
    public String insertIDCardFrontFlag(IDCardFront idCardFront, String channelCode, String imgId, Boolean flag , String riskFlag) {
        return getStringFlag(channelCode, imgId, idCardFront.getImgType(), idCardFront.getIdCardNo(), JSON.toJSONString(idCardFront), flag,riskFlag);
    }

    private String getStringFlag(String channelCode, String imgId, String imgType, String ocrSeq, String toJSONString, Boolean flag, String riskFlag) {
        OcrTrade ocrTrade = new OcrTrade();
        String id =UUID.randomUUID().toString();
        ocrTrade.setId(id);
        ocrTrade.setChannel(channelCode);
        ocrTrade.setOcrSeq(ocrSeq);
        ocrTrade.setImageId(imgId);
        ocrTrade.setImageType(imgType);
        ocrTrade.setOcrOrgan(riskFlag);

        //修改start
        String numType =ChannelTypeConstants.getChannelType2().get(imgType);
        if(imgType.equals("IDCardBack") || imgType.equals("IDCardFront")){
            ocrTrade.setImageName("1");
        }else{
            ocrTrade.setImageName(numType);
        }
        if (flag){
            ocrTrade.setOcrStatus("0");
            ocrTrade.setTickStatus("0");
            ocrTrade.setPlatStatus("0");
        }else {
            ocrTrade.setOcrStatus("1");
            ocrTrade.setTickStatus("0");
            ocrTrade.setPlatStatus("1");
        }
        ocrTrade.setRemark2("0");
        ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        ocrTrade.setOcrTime(DateUtils.getTimeShort());
        ocrTrade.setRemark1(toJSONString);
        if (ocrTradeMapper.insertOcrTrade(ocrTrade) > 0) {
            return id;
        } else {
            return "";
        }
    }

    @Override
    public String insertIDCardBackFlag(IDCardBack idCardBack, String channelCode, String imgId, Boolean flag ,String riskFlag) {
        return getStringFlag(channelCode, imgId, idCardBack.getImgType(), null, JSON.toJSONString(idCardBack),flag,riskFlag);

    }

    @Override
    public String insertBankCardFlag(BankCard bankCard, String channelCode, String imgId, Boolean flag,String riskFlag) {
        return getStringFlag(channelCode, imgId, bankCard.getImgType(), bankCard.getBankCardNo(), JSON.toJSONString(bankCard),flag,riskFlag);

    }

    @Override
    public String insertDepositFlag(DepositReceipt deposit, String channelCode, String imgId, Boolean flag,String riskFlag) {
        return getStringFlag(channelCode, imgId, deposit.getImgType(), deposit.getDepositNo(), JSON.toJSONString(deposit),flag,riskFlag);
    }

    @Override
    public String insertPremisesPermitFlag(PremisesPermit premisesPermit, String channelCode, String imgId, Boolean flag,String riskFlag) {
        return getStringFlag(channelCode, imgId, premisesPermit.getImgType(), premisesPermit.getCertificateNo(), JSON.toJSONString(premisesPermit),flag,riskFlag);
    }

    @Override
    public String insertResidenceBookletFlag(ResidenceBooklet residenceBooklet, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, residenceBooklet.getImgType(), "", JSON.toJSONString(residenceBooklet),flag,riskFlag);
    }

    @Override
    public String insertMarriageLicenseFlag(MarriageLicense marriageLicense, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, marriageLicense.getImgType(), marriageLicense.getMarriageNo(), JSON.toJSONString(marriageLicense),flag,riskFlag);
    }

    @Override
    public String insertDrivingLicenseFlag(DrivingLicense drivingLicense, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, drivingLicense.getImgType(), drivingLicense.getFileNumber(), JSON.toJSONString(drivingLicense),flag,riskFlag);
    }

    @Override
    public String insertDriversLicenseFlag(DriversLicense driversLicense, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, driversLicense.getImgType(), driversLicense.getFileNumber(), JSON.toJSONString(driversLicense),flag, riskFlag);
    }

    @Override
    public String insertPlateNumberFlag(PlateNumber plateNumber, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, plateNumber.getImgType(), plateNumber.getNumber(), JSON.toJSONString(plateNumber),flag, riskFlag);
    }

    @Override
    public String insertBusinessLicenseFlag(BusinessLicense businessLicense, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, businessLicense.getImgType(), businessLicense.getSocialCode(), JSON.toJSONString(businessLicense),flag, riskFlag);
    }

    @Override
    public String insertInvoiceFlag(Invoice invoice, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, invoice.getImgType(), invoice.getNumber(), JSON.toJSONString(invoice),flag, riskFlag);
    }

    @Override
    public String insertItineraryFlag(Itinerary itinerary, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, itinerary.getImgType(), itinerary.getName(), JSON.toJSONString(itinerary),flag, riskFlag);
    }

    @Override
    public String insertQuotaInvoiceFlag(QuotaInvoice quotaInvoice, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, quotaInvoice.getImgType(), quotaInvoice.getInvoiceNumber(), JSON.toJSONString(quotaInvoice),flag, riskFlag);
    }

    @Override
    public String insertRalTicketFlag(RalTicket ralTicket, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, ralTicket.getImgType(), ralTicket.getName(), JSON.toJSONString(ralTicket),flag, riskFlag);
    }

    @Override
    public String insertTollInvoiceFlag(TollInvoice tollInvoice, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, tollInvoice.getImgType(), tollInvoice.getInvoiceNumber(), JSON.toJSONString(tollInvoice),flag, riskFlag);
    }

    @Override
    public String insertVatInvoiceFlag(VatInvoice vatInvoice, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, vatInvoice.getImgType(), vatInvoice.getNumber(), JSON.toJSONString(vatInvoice),flag, riskFlag);
    }
    @Override
    public String insertEleInvoiceFlag(EleInvoice eleInvoice, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, eleInvoice.getImgType(), eleInvoice.getNumber(), JSON.toJSONString(eleInvoice),flag, riskFlag);
    }

    @Override
    public String insertGeneralTestFlag(GeneralText generalText, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, generalText.getImgType(), "", JSON.toJSONString(generalText),flag, riskFlag);
    }
    @Override
    public String insertCunZheFlag(CunZhe cunZhe, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, cunZhe.getImgType(),cunZhe.getPassbookNo(), JSON.toJSONString(cunZhe),flag, riskFlag);
    }
    @Override
    public String insertTongXingFlag(TongXing tongXing, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, tongXing.getImgType(), tongXing.getInvoiceNumber(), JSON.toJSONString(tongXing),flag, riskFlag);
    }
    @Override
    public String insertGouFangHeTongFlag(GouFangHeTong gouFangHeTong, String channelCode, String imgId, Boolean flag,String riskFlag){
        return getStringFlag(channelCode, imgId, gouFangHeTong.getImgType(), "", JSON.toJSONString(gouFangHeTong),flag, riskFlag);
    }
    /**
     * 房本流水
     * @param premisesPermit
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertPremisesPermit(PremisesPermit premisesPermit, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, premisesPermit.getImgType(), premisesPermit.getCertificateNo(), JSON.toJSONString(premisesPermit), riskFlag);
    }

    /**
     * 营业执照流水
     * @param businessLicense
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertBusinessLicense(BusinessLicense businessLicense, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, businessLicense.getImgType(), businessLicense.getSocialCode(), JSON.toJSONString(businessLicense), riskFlag);
    }

    /**
     * 车牌号流水
     * @param plateNumber
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertPlateNumber(PlateNumber plateNumber, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, plateNumber.getImgType(), plateNumber.getNumber(), JSON.toJSONString(plateNumber), riskFlag);
    }

    /**
     * 驾驶证流水
     * @param driversLicense
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertDriversLicense(DriversLicense driversLicense, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, driversLicense.getImgType(), driversLicense.getFileNumber(), JSON.toJSONString(driversLicense), riskFlag);
    }

    /**
     * 行驶证流水
     * @param drivingLicense
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertDrivingLicense(DrivingLicense drivingLicense, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, drivingLicense.getImgType(), drivingLicense.getFileNumber(), JSON.toJSONString(drivingLicense), riskFlag);
    }

    /**
     * 结婚证流水
     * @param marriageLicense
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertMarriageLicense(MarriageLicense marriageLicense, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, marriageLicense.getImgType(), marriageLicense.getMarriageNo(), JSON.toJSONString(marriageLicense), riskFlag);
    }

    /**
     * 户口本流水
     * @param residenceBooklet
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertResidenceBooklet(ResidenceBooklet residenceBooklet, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, residenceBooklet.getImgType(), "", JSON.toJSONString(residenceBooklet), riskFlag);
    }

    /**
     * 增值税发票流水
     * @param vatInvoice
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertVatInvoice(VatInvoice vatInvoice, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, vatInvoice.getImgType(), vatInvoice.getNumber(), JSON.toJSONString(vatInvoice), riskFlag);
    }

    /**
     * 普通发票流水
     * @param invoice
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertInvoice(Invoice invoice, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, invoice.getImgType(), invoice.getNumber(), JSON.toJSONString(invoice), riskFlag);
    }

    /**
     * 航空运输电子客票行程单流水
     * @param itinerary
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertItinerary(Itinerary itinerary, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, itinerary.getImgType(), itinerary.getName(), JSON.toJSONString(itinerary), riskFlag);
    }

    /**
     * 火车票流水
     * @param ralTicket
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertRalTicket(RalTicket ralTicket, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, ralTicket.getImgType(), ralTicket.getName(), JSON.toJSONString(ralTicket), riskFlag);
    }
    /**
     * 通行费发票流水
     * @param tollInvoice
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertTollInvoice(TollInvoice tollInvoice, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, tollInvoice.getImgType(), tollInvoice.getInvoiceNumber(), JSON.toJSONString(tollInvoice), riskFlag);
    }

    /**
     * 通用定额发票流水
     * @param quotaInvoice
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertQuotaInvoice(QuotaInvoice quotaInvoice, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, quotaInvoice.getImgType(), quotaInvoice.getInvoiceNumber(), JSON.toJSONString(quotaInvoice), riskFlag);
    }
    /**
     * 电子发票流水
     * @param eleInvoice
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertEleInvoice(EleInvoice eleInvoice, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, eleInvoice.getImgType(), eleInvoice.getNumber(), JSON.toJSONString(eleInvoice), riskFlag);
    }

    /**
     * 通用文本流水
     * @param generalText
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertGeneralText(GeneralText generalText, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, generalText.getImgType(), "", JSON.toJSONString(generalText), riskFlag);
    }
    /**
     * 存折流水
     * @param cunZhe
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertCunZhe(CunZhe cunZhe, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, cunZhe.getImgType(),cunZhe.getPassbookNo(), JSON.toJSONString(cunZhe), riskFlag);
    }
    /**
     * 通行费流水
     * @param tongXing
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertTongXing(TongXing tongXing, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, tongXing.getImgType(), tongXing.getInvoiceNumber(), JSON.toJSONString(tongXing), riskFlag);
    }
    /**
     * 购房合同流水
     * @param gouFangHeTong
     * @param channelCode
     * @param imgId
     * @return
     */
    @Override
    public String insertGouFangHeTong(GouFangHeTong gouFangHeTong, String channelCode, String imgId,String riskFlag) {
        return getString(channelCode, imgId, gouFangHeTong.getImgType(), "", JSON.toJSONString(gouFangHeTong), riskFlag);
    }

    @Override
    public String[] selectImagePath(String[] tradeIds) {
        return ocrTradeMapper.selectImagePath(tradeIds);
    }

    @Override
    public String selectCountByTrickStatus(String tickStatus) {
        return ocrTradeMapper.selectCountByTrickStatus(tickStatus);
    }

    @Override
    public List<String> selectNameToTypeTotal() {
        List<String> list = ocrTradeMapper.selectNameToTypeTotal();
        List<String> values = new ArrayList<>();
        for (String value:list) {
            if(value != null){
                switch (value){
                    case "0":
                        values.add("无效类型");
                        break;
                    case "1":
                        values.add("身份证");
                        break;
                    case "2":
                        values.add("银行卡");
                        break;
                    case "3":
                        values.add("存单");
                        break;
                    case "4":
                        values.add("房本");
                        break;
                    case "5":
                        values.add("存折");
                        break;
                    case "6":
                        values.add("户口本");
                        break;
                    case "7":
                        values.add("结婚证");
                        break;
                    case "8":
                        values.add("行驶证");
                        break;
                    case "9":
                        values.add("驾驶证");
                        break;
                    case "10":
                        values.add("车牌号");
                        break;
                    case "11":
                        values.add("营业执照");
                        break;
                    case "12":
                        values.add("增值税发票");
                        break;
                    case "13":
                        values.add("普通发票");
                        break;
                    case "14":
                        values.add("航空运输电子客票行程单");
                        break;
                    case "15":
                        values.add("火车票");
                        break;
                    case "16":
                        values.add("通行费发票");
                        break;
                    case "17":
                        values.add("通用定额发票");
                        break;
                    case "18":
                        values.add("电子发票");
                        break;
                    case "19":
                        values.add("存折");
                        break;
                    case "20":
                        values.add("通行费");
                        break;
                    case "21":
                        values.add("购房合同");
                        break;
                    case "10000":
                        values.add("通用文本");
                        break;
                }
            }
        }
        return values;
    }

    @Override
    public List<String> selectValueToTypeTotal() {
        return ocrTradeMapper.selectValueToTypeTotal();
    }

    @Override
    public List<String> selectNameToDataTotal() {
        return ocrTradeMapper.selectNameToDataTotal();
    }

    @Override
    public List<String> selectValueToDataTotal() {
        return ocrTradeMapper.selectValueToDataTotal();
    }

    @Override
    public void updateRemark2ByIds(String[] ids) {
        ocrTradeMapper.updateRemark2ByIds(ids);
    }

    private String getString(String channelCode, String imgId, String imgType, String ocrSeq, String json,String riskFlag) {
        OcrTrade ocrTrade = new OcrTrade();
        String id =UUID.randomUUID().toString();
        ocrTrade.setId(id);
        ocrTrade.setChannel(channelCode);
        ocrTrade.setOcrSeq(ocrSeq);
        ocrTrade.setImageId(imgId);
        ocrTrade.setImageType(imgType);
        ocrTrade.setImageName(ChannelTypeConstants.getChannelType2().get(imgType));
        ocrTrade.setOcrOrgan(riskFlag);
        ocrTrade.setOcrStatus("0");
        ocrTrade.setTickStatus("0");
        ocrTrade.setPlatStatus("0");
        ocrTrade.setRemark2("0");
        ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        ocrTrade.setOcrTime(DateUtils.getTimeShort());
        ocrTrade.setRemark1(json);
        if (ocrTradeMapper.insertOcrTrade(ocrTrade) > 0) {
            return id;
        } else {
            return "";
        }
    }

}
