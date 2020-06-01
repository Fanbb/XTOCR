package com.ocr.system.model;

/**
 * 身份证反面
 * @author Jocker
 * @date 2020/5/25
 */
public class IDCardBack {
    /**
     * 有效开始日期
     */
    private String startDate;
    /**
     * 有效结束日期
     */
    private String endDate;
    /**
     * 图片类型
     */
    private String imgType;


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public IDCardBack(String startDate, String endDate, String imgType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.imgType = imgType;
    }

    public IDCardBack() {
    }

    @Override
    public String toString() {
        return "IDCardBack{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", imgType='" + imgType + '\'' +
                '}';
    }
}
