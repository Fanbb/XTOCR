package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ocr.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 识别影像表 ocr_image
 * 
 * @author ocr
 * @date 2020-05-20
 */
public class OcrImage extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private String id;
	/** 匹配流水id */
	private String compTradeId;
	/** 匹配流水号 */
	private String compTxnSeq;
	/** 识别日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date ocrDate;
	/** 识别日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date ocrTime;
	/** 根影像（关联流水中的影像id） */
	private String parentId;
	/** ocr识别结果 */
	private String ocrResult;
	/** 本地文件目录 */
	private String localPath;
	/** 影像类型 */
	private String type;
	/** 类型名称 */
	private String typeName;

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	public void setCompTradeId(String compTradeId) 
	{
		this.compTradeId = compTradeId;
	}

	public String getCompTradeId() 
	{
		return compTradeId;
	}
	public void setCompTxnSeq(String compTxnSeq) 
	{
		this.compTxnSeq = compTxnSeq;
	}

	public String getCompTxnSeq() 
	{
		return compTxnSeq;
	}
	public void setOcrDate(Date ocrDate) 
	{
		this.ocrDate = ocrDate;
	}

	public Date getOcrDate() 
	{
		return ocrDate;
	}
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}

	public String getParentId() 
	{
		return parentId;
	}
	public void setOcrResult(String ocrResult) 
	{
		this.ocrResult = ocrResult;
	}

	public String getOcrResult() 
	{
		return ocrResult;
	}
	public void setLocalPath(String localPath) 
	{
		this.localPath = localPath;
	}

	public String getLocalPath() 
	{
		return localPath;
	}
	public void setType(String type) 
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	public void setTypeName(String typeName) 
	{
		this.typeName = typeName;
	}

	public String getTypeName() 
	{
		return typeName;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("compTradeId", getCompTradeId())
            .append("compTxnSeq", getCompTxnSeq())
            .append("ocrDate", getOcrDate())
            .append("ocrTime", getOcrTime())
            .append("parentId", getParentId())
            .append("ocrResult", getOcrResult())
            .append("localPath", getLocalPath())
            .append("type", getType())
            .append("typeName", getTypeName())
            .toString();
    }

	public Date getOcrTime() {
		return ocrTime;
	}

	public void setOcrTime(Date ocrTime) {
		this.ocrTime = ocrTime;
	}
}
