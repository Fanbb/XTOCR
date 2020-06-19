package com.ocr.system.mapper;

import com.ocr.system.domain.Channel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道 数据层
 * 
 * @author ocr
 * @date 2020-05-19
 */
public interface ChannelMapper 
{
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
     * 删除渠道
     * 
     * @param id 渠道ID
     * @return 结果
     */
	public int deleteChannelById(String id);
	
	/**
     * 批量删除渠道
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteChannelByIds(String[] ids);

	/**
	 * 根据渠道编号获取渠道
	 * @param channelCode
	 * @return
	 */
	public Channel selectChannelByCode(String channelCode);

    List<Channel> selectChannelAll();

    List<Channel> selectChannelByUserId(@Param("userId") Long userId);
}