package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 通行费
 * @author Jocker
 * @date 2021/3/1
 */
public class TongXingBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 发票代码
     */
    private String invoiceCode ;
    /**
     *发票号码
     */
    private String 	invoiceNumber;
    /**
     *金额
     */
    private String amt;

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

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public TongXingBack() {
    }

    public TongXingBack(String tradeId, String invoiceCode, String invoiceNumber, String amt) {
        this.tradeId = tradeId;
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "TongXingBack{" +
                "tradeId='" + tradeId + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
