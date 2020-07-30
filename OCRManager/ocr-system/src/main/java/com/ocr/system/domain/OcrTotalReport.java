package com.ocr.system.domain;

import com.ocr.common.core.domain.BaseEntity;

/**
 * ocr识别结果汇总
 *
 * @author ocr
 * @date 2020-05-20
 */
public class OcrTotalReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 识别类型
     */
    private String imageName;
    /**
     * 识别总数
     */
    private String tradeTotal;
    /**
     * OCR识别成功数量
     */
    private String ocrTotal;
    /**
     * OCR识别率
     */
    private String ocrRate;
    /**
     * 勾对识别成功数量
     */
    private String tickTotal;
    /**
     * 勾对识别成功率
     */
    private String tickRate;
    /**
     * 平台返回识别成功数量
     */
    private String platTotal;
    /**
     * 平台返回识别成功率
     */
    private String platTate;
    /**
     * 字段勾选成功数
     */
    private Integer rightTotal;

    /**
     * 字段总数
     */
    private Integer fieldTotal;

    /**
     * 字段识别率
     */
    private String fieldRate;


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(String tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public String getOcrTotal() {
        return ocrTotal;
    }

    public void setOcrTotal(String ocrTotal) {
        this.ocrTotal = ocrTotal;
    }

    public String getOcrRate() {
        return ocrRate;
    }

    public void setOcrRate(String ocrRate) {
        this.ocrRate = ocrRate;
    }

    public String getTickTotal() {
        return tickTotal;
    }

    public void setTickTotal(String tickTotal) {
        this.tickTotal = tickTotal;
    }

    public String getTickRate() {
        return tickRate;
    }

    public void setTickRate(String tickRate) {
        this.tickRate = tickRate;
    }

    public String getPlatTotal() {
        return platTotal;
    }

    public void setPlatTotal(String platTotal) {
        this.platTotal = platTotal;
    }

    public String getPlatTate() {
        return platTate;
    }

    public void setPlatTate(String platTate) {
        this.platTate = platTate;
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

    public String getFieldRate() {
        return fieldRate;
    }

    public void setFieldRate(String fieldRate) {
        this.fieldRate = fieldRate;
    }

    public OcrTotalReport() {
    }

    public OcrTotalReport(String imageName, String tradeTotal, String ocrTotal, String ocrRate, String tickTotal, String tickRate, String platTotal, String platTate, Integer rightTotal, Integer fieldTotal, String fieldRate) {
        this.imageName = imageName;
        this.tradeTotal = tradeTotal;
        this.ocrTotal = ocrTotal;
        this.ocrRate = ocrRate;
        this.tickTotal = tickTotal;
        this.tickRate = tickRate;
        this.platTotal = platTotal;
        this.platTate = platTate;
        this.rightTotal = rightTotal;
        this.fieldTotal = fieldTotal;
        this.fieldRate = fieldRate;
    }

    @Override
    public String toString() {
        return "OcrTotalReport{" +
                "imageName='" + imageName + '\'' +
                ", tradeTotal='" + tradeTotal + '\'' +
                ", ocrTotal='" + ocrTotal + '\'' +
                ", ocrRate='" + ocrRate + '\'' +
                ", tickTotal='" + tickTotal + '\'' +
                ", tickRate='" + tickRate + '\'' +
                ", platTotal='" + platTotal + '\'' +
                ", platTate='" + platTate + '\'' +
                ", rightTotal=" + rightTotal +
                ", fieldTotal=" + fieldTotal +
                ", fieldRate='" + fieldRate + '\'' +
                '}';
    }
}
