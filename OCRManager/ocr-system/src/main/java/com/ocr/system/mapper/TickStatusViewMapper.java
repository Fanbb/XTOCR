package com.ocr.system.mapper;

import com.ocr.system.domain.TickStatusView;

import java.util.List;

/**
 * 识别流水 数据层
 *
 * @author ocr
 * @date 2020-05-20
 */
public interface TickStatusViewMapper {

    /**
     * 查询识别流水列表
     *
     * @param ocrTrade 识别流水信息
     * @return 识别流水集合
     */
    public List<TickStatusView> selectTickStatusViewList(TickStatusView TickStatusView);

}