package com.ocr.system.model;

/**
 * 房本副例
 * @author Jocker
 * @date 2020/5/25
 */
public class PremisesPermitBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 姓名
     */
    private String name;

    /**
     * 产权证号
     */
    private String certificateNo;
    /**
     * 房屋用途
     */
    private String purpose;
    /**
     * 房屋位置
     */
    private String location;
    /**
     * 建筑面积
     */
    private String builtArea;
    /**
     *套内建筑面积
     */
    private String floorArea;
    /**
     * 土地使用方式
     */
    private String landUse;
    /**
     * 房屋结构
     */
    private String structure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuiltArea() {
        return builtArea;
    }

    public void setBuiltArea(String builtArea) {
        this.builtArea = builtArea;
    }

    public String getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(String floorArea) {
        this.floorArea = floorArea;
    }

    public String getLandUse() {
        return landUse;
    }

    public void setLandUse(String landUse) {
        this.landUse = landUse;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public PremisesPermitBack() {
    }

    public PremisesPermitBack(String tradeId, String name, String certificateNo, String purpose, String location, String builtArea, String floorArea, String landUse, String structure) {
        this.tradeId = tradeId;
        this.name = name;
        this.certificateNo = certificateNo;
        this.purpose = purpose;
        this.location = location;
        this.builtArea = builtArea;
        this.floorArea = floorArea;
        this.landUse = landUse;
        this.structure = structure;
    }

    @Override
    public String toString() {
        return "PremisesPermit{" +
                "tradeId='" + tradeId + '\'' +
                "name='" + name + '\'' +
                ", certificateNo='" + certificateNo + '\'' +
                ", purpose='" + purpose + '\'' +
                ", location='" + location + '\'' +
                ", builtArea='" + builtArea + '\'' +
                ", floorArea='" + floorArea + '\'' +
                ", landUse='" + landUse + '\'' +
                ", structure='" + structure + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

}
