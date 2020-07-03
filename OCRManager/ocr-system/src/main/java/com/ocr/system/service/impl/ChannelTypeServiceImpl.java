package com.ocr.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocr.system.mapper.ChannelTypeMapper;
import com.ocr.system.domain.ChannelType;
import com.ocr.system.service.IChannelTypeService;
import com.ocr.common.core.text.Convert;

/**
 * 渠道识别类型参数 服务层实现
 *
 * @author ocr
 * @date 2020-05-29
 */
@Service
public class ChannelTypeServiceImpl implements IChannelTypeService {
    @Autowired
    private ChannelTypeMapper channelTypeMapper;

    /**
     * 查询渠道识别类型参数信息
     *
     * @param id 渠道识别类型参数ID
     * @return 渠道识别类型参数信息
     */
    @Override
    public ChannelType selectChannelTypeById(String id) {
        return channelTypeMapper.selectChannelTypeById(id);
    }

    @Override
    public ChannelType selectByNoAndType(String channelCode, String ocrType) {
        return channelTypeMapper.selectByNoAndType(channelCode, ocrType);
    }

    /**
     * 查询渠道识别类型参数列表
     *
     * @param channelType 渠道识别类型参数信息
     * @return 渠道识别类型参数集合
     */
    @Override
    public List<ChannelType> selectChannelTypeList(ChannelType channelType) {
        return channelTypeMapper.selectChannelTypeList(channelType);
    }

    /**
     * 新增渠道识别类型参数
     *
     * @param channelType 渠道识别类型参数信息
     * @return 结果
     */
    @Override
    public int insertChannelType(ChannelType channelType) {
        channelType.setId(System.currentTimeMillis() + "");
        return channelTypeMapper.insertChannelType(channelType);
    }

    /**
     * 修改渠道识别类型参数
     *
     * @param channelType 渠道识别类型参数信息
     * @return 结果
     */
    @Override
    public int updateChannelType(ChannelType channelType) {
        return channelTypeMapper.updateChannelType(channelType);
    }

    /**
     * 删除渠道识别类型参数对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteChannelTypeByIds(String ids) {
        return channelTypeMapper.deleteChannelTypeByIds(Convert.toStrArray(ids));
    }

    @Override
    public void deleteChannelTypeByChannelCode(String channelCode) {
        channelTypeMapper.deleteChannelTypeByChannelCode(channelCode);
    }

}
