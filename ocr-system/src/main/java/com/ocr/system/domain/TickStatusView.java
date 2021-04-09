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
    @Excel(name = "影像类型", readConverterExp = "0=无效类型,1=身份证,2=银行卡,3=存单,4=房本,5=存折,6=户口本,7=结婚证,8=行驶证,9=驾驶证,10=车牌号,11=营业执照")
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

    @Excel(name = "字段勾选成功数 ", prompt = "字段勾选成功数")
    private Integer rightTotal;

    @Excel(name = "字段总数", prompt = "字段总数")
    private Integer fieldTotal;

    @Excel(name = "字段识别率", prompt = "字段识别率")
    private String tickRate;

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

    public Integer getRightTotal() {
        return rightTotal;
    }

    public void setRightTotal(Integer rightTotal) {
        this.rightTotal = rightTotal;
    }

    public Integer getFieldTotal() {
        return fieldTotal;
    }

    public void setFieldTotal(Integer fieldTotal) {
        this.fieldTotal = fieldTotal;
    }

    public String getTickRate() {
        return tickRate;
    }

    public void setTickRate(String tickRate) {
        this.tickRate = tickRate;
    }

    public TickStatusView(Date ocrDate, String channel, String imageName, Integer tickTotal, Integer tradeTotal, String ocrRate, Integer rightTotal, Integer fieldTotal, String tickRate) {
        this.ocrDate = ocrDate;
        this.channel = channel;
        this.imageName = imageName;
        this.tickTotal = tickTotal;
        this.tradeTotal = tradeTotal;
        this.ocrRate = ocrRate;
        this.rightTotal = rightTotal;
        this.fieldTotal = fieldTotal;
        this.tickRate = tickRate;
    }

    public TickStatusView() {
    }

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("ocrDate", getOcrDate())
                .append("channel", getChannel())
                .append("imageName", getImageName())
                .append("tickTotal", getTickTotal())
                .append("tradeTotal", getTradeTotal())
                .append("ocrRate", getOcrRate())
                .append("rightTotal", getRightTotal())
                .append("fieldTotal", getFieldTotal())
                .append("tickRate", getTickRate())
                .toString();
    }

    public Integer getTickTotal() {
        return tickTotal;
    }

    public void setTickTotal(Integer tickTotal) {
        this.tickTotal = tickTotal;
    }
}
