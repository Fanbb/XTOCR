package com.ocr.system.model;

/**
 * 银行卡
 * @author Jocker
 * @date 2020/5/25
 */
public class BankCard {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 银行卡号
     */
    private String bankCardNo;
    /**
     * 图片类型
     */
    private String imgType;

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public BankCard(String tradeId,String bankCardNo, String imgType) {
        this.tradeId = tradeId;
        this.bankCardNo = bankCardNo;
        this.imgType = imgType;
    }

    public BankCard() {
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "tradeId='" + tradeId + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                ", imgType='" + imgType + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
