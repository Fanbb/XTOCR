package com.ocr.system.model;

/**
 * 身份证反面副例
 * @author Jocker
 * @date 2020/7/29
 */
public class IDCardBackBack {
    /**
     * 流水ID
     */
    private String tradeId;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public IDCardBackBack(String tradeId, String authority, String startDate, String endDate) {
        this.tradeId = tradeId;
        this.authority = authority;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public IDCardBackBack() {
    }

    @Override
    public String toString() {
        return "IDCardBackBack{" +
                "tradeId='" + tradeId + '\'' +
                ", authority='" + authority + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

}
