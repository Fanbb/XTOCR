package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ocr.common.annotation.Excel;
import com.ocr.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 识别流水表 ocr_trade
 *
 * @author ocr
 * @date 2020-05-20
 */
public class OcrTrade extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 识别日期
     */
    @Excel(name = "识别日期", prompt = "识别日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ocrDate;
    /**
     * 识别时间
     */
    private String ocrTime;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 唯一识别号码（身份证号、存单号）
     */
    private String ocrSeq;
    /**
     * OCR识别状态（0 成功  1 失败）
     */
    private String ocrStatus;
    /**
     * 勾对状态（0未勾对  1 已勾对成功 2已勾对失败）
     */
    private String tickStatus;
    /**
     * 影像id
     */
    private String imageId;
    /**
     * 影像类型
     */
    private String imageType;
    /**
     * 影像类型名称
     */
    private String imageName;
    /**
     * 平台勾对状态 （0未返回默认成功 1返回更改失败）
     */
    private String platStatus;
    /**
     * 机构号
     */
    private String ocrOrgan;
    /**
     * 预留字段1
     */
    private String remark1;
    /**
     * 预留字段2
     */
    private String remark2;
    /**
     * 预留字段3
     */
    private String remark3;

    /**
     * 渠道
     */
    private String[] channelCodes;

    private Long userId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOcrDate(Date ocrDate) {
        this.ocrDate = ocrDate;
    }

    public Date getOcrDate() {
        return ocrDate;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setOcrSeq(String ocrSeq) {
        this.ocrSeq = ocrSeq;
    }

    public String getOcrSeq() {
        return ocrSeq;
    }

    public void setOcrStatus(String ocrStatus) {
        this.ocrStatus = ocrStatus;
    }

    public String getOcrStatus() {
        return ocrStatus;
    }

    public void setTickStatus(String tickStatus) {
        this.tickStatus = tickStatus;
    }

    public String getTickStatus() {
        return tickStatus;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setPlatStatus(String platStatus) {
        this.platStatus = platStatus;
    }

    public String getPlatStatus() {
        return platStatus;
    }

    public void setOcrOrgan(String ocrOrgan) {
        this.ocrOrgan = ocrOrgan;
    }

    public String getOcrOrgan() {
        return ocrOrgan;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark3() {
        return remark3;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ocrDate", getOcrDate())
                .append("ocrTime", getOcrTime())
                .append("channel", getChannel())
                .append("ocrSeq", getOcrSeq())
                .append("ocrStatus", getOcrStatus())
                .append("tickStatus", getTickStatus())
                .append("imageId", getImageId())
                .append("imageType", getImageType())
                .append("imageName", getImageName())
                .append("platStatus", getPlatStatus())
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

    public String[] getChannelCodes() {
        return channelCodes;
    }

    public void setChannelCodes(String[] channelCodes) {
        this.channelCodes = channelCodes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
