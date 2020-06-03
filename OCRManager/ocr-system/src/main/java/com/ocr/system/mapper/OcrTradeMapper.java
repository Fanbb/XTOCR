package com.ocr.system.mapper;

import com.ocr.system.domain.OcrTrade;
import java.util.List;	

/**
 * 识别流水 数据层
 * 
 * @author ocr
 * @date 2020-05-20
 */
public interface OcrTradeMapper 
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
     * 删除识别流水
     * 
     * @param id 识别流水ID
     * @return 结果
     */
	public int deleteOcrTradeById(String id);
	
	/**
     * 批量删除识别流水
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOcrTradeByIds(String[] ids);


	/**
	 * 根据流水id获取影像地址
	 * @param id
	 * @return
	 */
	public String[] selectImagePath(String[] ids);

}