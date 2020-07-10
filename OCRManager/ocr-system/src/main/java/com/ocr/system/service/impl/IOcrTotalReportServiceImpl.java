package com.ocr.system.service.impl;

import com.ocr.system.domain.OcrTotalReport;
import com.ocr.system.mapper.OcrTotalReportMapper;
import com.ocr.system.service.IOcrTotalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IOcrTotalReportServiceImpl implements IOcrTotalReportService {

    @Autowired
    private OcrTotalReportMapper ocrTotalReportMapper;


    @Override
    public List<OcrTotalReport> selectOcrTradeViewList(OcrTotalReport ocrTotalReport) {
        return ocrTotalReportMapper.selectOcrTotalReportList(ocrTotalReport);
    }
}
