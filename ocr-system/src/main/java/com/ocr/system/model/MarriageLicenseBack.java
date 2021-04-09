package com.ocr.system.model;


/**
 * 结婚证副例
 * @author Jocker
 */
public class MarriageLicenseBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 结婚证号
     */
    private String marriageNo;
    /**
     * 配偶姓名
     */
    private String name;
    /**
     * 配偶身份证号
     */
    private String idCardNo;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getMarriageNo() {
        return marriageNo;
    }

    public void setMarriageNo(String marriageNo) {
        this.marriageNo = marriageNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public MarriageLicenseBack() {
    }

    public MarriageLicenseBack(String tradeId, String marriageNo, String name, String idCardNo) {
        this.tradeId = tradeId;
        this.marriageNo = marriageNo;
        this.name = name;
        this.idCardNo = idCardNo;
    }

    @Override
    public String toString() {
        return "MarriageLicenseBack{" +
                "tradeId='" + tradeId + '\'' +
                ", marriageNo='" + marriageNo + '\'' +
                ", name='" + name + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                '}';
    }
}
