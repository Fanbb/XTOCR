package com.ocr.system.model;

/**
 * 存单副例
 * @author Jocker
 * @date 2020/7/29
 */
public class DepositReceiptBack {
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

    public DepositReceiptBack(String tradeId, String name, String accNo, String amt, String amtCapital, String depositNo) {
        this.tradeId = tradeId;
        this.name = name;
        this.accNo = accNo;
        this.amt = amt;
        this.amtCapital = amtCapital;
        this.depositNo = depositNo;
    }

    public DepositReceiptBack() {
    }

    @Override
    public String toString() {
        return "DepositReceiptBack{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", accNo='" + accNo + '\'' +
                ", amt='" + amt + '\'' +
                ", amtCapital='" + amtCapital + '\'' +
                ", depositNo='" + depositNo + '\'' +
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
}
