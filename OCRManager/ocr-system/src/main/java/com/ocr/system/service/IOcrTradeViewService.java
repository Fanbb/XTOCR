package com.ocr.system.service;

import com.ocr.system.domain.OcrTradeView;

import java.util.List;

/**
 * 识别流水统计 服务层
 *
 * @author ocr
 * @date 2020-05-20
 */
public interface IOcrTradeViewService {


    /**
     * 查询识别流水列表
     *
     * @param ocrTradeView 识别流水统计信息
     * @return 识别流水集合
     */
    public List<OcrTradeView> selectOcrTradeViewList(OcrTradeView ocrTradeView);

}
