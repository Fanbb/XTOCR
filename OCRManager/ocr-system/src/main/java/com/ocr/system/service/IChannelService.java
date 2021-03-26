package com.ocr.system.service;

import com.ocr.system.domain.Channel;

import java.util.List;

/**
 * 渠道 服务层
 *
 * @author ocr
 * @date 2020-05-19
 */
public interface IChannelService {
    /**
     * 查询渠道信息
     *
     * @param id 渠道ID
     * @return 渠道信息
     */
    public Channel selectChannelById(String id);

    /**
     * 查询渠道列表
     *
     * @param channel 渠道信息
     * @return 渠道集合
     */
    public List<Channel> selectChannelList(Channel channel);

    /**
     * 新增渠道
     *
     * @param channel 渠道信息
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改渠道
     *
     * @param channel 渠道信息
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 删除渠道信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChannelByIds(String ids);

    /**
     * 获取渠道平台状态 查看是否有授权
     *
     * @param channelCode
     * @return false启用状态 true停用状态
     */
    public Boolean channelStatus(String channelCode);

    /**
     * 判断渠道号是否存在
     *
     * @param channelCode
     * @return
     */
    Channel selectChannelByChannelCode(String channelCode);

    List<Channel> selectChannelAll();

    List<Channel> selectChannelByUserId(Long userId);

    List<Channel> selectChannelsByUserId(Long userId);
}
