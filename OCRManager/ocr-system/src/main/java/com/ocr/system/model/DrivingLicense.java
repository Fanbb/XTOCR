package com.ocr.system.model;

/**
 * 行驶证
 * @author Jocker
 */
public class DrivingLicense {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 号牌号码
     */
    private String plateNumber;
    /**
     * 车辆类型
     */
    private String vehicleType;
    /**
     * 所有人
     */
    private String owner;
    /**
     * 住址
     */
    private String address;
    /**
     * 使用性质
     */
    private String useNature;
    /**
     * 品牌型号
     */
    private String brandModel;
    /**
     * 识别代码
     */
    private String identifyCode;
    /**
     * 发动机号
     */
    private String engineNumber;
    /**
     * 注册日期
     */
    private String registrationDate;
    /**
     * 发证日期
     */
    private String issueDate;
    /**
     * 发证单位
     */
    private String issueUnit;
    /**
     * 档案编号
     */
    private String fileNumber;
    /**
     * 核定载人数
     */
    private String number;
    /**
     * 总质量
     */
    private String totalMass;
    /**
     * 整备质量
     */
    private String unladenMass;
    /**
     * 核定载质量
     */
    private String ratifiedMass;
    /**
     * 外廓尺寸
     */
    private String gabarite;
    /**
     * 准牵引总质量
     */
    private String tractionMass;
    /**
     * 备注
     */
    private String remark;
    /**
     * 检验记录
     */
    private String inspectionRecord;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUseNature() {
        return useNature;
    }

    public void setUseNature(String useNature) {
        this.useNature = useNature;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueUnit() {
        return issueUnit;
    }

    public void setIssueUnit(String issueUnit) {
        this.issueUnit = issueUnit;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotalMass() {
        return totalMass;
    }

    public void setTotalMass(String totalMass) {
        this.totalMass = totalMass;
    }

    public String getUnladenMass() {
        return unladenMass;
    }

    public void setUnladenMass(String unladenMass) {
        this.unladenMass = unladenMass;
    }

    public String getRatifiedMass() {
        return ratifiedMass;
    }

    public void setRatifiedMass(String ratifiedMass) {
        this.ratifiedMass = ratifiedMass;
    }

    public String getGabarite() {
        return gabarite;
    }

    public void setGabarite(String gabarite) {
        this.gabarite = gabarite;
    }

    public String getTractionMass() {
        return tractionMass;
    }

    public void setTractionMass(String tractionMass) {
        this.tractionMass = tractionMass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInspectionRecord() {
        return inspectionRecord;
    }

    public void setInspectionRecord(String inspectionRecord) {
        this.inspectionRecord = inspectionRecord;
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

    public DrivingLicense(String tradeId, String plateNumber, String vehicleType, String owner, String address, String useNature, String brandModel, String identifyCode, String engineNumber, String registrationDate, String issueDate, String issueUnit, String fileNumber, String number, String totalMass, String unladenMass, String ratifiedMass, String gabarite, String tractionMass, String remark, String inspectionRecord, String imgType, String flag) {
        this.tradeId = tradeId;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.owner = owner;
        this.address = address;
        this.useNature = useNature;
        this.brandModel = brandModel;
        this.identifyCode = identifyCode;
        this.engineNumber = engineNumber;
        this.registrationDate = registrationDate;
        this.issueDate = issueDate;
        this.issueUnit = issueUnit;
        this.fileNumber = fileNumber;
        this.number = number;
        this.totalMass = totalMass;
        this.unladenMass = unladenMass;
        this.ratifiedMass = ratifiedMass;
        this.gabarite = gabarite;
        this.tractionMass = tractionMass;
        this.remark = remark;
        this.inspectionRecord = inspectionRecord;
        this.imgType = imgType;
        this.flag = flag;
    }

    public DrivingLicense() {
    }

    @Override
    public String toString() {
        return "DrivingLicense{" +
                "tradeId='" + tradeId + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", owner='" + owner + '\'' +
                ", address='" + address + '\'' +
                ", useNature='" + useNature + '\'' +
                ", brandModel='" + brandModel + '\'' +
                ", identifyCode='" + identifyCode + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", issueUnit='" + issueUnit + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                ", number='" + number + '\'' +
                ", totalMass='" + totalMass + '\'' +
                ", unladenMass='" + unladenMass + '\'' +
                ", ratifiedMass='" + ratifiedMass + '\'' +
                ", gabarite='" + gabarite + '\'' +
                ", tractionMass='" + tractionMass + '\'' +
                ", remark='" + remark + '\'' +
                ", inspectionRecord='" + inspectionRecord + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
