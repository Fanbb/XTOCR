package com.ocr.system.model;

/**
 * 航空运输电子客票行程单
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class ItineraryBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 旅客姓名
     */
    private String name;
    /**
     * 日期
     */
    private String date;
    /**
     * 出发站
     */
    private String depStation;
    /**
     * 终点站
     */
    private String terminus;
    /**
     * 航班
     */
    private String flight;
    /**
     * 座位等级
     */
    private String seatLevel;
    /**
     * 金额
     */
    private String amt;

    public ItineraryBack() {
    }

    public ItineraryBack(String tradeId, String name, String date, String depStation, String terminus, String flight, String seatLevel, String amt) {
        this.tradeId = tradeId;
        this.name = name;
        this.date = date;
        this.depStation = depStation;
        this.terminus = terminus;
        this.flight = flight;
        this.seatLevel = seatLevel;
        this.amt = amt;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepStation() {
        return depStation;
    }

    public void setDepStation(String depStation) {
        this.depStation = depStation;
    }

    public String getTerminus() {
        return terminus;
    }

    public void setTerminus(String terminus) {
        this.terminus = terminus;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getSeatLevel() {
        return seatLevel;
    }

    public void setSeatLevel(String seatLevel) {
        this.seatLevel = seatLevel;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "ItineraryBack{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", depStation='" + depStation + '\'' +
                ", terminus='" + terminus + '\'' +
                ", flight='" + flight + '\'' +
                ", seatLevel='" + seatLevel + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
