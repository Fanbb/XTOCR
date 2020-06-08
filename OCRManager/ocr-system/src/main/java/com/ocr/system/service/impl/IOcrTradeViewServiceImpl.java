package com.ocr.system.service.impl;

import com.ocr.system.domain.OcrTradeView;
import com.ocr.system.mapper.OcrTradeViewMapper;
import com.ocr.system.service.IOcrTradeViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IOcrTradeViewServiceImpl implements IOcrTradeViewService {

    @Autowired
    private OcrTradeViewMapper ocrTradeViewMapper;

    @Override
    public List<OcrTradeView> selectOcrTradeViewList(OcrTradeView ocrTradeView) {
        return ocrTradeViewMapper.selectOcrTradeViewList(ocrTradeView);
    }
}
