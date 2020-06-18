package com.ocr.system.service;

import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.BankCard;
import com.ocr.system.model.DepositReceipt;
import com.ocr.system.model.IDCardBack;
import com.ocr.system.model.IDCardFront;

import java.util.List;
import java.util.Map;

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
	 * 身份证正面流水
	 * @param idCardBack
	 * @param channelCode
	 * @param imgId 影像id
	 * @return
	 */
	public String insertIDCardBack(IDCardBack idCardBack,String channelCode, String imgId);

	/**
	 * 身份证正面流水
	 * @param bankCard 渠道
	 * @param channelCode 渠道
	 * @param imgId 影像id
	 * @return
	 */
	public String insertBankCard(BankCard bankCard,String channelCode, String imgId);

	/**
	 * 身份证正面流水
	 * @param depositReceipt
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	public String insertDeposit(DepositReceipt depositReceipt,String channelCode, String imgId);

	/**
	 * 存入
	 * @param channelCode
	 * @param imgId
	 * @return
	 */
	public String insertNoneTrade(String result,String channelCode, String imgId);


	public String[] selectImagePath(String[] tradeIds);

	public String selectCountByTrickStatus(String tickStatus);

	List<String> selectNameToTypeTotal();

	List<String> selectValueToTypeTotal();

	List<String> selectNameToDataTotal();

	List<String> selectValueToDataTotal();

    void updateRemark2ByIds(String[] ids);
}
