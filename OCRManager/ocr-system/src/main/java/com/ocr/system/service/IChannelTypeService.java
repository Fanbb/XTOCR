package com.ocr.system.service;

import com.ocr.system.domain.ChannelType;

import java.util.List;

/**
 * 渠道识别类型参数 服务层
 *
 * @author ocr
 * @date 2020-05-29
 */
public interface IChannelTypeService {
    /**
     * 查询渠道识别类型参数信息
     *
     * @param id 渠道识别类型参数ID
     * @return 渠道识别类型参数信息
     */
    public ChannelType selectChannelTypeById(String id);

    /**
     * 查询渠道识别类型参数信息
     * @param channelCode 渠道号
     * @param ocrType ocr识别类型
     * @return
     */
    public ChannelType selectByNoAndType(String channelCode,String ocrType);

    /**
     * 查询渠道识别类型参数列表
     *
     * @param channelType 渠道识别类型参数信息
     * @return 渠道识别类型参数集合
     */
    public List<ChannelType> selectChannelTypeList(ChannelType channelType);

    /**
     * 新增渠道识别类型参数
     *
     * @param channelType 渠道识别类型参数信息
     * @return 结果
     */
    public int insertChannelType(ChannelType channelType);

    /**
     * 修改渠道识别类型参数
     *
     * @param channelType 渠道识别类型参数信息
     * @return 结果
     */
    public int updateChannelType(ChannelType channelType);

    /**
     * 删除渠道识别类型参数信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChannelTypeByIds(String ids);

    void deleteChannelTypeByChannelCode(String channelCode);
}
