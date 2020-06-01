package com.ocr.system.model;

/**
 * 银行卡
 * @author Jocker
 * @date 2020/5/25
 */
public class BankCard {
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

    public BankCard(String bankCardNo, String imgType) {
        this.bankCardNo = bankCardNo;
        this.imgType = imgType;
    }

    public BankCard() {
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "bankCardNo='" + bankCardNo + '\'' +
                ", imgType='" + imgType + '\'' +
                '}';
    }
}
