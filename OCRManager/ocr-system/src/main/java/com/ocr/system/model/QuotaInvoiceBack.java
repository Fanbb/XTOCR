package com.ocr.system.model;

/**
 * 通用定额发票
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class QuotaInvoiceBack {
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

    public QuotaInvoiceBack() {
    }

    public QuotaInvoiceBack(String tradeId, String invoiceCode, String invoiceNumber, String amt) {
        this.tradeId = tradeId;
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.amt = amt;
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

    @Override
    public String toString() {
        return "TollInvoice{" +
                "tradeId='" + tradeId + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
