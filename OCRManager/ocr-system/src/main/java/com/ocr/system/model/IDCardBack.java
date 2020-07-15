package com.ocr.system.model;

/**
 * 身份证反面
 * @author Jocker
 * @date 2020/5/25
 */
public class IDCardBack {
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
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;


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

    public IDCardBack(String tradeId,String authority, String startDate, String endDate, String imgType,String flag) {
        this.tradeId = tradeId;
        this.authority = authority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imgType = imgType;
        this.flag = flag;
    }

    public IDCardBack() {
    }

    @Override
    public String toString() {
        return "IDCardBack{" +
                "tradeId='" + tradeId + '\'' +
                ", authority='" + authority + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
