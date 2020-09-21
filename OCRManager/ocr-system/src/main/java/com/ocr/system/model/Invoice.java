package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 普通发票
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class Invoice {

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
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;


    public Invoice() {
    }

    public Invoice(String tradeId, String name, String serName, String amt, String taxAmt, String imgType, String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.serName = serName;
        this.amt = amt;
        this.taxAmt = taxAmt;
        this.imgType = imgType;
        this.flag = flag;
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
        return "VatInvoice{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", serName='" + serName + '\'' +
                ", amt='" + amt + '\'' +
                ", taxAmt='" + taxAmt + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getName())||StringUtils.isEmpty(getSerName())||StringUtils.isEmpty(getAmt())||StringUtils.isEmpty(getTaxAmt());
    }
}
