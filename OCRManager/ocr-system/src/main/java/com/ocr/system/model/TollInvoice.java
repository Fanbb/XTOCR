package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 通行费发票
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class TollInvoice {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNumber;
    /**
     * 金额
     */
    private String amt;
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;
    /**
     * 识别结果
     */
    private String riskFlag;


    public TollInvoice() {
    }

    public TollInvoice(String tradeId, String invoiceCode, String invoiceNumber, String amt, String imgType, String flag, String riskFlag) {
        this.tradeId = tradeId;
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.amt = amt;
        this.imgType = imgType;
        this.flag = flag;
        this.riskFlag = riskFlag;
    }

    public String getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(String riskFlag) {
        this.riskFlag = riskFlag;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "TollInvoice{" +
                "tradeId='" + tradeId + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", amt='" + amt + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", riskFlag='" + riskFlag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getInvoiceCode())||StringUtils.isEmpty(getInvoiceNumber())||StringUtils.isEmpty(getAmt());
    }
}
