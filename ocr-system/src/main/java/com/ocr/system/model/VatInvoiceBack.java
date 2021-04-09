package com.ocr.system.model;

/**
 * 增值税专用发票
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
     * 发票代码
     */
    private String Code;
    /**
     * 发票号码
     */
    private String Number;
    /**
     * 开票日期
     */
    private String IssuedDate;
    /**
     *购买方名称
     */
    private String PurchaserName;
    /**
     *购买方纳税人识别号
     */
    private String PurchaserTaxpayerNum;
    /**
     *金额
     */
    private String ValueAddedTax;
    /**
     *销售方名称
     */
    private String SellerName;
    /**
     *销售方纳税人识别号
     */
    private String SellerTaxpayerNum;
    /**
     *服务名称
     */
    private String Subjects;


    public VatInvoiceBack() {
    }


    public VatInvoiceBack(String tradeId, String code, String number, String issuedDate, String purchaserName, String purchaserTaxpayerNum, String valueAddedTax, String sellerName, String sellerTaxpayerNum, String subjects) {
        this.tradeId = tradeId;
        Code = code;
        Number = number;
        IssuedDate = issuedDate;
        PurchaserName = purchaserName;
        PurchaserTaxpayerNum = purchaserTaxpayerNum;
        ValueAddedTax = valueAddedTax;
        SellerName = sellerName;
        SellerTaxpayerNum = sellerTaxpayerNum;
        Subjects = subjects;
    }

    public String getSubjects() {
        return Subjects;
    }

    public void setSubjects(String subjects) {
        Subjects = subjects;
    }
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getIssuedDate() {
        return IssuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        IssuedDate = issuedDate;
    }

    public String getPurchaserName() {
        return PurchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        PurchaserName = purchaserName;
    }

    public String getPurchaserTaxpayerNum() {
        return PurchaserTaxpayerNum;
    }

    public void setPurchaserTaxpayerNum(String purchaserTaxpayerNum) {
        PurchaserTaxpayerNum = purchaserTaxpayerNum;
    }

    public String getValueAddedTax() {
        return ValueAddedTax;
    }

    public void setValueAddedTax(String valueAddedTax) {
        ValueAddedTax = valueAddedTax;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerTaxpayerNum() {
        return SellerTaxpayerNum;
    }

    public void setSellerTaxpayerNum(String sellerTaxpayerNum) {
        SellerTaxpayerNum = sellerTaxpayerNum;
    }


    @Override
    public String toString() {
        return "VatInvoiceBack{" +
                "tradeId='" + tradeId + '\'' +
                ", Code='" + Code + '\'' +
                ", Number='" + Number + '\'' +
                ", IssuedDate='" + IssuedDate + '\'' +
                ", PurchaserName='" + PurchaserName + '\'' +
                ", PurchaserTaxpayerNum='" + PurchaserTaxpayerNum + '\'' +
                ", ValueAddedTax='" + ValueAddedTax + '\'' +
                ", SellerName='" + SellerName + '\'' +
                ", SellerTaxpayerNum='" + SellerTaxpayerNum + '\'' +
                ", Subjects='" + Subjects + '\'' +
                '}';
    }
}
