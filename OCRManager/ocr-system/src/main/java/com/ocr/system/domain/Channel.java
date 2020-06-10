package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ocr.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ocr.common.core.domain.BaseEntity;

/**
 * 渠道表 sys_channel
 * 
 * @author ocr
 * @date 2020-05-19
 */
public class Channel extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private String id;
	/** 渠道编号 */
	@Excel(name = "渠道编号", prompt = "渠道编号")
	private String channelCode;
	/** 渠道名称 */
	@Excel(name = "渠道名称", prompt = "渠道名称")
	private String channelName;
	/** 渠道英文缩写 */
	@Excel(name = "渠道英文缩写", prompt = "渠道英文缩写")
	private String channelNm;
	/** 渠道状态（0：在用  1：停用） */
	@Excel(name = "渠道状态", readConverterExp = "0=在用,1=停用")
	private String status;

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	public void setChannelCode(String channelCode) 
	{
		this.channelCode = channelCode;
	}

	public String getChannelCode() 
	{
		return channelCode;
	}
	public void setChannelName(String channelName) 
	{
		this.channelName = channelName;
	}

	public String getChannelName() 
	{
		return channelName;
	}
	public void setChannelNm(String channelNm) 
	{
		this.channelNm = channelNm;
	}

	public String getChannelNm() 
	{
		return channelNm;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelCode", getChannelCode())
            .append("channelName", getChannelName())
            .append("channelNm", getChannelNm())
            .append("status", getStatus())
            .toString();
    }
}
