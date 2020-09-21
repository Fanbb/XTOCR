package com.ocr.system.model;

/**
 * 火车票
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class RalTicketBack {
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
     * 车次
     */
    private String trainNumber;
    /**
     * 座位等级
     */
    private String seatLevel;
    /**
     * 金额
     */
    private String amt;

    public RalTicketBack() {
    }

    public RalTicketBack(String tradeId, String name, String date, String depStation, String terminus, String trainNumber, String seatLevel, String amt) {
        this.tradeId = tradeId;
        this.name = name;
        this.date = date;
        this.depStation = depStation;
        this.terminus = terminus;
        this.trainNumber = trainNumber;
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

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
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
        return "RalTicket{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", depStation='" + depStation + '\'' +
                ", terminus='" + terminus + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", seatLevel='" + seatLevel + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
