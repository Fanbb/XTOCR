package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

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
    /**
     * 识别结果
     */
    private String flag;

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

    public BankCard(String tradeId,String bankCardNo, String imgType,String flag) {
        this.tradeId = tradeId;
        this.bankCardNo = bankCardNo;
        this.imgType = imgType;
        this.flag = flag;
    }

    public BankCard() {
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "tradeId='" + tradeId + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
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

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getBankCardNo());
    }
}
