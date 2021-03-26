package com.ocr.system.model;

/**
 * 车牌号副例
 * @author Jocker
 */
public class PlateNumberBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 号牌号码
     */
    private String number;


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

    public PlateNumberBack(String tradeId, String number) {
        this.tradeId = tradeId;
        this.number = number;
    }

    public PlateNumberBack() {
    }

    @Override
    public String toString() {
        return "PlateNumberBack{" +
                "tradeId='" + tradeId + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
