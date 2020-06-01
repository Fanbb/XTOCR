package com.ocr.system.mapper;

import com.ocr.system.domain.ChannelType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道识别类型参数 数据层
 * 
 * @author ocr
 * @date 2020-05-29
 */
public interface ChannelTypeMapper 
{
	/**
     * 查询渠道识别类型参数信息
     * 
     * @param id 渠道识别类型参数ID
     * @return 渠道识别类型参数信息
     */
	public ChannelType selectChannelTypeById(String id);
	
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
     * 删除渠道识别类型参数
     * 
     * @param id 渠道识别类型参数ID
     * @return 结果
     */
	public int deleteChannelTypeById(String id);
	
	/**
     * 批量删除渠道识别类型参数
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteChannelTypeByIds(String[] ids);

	public ChannelType selectByNoAndType(@Param("channelCode")String channelCode, @Param("ocrType")String ocrType);
}