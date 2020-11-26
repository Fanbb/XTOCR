package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 车牌号
 * @author Jocker
 */
public class PlateNumber {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 号牌号码
     */
    private String number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public PlateNumber(String tradeId, String number, String imgType, String flag, String riskFlag) {
        this.tradeId = tradeId;
        this.number = number;
        this.imgType = imgType;
        this.flag = flag;
        this.riskFlag = riskFlag;
    }

    public PlateNumber() {
    }

    @Override
    public String toString() {
        return "PlateNumber{" +
                "tradeId='" + tradeId + '\'' +
                ", number='" + number + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", riskFlag='" + riskFlag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getNumber());
    }
}
