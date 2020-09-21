package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

/**
 * 房本
 * @author Jocker
 * @date 2020/5/25
 */
public class PremisesPermit {
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
    /**
     * 影像类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;

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

    public PremisesPermit() {
    }

    public PremisesPermit(String tradeId, String name, String certificateNo, String purpose, String location, String builtArea, String floorArea, String landUse, String structure, String imgType,String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.certificateNo = certificateNo;
        this.purpose = purpose;
        this.location = location;
        this.builtArea = builtArea;
        this.floorArea = floorArea;
        this.landUse = landUse;
        this.structure = structure;
        this.imgType = imgType;
        this.flag = flag;
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
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
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

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getBuiltArea())||StringUtils.isEmpty(getCertificateNo())|| StringUtils.isEmpty(getFloorArea())||StringUtils.isEmpty(getName())||StringUtils.isEmpty(getLocation())||StringUtils.isEmpty(getPurpose())||StringUtils.isEmpty(getLandUse())||StringUtils.isEmpty(getStructure());
    }
}
