package com.ocr.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ocr.common.annotation.Excel;
import com.ocr.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 识别统计视图 tick_status_view
 *
 * @author ocr
 * @date 2020-05-20
 */
public class TickStatusView extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @Excel(name = "识别日期", prompt = "识别日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ocrDate;
    /**
     * 渠道
     */
    @Excel(name = "识别渠道号", prompt = "识别渠道")
    private String channel;
    /**
     * 影像类型
     */
    @Excel(name = "影像类型", readConverterExp = "1=身份证,2=银行卡,3=存单")
    private String imageName;
    /**
     * OCR识别成功数量
     */
    @Excel(name = "勾对成功数量", prompt = "勾对成功数量")
    private Integer tickTotal;
    /**
     * 识别总量
     */
    @Excel(name = "识别数量", prompt = "识别数量")
    private Integer tradeTotal;
    /**
     * 识别率
     */
    @Excel(name = "识别率", prompt = "识别率")
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageType) {
        this.imageName = imageType;
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

    public TickStatusView(Date ocrDate, String channel, String imageName, Integer tickTotal, Integer tradeTotal, String ocrRate) {
        this.ocrDate = ocrDate;
        this.channel = channel;
        this.imageName = imageName;
        this.tickTotal = tickTotal;
        this.tradeTotal = tradeTotal;
        this.ocrRate = ocrRate;
    }

    public TickStatusView() {
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("ocrDate", getOcrDate())
                .append("channel", getChannel())
                .append("imageName", getImageName())
                .append("tickTotal", getTickTotal())
                .append("tradeTotal", getOcrRate())
                .toString();
    }

    public Integer getTickTotal() {
        return tickTotal;
    }

    public void setTickTotal(Integer tickTotal) {
        this.tickTotal = tickTotal;
    }
}
