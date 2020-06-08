package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ocr.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 识别统计视图 ocr_trade_view
 *
 * @author ocr
 * @date 2020-05-20
 */
public class OcrTradeView extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ocrDate;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 影像类型
     */
    private String imageType;
    /**
     * 识别总量
     */
    private Integer tradeTotal;
    /**
     * 识别率
     */
    private String ocrRate;

    public Date getOcrDate() {
        return ocrDate;
    }

    public void setOcrDate(Date ocrDate) {
        this.ocrDate = ocrDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Integer getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(Integer tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public String getOcrRate() {
        return ocrRate;
    }

    public void setOcrRate(String ocrRate) {
        this.ocrRate = ocrRate;
    }

    public OcrTradeView(Date ocrDate, String channel, String imageType, Integer tradeTotal, String ocrRate) {
        this.ocrDate = ocrDate;
        this.channel = channel;
        this.imageType = imageType;
        this.tradeTotal = tradeTotal;
        this.ocrRate = ocrRate;
    }

    public OcrTradeView() {
    }

    @Override
    public String toString() {
        return "OcrTradeView{" +
                "ocrDate=" + ocrDate +
                ", channel='" + channel + '\'' +
                ", imageType='" + imageType + '\'' +
                ", tradeTotal=" + tradeTotal +
                ", ocrRate='" + ocrRate + '\'' +
                '}';
    }
}
