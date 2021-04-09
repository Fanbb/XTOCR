package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 营业执照
 * @author Jocker
 */
public class BusinessLicense {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 统一社会信用代码
     */
    private String socialCode;
    /**
     * 名称
     */
    private String companyName;
    /**
     * 注册资本
     */
    private String registeredCapital;
    /**
     * 法定代表人
     */
    private String legalPerson;
    /**
     * 住所
     */
    private String address;
    /**
     * 经营范围
     */
    private String businessScope;
    /**
     * 类型
     */
    private String vertical ;
    /**
     * 营业期限
     */
    private String businessTerm;
    /**
     * 成立日期
     */
    private String registerDate;
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;
    /**
     * 预警结果
     */
    private String riskFlag;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(String businessTerm) {
        this.businessTerm = businessTerm;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
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

    public String getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(String fiskFlag) {
        this.riskFlag = fiskFlag;
    }

    public BusinessLicense(String tradeId, String socialCode, String companyName, String registeredCapital, String legalPerson, String address, String businessScope, String vertical, String businessTerm, String registerDate, String imgType, String flag, String riskFlag) {
        this.tradeId = tradeId;
        this.socialCode = socialCode;
        this.companyName = companyName;
        this.registeredCapital = registeredCapital;
        this.legalPerson = legalPerson;
        this.address = address;
        this.businessScope = businessScope;
        this.vertical = vertical;
        this.businessTerm = businessTerm;
        this.registerDate = registerDate;
        this.imgType = imgType;
        this.flag = flag;
        this.riskFlag = riskFlag;
    }

    public BusinessLicense() {
    }

    @Override
    public String toString() {
        return "BusinessLicense{" +
                "tradeId='" + tradeId + '\'' +
                ", socialCode='" + socialCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", registeredCapital='" + registeredCapital + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", address='" + address + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", vertical='" + vertical + '\'' +
                ", businessTerm='" + businessTerm + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", riskFlag='" + riskFlag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getSocialCode())||StringUtils.isEmpty(getAddress())||StringUtils.isEmpty(getBusinessScope())||StringUtils.isEmpty(getBusinessTerm())||StringUtils.isEmpty(getCompanyName())||StringUtils.isEmpty(getLegalPerson())||StringUtils.isEmpty(getRegisterDate())||StringUtils.isEmpty(getRegisteredCapital())||StringUtils.isEmpty(getVertical());
    }
}
