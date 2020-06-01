package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ocr.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 识别流水表 ocr_trade
 * 
 * @author ocr
 * @date 2020-05-20
 */
public class OcrTrade extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private String id;
	/** 识别日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date ocrDate;
	/** 识别时间 */
	private String ocrTime;
	/** 渠道 */
	private String channel;
	/** 唯一识别号码（身份证号、存单号） */
	private String ocrSeq;
	/** 成功标示（0 成功  1 失败） */
	private String ocrStatus;
	/** 识别节点（记录识别进行的节点） */
	private String ocrPoint;
	/** 影像id */
	private String imageId;
	/** 影像类型 */
	private String imageType;
	/** 影像类型名称 */
	private String imageName;
	/** 柜员号 */
	private String ocrTeller;
	/** 机构号 */
	private String ocrOrgan;
	/** 预留字段1 */
	private String remark1;
	/** 预留字段2 */
	private String remark2;
	/** 预留字段3 */
	private String remark3;

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	public void setOcrDate(Date ocrDate) 
	{
		this.ocrDate = ocrDate;
	}

	public Date getOcrDate() 
	{
		return ocrDate;
	}
	public void setChannel(String channel) 
	{
		this.channel = channel;
	}

	public String getChannel() 
	{
		return channel;
	}
	public void setOcrSeq(String ocrSeq) 
	{
		this.ocrSeq = ocrSeq;
	}

	public String getOcrSeq() 
	{
		return ocrSeq;
	}
	public void setOcrStatus(String ocrStatus) 
	{
		this.ocrStatus = ocrStatus;
	}

	public String getOcrStatus() 
	{
		return ocrStatus;
	}
	public void setOcrPoint(String ocrPoint) 
	{
		this.ocrPoint = ocrPoint;
	}

	public String getOcrPoint() 
	{
		return ocrPoint;
	}
	public void setImageId(String imageId) 
	{
		this.imageId = imageId;
	}

	public String getImageId() 
	{
		return imageId;
	}
	public void setImageType(String imageType) 
	{
		this.imageType = imageType;
	}

	public String getImageType() 
	{
		return imageType;
	}
	public void setImageName(String imageName) 
	{
		this.imageName = imageName;
	}

	public String getImageName() 
	{
		return imageName;
	}
	public void setOcrTeller(String ocrTeller) 
	{
		this.ocrTeller = ocrTeller;
	}

	public String getOcrTeller() 
	{
		return ocrTeller;
	}
	public void setOcrOrgan(String ocrOrgan) 
	{
		this.ocrOrgan = ocrOrgan;
	}

	public String getOcrOrgan() 
	{
		return ocrOrgan;
	}
	public void setRemark1(String remark1) 
	{
		this.remark1 = remark1;
	}

	public String getRemark1() 
	{
		return remark1;
	}
	public void setRemark2(String remark2) 
	{
		this.remark2 = remark2;
	}

	public String getRemark2() 
	{
		return remark2;
	}
	public void setRemark3(String remark3) 
	{
		this.remark3 = remark3;
	}

	public String getRemark3() 
	{
		return remark3;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ocrDate", getOcrDate())
            .append("ocrTime", getOcrTime())
            .append("channel", getChannel())
            .append("ocrSeq", getOcrSeq())
            .append("ocrStatus", getOcrStatus())
            .append("ocrPoint", getOcrPoint())
            .append("imageId", getImageId())
            .append("imageType", getImageType())
            .append("imageName", getImageName())
            .append("ocrTeller", getOcrTeller())
            .append("ocrOrgan", getOcrOrgan())
            .append("remark1", getRemark1())
            .append("remark2", getRemark2())
            .append("remark3", getRemark3())
            .toString();
    }

	public String getOcrTime() {
		return ocrTime;
	}

	public void setOcrTime(String ocrTime) {
		this.ocrTime = ocrTime;
	}
}
