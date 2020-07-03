package com.ocr.system.domain;

import com.ocr.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ocr.common.core.domain.BaseEntity;

/**
 * 渠道识别类型参数表 sys_channel_type
 *
 * @author ocr
 * @date 2020-05-29
 */
public class ChannelType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 渠道编号
     */
    @Excel(name = "渠道编号", prompt = "渠道编号")
    private String channelCode;
    /**
     * 渠道名称
     */
    @Excel(name = "渠道名称", prompt = "渠道名称")
    private String channelName;
    /**
     * 渠道英文简称
     */
    @Excel(name = "渠道英文简称", prompt = "渠道英文简称")
    private String channelNm;
    /**
     * 识别类型
     */
    @Excel(name = "识别类型", prompt = "识别类型")
    private String ocrType;
    /**
     * 识别类型名称
     */
    @Excel(name = "识别类型名称", prompt = "识别类型名称")
    private String ocrTypeNm;

    public String[] getOcrTypes() {
        return ocrTypes;
    }

    public void setOcrTypes(String[] ocrTypes) {
        this.ocrTypes = ocrTypes;
    }

    /** 类型组 */
    private String[] ocrTypes;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelNm(String channelNm) {
        this.channelNm = channelNm;
    }

    public String getChannelNm() {
        return channelNm;
    }

    public void setOcrType(String ocrType) {
        this.ocrType = ocrType;
    }

    public String getOcrType() {
        return ocrType;
    }

    public void setOcrTypeNm(String ocrTypeNm) {
        this.ocrTypeNm = ocrTypeNm;
    }

    public String getOcrTypeNm() {
        return ocrTypeNm;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("channelCode", getChannelCode())
                .append("channelName", getChannelName())
                .append("channelNm", getChannelNm())
                .append("ocrType", getOcrType())
                .append("ocrTypeNm", getOcrTypeNm())
                .toString();
    }
}
