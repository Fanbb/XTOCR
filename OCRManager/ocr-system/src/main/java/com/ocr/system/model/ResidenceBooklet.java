package com.ocr.system.model;


import com.ocr.common.utils.StringUtils;

/**
 * 户口本
 * @author Jocker
 */
public class ResidenceBooklet {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 户籍地址
     */
    private String address;
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ResidenceBooklet(String tradeId, String nativePlace, String address, String imgType, String flag) {
        this.tradeId = tradeId;
        this.nativePlace = nativePlace;
        this.address = address;
        this.imgType = imgType;
        this.flag = flag;
    }

    public ResidenceBooklet() {
    }

    @Override
    public String toString() {
        return "ResidenceBooklet{" +
                "tradeId='" + tradeId + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", address='" + address + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getAddress())|| StringUtils.isEmpty(getNativePlace());
    }
}
