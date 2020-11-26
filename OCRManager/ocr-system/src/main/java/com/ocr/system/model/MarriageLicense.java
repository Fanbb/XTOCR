package com.ocr.system.model;


import com.ocr.common.utils.StringUtils;

/**
 * 结婚证
 * @author Jocker
 */
public class MarriageLicense {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 结婚证号
     */
    private String marriageNo;
    /**
     * 配偶姓名
     */
    private String name;
    /**
     * 配偶身份证号
     */
    private String idCardNo;
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

    public String getMarriageNo() {
        return marriageNo;
    }

    public void setMarriageNo(String marriageNo) {
        this.marriageNo = marriageNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
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

    public void setRiskFlag(String riskFlag) {
        this.riskFlag = riskFlag;
    }

    public MarriageLicense() {
    }

    public MarriageLicense(String tradeId, String marriageNo, String name, String idCardNo, String imgType, String flag, String riskFlag) {
        this.tradeId = tradeId;
        this.marriageNo = marriageNo;
        this.name = name;
        this.idCardNo = idCardNo;
        this.imgType = imgType;
        this.flag = flag;
        this.riskFlag = riskFlag;
    }

    @Override
    public String toString() {
        return "MarriageLicense{" +
                "tradeId='" + tradeId + '\'' +
                ", marriageNo='" + marriageNo + '\'' +
                ", name='" + name + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", riskFlag='" + riskFlag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getMarriageNo())|| StringUtils.isEmpty(getIdCardNo())||StringUtils.isEmpty(getName());
    }
}
