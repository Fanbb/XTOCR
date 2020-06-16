package com.ocr.system.service.impl;

import com.ocr.system.domain.TickStatusView;
import com.ocr.system.mapper.TickStatusViewMapper;
import com.ocr.system.service.ITickStatusViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TickStatusViewServiceImpl implements ITickStatusViewService {

    @Autowired
    private TickStatusViewMapper tickStatusViewMapper;

    @Override
    public List<TickStatusView> selectTickStatusViewwList(TickStatusView tickStatusVieww) {
        return tickStatusViewMapper.selectTickStatusViewList(tickStatusVieww);
    }
}
