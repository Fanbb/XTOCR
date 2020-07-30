package com.ocr.system.model;


/**
 * 户口本副例
 * @author Jocker
 */
public class ResidenceBookletBack {
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

    public ResidenceBookletBack(String tradeId, String nativePlace, String address) {
        this.tradeId = tradeId;
        this.nativePlace = nativePlace;
        this.address = address;
    }

    public ResidenceBookletBack() {
    }

    @Override
    public String toString() {
        return "ResidenceBookletBack{" +
                "tradeId='" + tradeId + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
