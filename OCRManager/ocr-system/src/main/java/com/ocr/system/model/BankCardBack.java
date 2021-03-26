package com.ocr.system.model;

/**
 * 银行卡副例
 * @author Jocker
 * @date 2020/7/29
 */
public class BankCardBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 银行卡号
     */
    private String bankCardNo;

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public BankCardBack(String tradeId, String bankCardNo) {
        this.tradeId = tradeId;
        this.bankCardNo = bankCardNo;
    }

    public BankCardBack() {
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "tradeId='" + tradeId + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

}
