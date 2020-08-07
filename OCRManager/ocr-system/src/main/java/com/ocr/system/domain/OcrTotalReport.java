package com.ocr.system.domain;

import com.ocr.common.annotation.Excel;
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
     * 1身份证2银行卡3存单4房本5存折6户口本7结婚证8行驶证9驾驶证10车牌号11营业执照
     */
    @Excel(name = "识别类型", readConverterExp = "0=无效类型,1=身份证,2=银行卡,3=存单,4=房本,5=存折,6=户口本,7=结婚证,8=行驶证,9=驾驶证,10=车牌号,11=营业执照")
    private String imageName;
    /**
     * 识别总数
     */
    @Excel(name = "识别总数", prompt = "识别总数")
    private String tradeTotal;
    /**
     * OCR识别成功数量
     */
    @Excel(name = "OCR识别成功数量", prompt = "OCR识别成功数量")
    private String ocrTotal;
    /**
     * OCR识别率
     */
    @Excel(name = "OCR识别率", prompt = "OCR识别率")
    private String ocrRate;
    /**
     * 平台返回识别成功数量
     */
    @Excel(name = "平台返回识别成功数量", prompt = "平台返回识别成功数量")
    private String platTotal;
    /**
     * 平台返回识别成功率
     */
    @Excel(name = "平台返回识别成功率", prompt = "平台返回识别成功率")
    private String platTate;
    /**
     * 版面识别成功数量
     */
    @Excel(name = "版面识别成功数量", prompt = "版面识别成功数量")
    private String tickTotal;
    /**
     * 版面识别成功数量
     */
    @Excel(name = "版面识别勾对总数", prompt = "版面识别勾对总数")
    private String tickTotalNumber;
    /**
     * 版面识别成功率
     */
    @Excel(name = "版面识别成功率", prompt = "版面识别成功率")
    private String tickRate;
    /**
     * 字段勾选成功数
     */
    @Excel(name = "字段勾选成功数", prompt = "字段勾选成功数")
    private Integer rightTotal;
    /**
     * 字段总数
     */
    @Excel(name = "字段总数", prompt = "字段总数")
    private Integer fieldTotal;
    /**
     * 字段识别率
     */
    @Excel(name = "字段识别率", prompt = "字段识别率")
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

    public String getTickTotalNumber() {
        return tickTotalNumber;
    }

    public void setTickTotalNumber(String tickTotalNumber) {
        this.tickTotalNumber = tickTotalNumber;
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

    public OcrTotalReport(String imageName, String tradeTotal, String ocrTotal, String ocrRate, String tickTotal, String tickTotalNumber, String tickRate, String platTotal, String platTate, Integer rightTotal, Integer fieldTotal, String fieldRate) {
        this.imageName = imageName;
        this.tradeTotal = tradeTotal;
        this.ocrTotal = ocrTotal;
        this.ocrRate = ocrRate;
        this.platTotal = platTotal;
        this.platTate = platTate;
        this.tickTotal = tickTotal;
        this.tickTotalNumber = tickTotalNumber;
        this.tickRate = tickRate;
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
                ", platTotal='" + platTotal + '\'' +
                ", platTate='" + platTate + '\'' +
                ", tickTotal='" + tickTotal + '\'' +
                ", tickTotalNumber='" + tickTotalNumber + '\'' +
                ", tickRate='" + tickRate + '\'' +
                ", rightTotal=" + rightTotal +
                ", fieldTotal=" + fieldTotal +
                ", fieldRate='" + fieldRate + '\'' +
                '}';
    }
}
