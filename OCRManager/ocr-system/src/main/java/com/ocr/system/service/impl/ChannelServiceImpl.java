package com.ocr.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocr.system.mapper.ChannelMapper;
import com.ocr.system.domain.Channel;
import com.ocr.system.service.IChannelService;
import com.ocr.common.core.text.Convert;

/**
 * 渠道 服务层实现
 *
 * @author ocr
 * @date 2020-05-19
 */
@Service
public class ChannelServiceImpl implements IChannelService {
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 查询渠道信息
     *
     * @param id 渠道ID
     * @return 渠道信息
     */
    @Override
    public Channel selectChannelById(String id) {
        return channelMapper.selectChannelById(id);
    }

    /**
     * 查询渠道列表
     *
     * @param channel 渠道信息
     * @return 渠道集合
     */
    @Override
    public List<Channel> selectChannelList(Channel channel) {
        return channelMapper.selectChannelList(channel);
    }

    /**
     * 新增渠道
     *
     * @param channel 渠道信息
     * @return 结果
     */
    @Override
    public int insertChannel(Channel channel) {
        return channelMapper.insertChannel(channel);
    }

    /**
     * 修改渠道
     *
     * @param channel 渠道信息
     * @return 结果
     */
    @Override
    public int updateChannel(Channel channel) {
        return channelMapper.updateChannel(channel);
    }

    /**
     * 删除渠道对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteChannelByIds(String ids) {
        return channelMapper.deleteChannelByIds(Convert.toStrArray(ids));
    }

    @Override
    public Boolean channelStatus(String channelCode) {
        Channel channel = channelMapper.selectChannelByCode(channelCode);
        if (channel.getStatus().equals("1")) {
            return true;
        }
        return false;
    }

    @Override
    public Channel selectChannelByChannelCode(String channelCode) {
        return channelMapper.selectChannelByCode(channelCode);
    }

    @Override
    public List<Channel> selectChannelByUserId(Long userId) {
        List<Channel> userRoles = channelMapper.selectChannelByUserId(userId);
        List<Channel> roles = selectChannelAll();
        for (Channel role : roles) {
            for (Channel userRole : userRoles) {
                if (role.getChannelName().equals(userRole.getChannelName())) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public List<Channel> selectChannelsByUserId(Long userId) {
        return channelMapper.selectChannelsByUserId(userId);
    }

    @Override
    public List<Channel> selectChannelAll() {
        return channelMapper.selectChannelAll();
    }

}
