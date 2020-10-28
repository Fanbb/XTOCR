package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 航空运输电子客票行程单
 *
 * @author Jocker
 * @date 2020/9/17
 */
public class Itinerary {
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
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;


    public Itinerary() {
    }

    public Itinerary(String tradeId, String name, String date, String startingStation, String endingStation, String flight, String seatLevel, String amt, String imgType, String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.date = date;
        this.startingStation = startingStation;
        this.endingStation = endingStation;
        this.flight = flight;
        this.seatLevel = seatLevel;
        this.amt = amt;
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
        return "Itinerary{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", startingStation='" + startingStation  + '\'' +
                ", endingStation='" + endingStation   + '\'' +
                ", flight='" + flight + '\'' +
                ", seatLevel='" + seatLevel + '\'' +
                ", amt='" + amt + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getName())||StringUtils.isEmpty(getDate())||StringUtils.isEmpty(getStartingStation())||StringUtils.isEmpty(getEndingStation())||StringUtils.isEmpty(getFlight())||StringUtils.isEmpty(getSeatLevel())||StringUtils.isEmpty(getAmt());
    }
}
