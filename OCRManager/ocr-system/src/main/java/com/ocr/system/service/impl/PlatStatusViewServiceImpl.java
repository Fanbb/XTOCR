package com.ocr.system.service.impl;

import com.ocr.system.domain.PlatStatusView;
import com.ocr.system.mapper.PlatStatusViewMapper;
import com.ocr.system.service.IPlatStatusViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatStatusViewServiceImpl implements IPlatStatusViewService {

    @Autowired
    private PlatStatusViewMapper platStatusViewMapper;

    @Override
    public List<PlatStatusView> selectPlatStatusViewList(PlatStatusView platStatusView) {
        return platStatusViewMapper.selectPlatStatusViewList(platStatusView);
    }
}
