package com.ocr.system.service;

import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.*;

import java.util.List;

/**
 * 识别流水 服务层
 * 
 * @author ocr
 * @date 2020-05-20
 */
public interface IOcrTradeService 
{
	/**
     * 查询识别流水信息
     * 
     * @param id 识别流水ID
     * @return 识别流水信息
     */
	public OcrTrade selectOcrTradeById(String id);
	
	/**
     * 查询识别流水列表
     * 
     * @param ocrTrade 识别流水信息
     * @return 识别流水集合
     */
	public List<OcrTrade> selectOcrTradeList(OcrTrade ocrTrade);
	
	/**
     * 新增识别流水
     * 
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
	public int insertOcrTrade(OcrTrade ocrTrade);
	
	/**
     * 修改识别流水
     * 
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
	public int updateOcrTrade(OcrTrade ocrTrade);
		
	/**
     * 删除识别流水信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOcrTradeByIds(String ids);

	/**
	 * 身份证正面流水
	 * @param idCardFront
	 * @param channelCode 渠道
	 * @param imgId 影像id
	 * @return
	 */
	public String insertIDCardFront(IDCardFront idCardFront,String channelCode, String imgId);

	/**
	 * 身份证反面流水
	 * @param idCardBack
	 * @param channelCode
	 * @param imgId 影像id
	 * @return
	 */
	public String insertIDCardBack(IDCardBack idCardBack,String channelCode, String imgId);

	/**
	 * 银行卡流水
	 * @param bankCard
	 * @param channelCode 渠道
	 * @param imgId 影像id
	 * @return
	 */
	public String insertBankCard(BankCard bankCard,String channelCode, String imgId);

	/**
	 * 存单流水
	 * @param depositReceipt
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	public String insertDeposit(DepositReceipt depositReceipt,String channelCode, String imgId);

	/**
	 * 房本流水
	 * @param premisesPermit
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertPremisesPermit(PremisesPermit premisesPermit, String channelCode, String imgId);

	/**
	 * 营业执照流水
	 * @param businessLicense
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertBusinessLicense(BusinessLicense businessLicense, String channelCode, String imgId);

	/**
	 * 车牌号流水
	 * @param plateNumber
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertPlateNumber(PlateNumber plateNumber, String channelCode, String imgId);

	/**
	 * 驾驶证流水
	 * @param driversLicense
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertDriversLicense(DriversLicense driversLicense, String channelCode, String imgId);

	/**
	 * 行驶证流水
	 * @param drivingLicense
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertDrivingLicense(DrivingLicense drivingLicense, String channelCode, String imgId);

	/**
	 * 结婚证流水
	 * @param marriageLicense
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertMarriageLicense(MarriageLicense marriageLicense, String channelCode, String imgId);

	/**
	 * 户口本流水
	 * @param residenceBooklet
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertResidenceBooklet(ResidenceBooklet residenceBooklet, String channelCode, String imgId);
	/**
	 * 增值税发票流水
	 * @param vatInvoice
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertVatInvoice(VatInvoice vatInvoice, String channelCode, String imgId);
	/**
	 * 普通发票流水
	 * @param invoice
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertInvoice(Invoice invoice, String channelCode, String imgId);
	/**
	 * 航空运输电子客票行程单流水
	 * @param itinerary
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertItinerary(Itinerary itinerary, String channelCode, String imgId);
	/**
	 * 火车票流水
	 * @param ralTicket
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertRalTicket(RalTicket ralTicket, String channelCode, String imgId);
	/**
	 * 通行费发票流水
	 * @param tollInvoice
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertTollInvoice(TollInvoice tollInvoice, String channelCode, String imgId);
	/**
	 * 通用定额发票流水
	 * @param quotaInvoice
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertQuotaInvoice(QuotaInvoice quotaInvoice, String channelCode, String imgId);
    /**
     * 电子发票流水
     * @param eleInvoice
     * @param channelCode
     * @param imgId
     * @return
     */
    String insertEleInvoice(EleInvoice eleInvoice, String channelCode, String imgId);

	/**
	 * 通用文本流水
	 * @param eleInvoice
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	String insertGeneralText(GeneralText eleInvoice, String channelCode, String imgId);

	/**
	 * 存入无效类型
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	public String insertNoneTrade(String result,String channelCode, String imgId);

	/**
	 * 通过流水id获取影像路径
	 * @param tradeIds
	 * @return
	 */
	public String[] selectImagePath(String[] tradeIds);

	public String selectCountByTrickStatus(String tickStatus);

	List<String> selectNameToTypeTotal();

	List<String> selectValueToTypeTotal();

	List<String> selectNameToDataTotal();

	List<String> selectValueToDataTotal();

    void updateRemark2ByIds(String[] ids);

    List<OcrTrade> selectOcrTradeListByIds(String tradeIds);

	String insertIDCardFrontFlag(IDCardFront idCardFront, String channelCode, String imgId, Boolean flag);

	String insertIDCardBackFlag(IDCardBack idCardBack, String channelCode, String imgId, Boolean flag);

	String insertBankCardFlag(BankCard bankCard, String channelCode, String imgId, Boolean flag);

	String insertDepositFlag(DepositReceipt deposit, String channelCode, String imgId, Boolean flag);

    String insertPremisesPermitFlag(PremisesPermit premisesPermit, String channelCode, String imgId, Boolean flag);

	String insertResidenceBookletFlag(ResidenceBooklet residenceBooklet, String channelCode, String imgId, Boolean flag);

	String insertMarriageLicenseFlag(MarriageLicense marriageLicense, String channelCode, String imgId, Boolean flag);

	String insertDriversLicenseFlag(DriversLicense driversLicense, String channelCode, String imgId, Boolean flag);

	String insertDrivingLicenseFlag(DrivingLicense drivingLicense, String channelCode, String imgId, Boolean flag);

	String insertPlateNumberFlag(PlateNumber plateNumber, String channelCode, String imgId, Boolean flag);

	String insertBusinessLicenseFlag(BusinessLicense businessLicense, String channelCode, String imgId, Boolean flag);

	String insertInvoiceFlag(Invoice invoice, String channelCode, String imgId, Boolean flag);

	String insertItineraryFlag(Itinerary itinerary, String channelCode, String imgId, Boolean flag);

	String insertQuotaInvoiceFlag(QuotaInvoice quotaInvoice, String channelCode, String imgId, Boolean flag);

	String insertRalTicketFlag(RalTicket ralTicket, String channelCode, String imgId, Boolean flag);

	String insertTollInvoiceFlag(TollInvoice tollInvoice, String channelCode, String imgId, Boolean flag);

	String insertVatInvoiceFlag(VatInvoice vatInvoice, String channelCode, String imgId, Boolean flag);

    String insertEleInvoiceFlag(EleInvoice eleInvoice, String channelCode, String imgId, Boolean flag);

	String insertGeneralTestFlag(GeneralText bigTest, String channelCode, String imgId, Boolean flag);

}
