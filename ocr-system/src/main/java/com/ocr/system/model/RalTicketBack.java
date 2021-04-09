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
    private String startingStation;
    /**
     * 终点站
     */
    private String endingStation;
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

    public RalTicketBack(String tradeId, String name, String date, String startingStation, String endingStation, String trainNumber, String seatLevel, String amt) {
        this.tradeId = tradeId;
        this.name = name;
        this.date = date;
        this.startingStation = startingStation;
        this.endingStation = endingStation;
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

    public String getStartingStation() {
        return startingStation;
    }

    public void setStartingStation(String startingStation) {
        this.startingStation = startingStation;
    }

    public String getEndingStation() {
        return endingStation;
    }

    public void setEndingStation(String endingStation) {
        this.endingStation = endingStation;
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
        return "RalTicketBack{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", depStation='" + startingStation + '\'' +
                ", terminus='" + endingStation + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", seatLevel='" + seatLevel + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }

}
