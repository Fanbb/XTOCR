package com.ocr.system.mapper;

import com.ocr.system.domain.OcrTotalReport;

import java.util.List;

/**
 * 识别汇总 数据层
 * 
 * @author ocr
 * @date 2020-05-20
 */
public interface OcrTotalReportMapper
{

	/**
	 * 识别汇总查询
	 * @param ocrTotalReport
	 * @return
	 */
	public List<OcrTotalReport> selectOcrTotalReportList(OcrTotalReport ocrTotalReport);

}