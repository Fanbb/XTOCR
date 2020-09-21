package com.ocr.system.model;

/**
 * 增值税发票
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class VatInvoiceBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 销货方名称
     */
    private String name;
    /**
     * 服务名称
     */
    private String serName;
    /**
     * 金额
     */
    private String amt;
    /**
     * 税额
     */
    private String taxAmt;

    public VatInvoiceBack() {
    }

    public VatInvoiceBack(String tradeId, String name, String serName, String amt, String taxAmt, String imgType, String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.serName = serName;
        this.amt = amt;
        this.taxAmt = taxAmt;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerName() {
        return serName;
    }

    public void setSerName(String serName) {
        this.serName = serName;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    @Override
    public String toString() {
        return "VatInvoice{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", serName='" + serName + '\'' +
                ", amt='" + amt + '\'' +
                ", taxAmt='" + taxAmt + '\'' +
                '}';
    }
}
