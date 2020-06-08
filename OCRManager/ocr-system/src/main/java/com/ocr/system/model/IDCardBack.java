package com.ocr.system.model;

/**
 * 身份证反面
 * @author Jocker
 * @date 2020/5/25
 */
public class IDCardBack {
    /**
     * 签发机关
     */
    private String authority;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public IDCardBack(String authority, String startDate, String endDate, String imgType) {
        this.authority = authority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imgType = imgType;
    }

    public IDCardBack() {
    }

    @Override
    public String toString() {
        return "IDCardBack{" +
                "authority='" + authority + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", imgType='" + imgType + '\'' +
                '}';
    }
}
