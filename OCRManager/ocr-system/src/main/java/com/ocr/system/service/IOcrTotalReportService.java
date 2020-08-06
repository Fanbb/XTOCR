package com.ocr.system.service;

import com.ocr.system.domain.OcrTotalReport;
import com.ocr.system.domain.OcrTradeView;

import java.util.List;

/**
 * 识别流水汇总 服务层
 *
 * @author ocr
 * @date 2020-05-20
 */
public interface IOcrTotalReportService {


    /**
     * 查询识别流水汇总列表
     *
     * @param ocrTotalReport 识别流水汇总信息
     * @return 识别流水汇总集合
     */
    public List<OcrTotalReport> selectOcrTradeViewList(OcrTotalReport ocrTotalReport);
    public List<OcrTotalReport> selectOcrTotalReportList2(OcrTotalReport ocrTotalReport);

}
