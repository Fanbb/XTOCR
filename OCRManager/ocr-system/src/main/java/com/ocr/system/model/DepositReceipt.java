package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 存单
 * @author Jocker
 * @date 2020/5/25
 */
public class DepositReceipt {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 户名
     */
    private String name;
    /**
     * 账号
     */
    private String accNo;
    /**
     * 金额小写
     */
    private String amt;
    /**
     * 金额大写
     */
    private String amtCapital;
    /**
     * 存单号
     */
    private String depositNo;
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getAmtCapital() {
        return amtCapital;
    }

    public void setAmtCapital(String amtCapital) {
        this.amtCapital = amtCapital;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public DepositReceipt(String tradeId,String name, String accNo, String amt, String amtCapital,String depositNo, String imgType,String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.accNo = accNo;
        this.amt = amt;
        this.amtCapital = amtCapital;
        this.depositNo = depositNo;
        this.imgType = imgType;
        this.flag = flag;
    }

    public DepositReceipt() {
    }

    @Override
    public String toString() {
        return "DepositReceipt{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", accNo='" + accNo + '\'' +
                ", amt='" + amt + '\'' +
                ", amtCapital='" + amtCapital + '\'' +
                ", depositNo='" + depositNo + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getDepositNo() {
        return depositNo;
    }

    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
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
        return StringUtils.isEmpty(getDepositNo()) || StringUtils.isEmpty(getAccNo()) || StringUtils.isEmpty(getAmt()) || StringUtils.isEmpty(getAmtCapital()) || StringUtils.isEmpty(getName());
    }
}
