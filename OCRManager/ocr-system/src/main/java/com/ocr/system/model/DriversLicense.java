package com.ocr.system.model;


/**
 * 驾驶证
 * @author Jocker
 */
public class DriversLicense {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 住址
     */
    private String address;
    /**
     * 出生日期
     */
    private String birthDate;
    /**
     * 初次领证日期
     */
    private String issueDate;
    /**
     * 准驾车型
     */
    private String drivingType;
    /**
     * 有效期开始时间
     */
    private String startDate;
    /**
     * 有效期截止时间
     */
    private String endDate;
    /**
     * 证号
     */
    private String cardNo;
    /**
     * 档案编号
     */
    private String fileNumber;
    /**
     * 记录
     */
    private String record;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDrivingType() {
        return drivingType;
    }

    public void setDrivingType(String drivingType) {
        this.drivingType = drivingType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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

    public DriversLicense(String tradeId, String name, String sex, String nationality, String address, String birthDate, String issueDate, String drivingType, String startDate, String endDate, String cardNo, String fileNumber, String record, String imgType, String flag) {
        this.tradeId = tradeId;
        this.name = name;
        this.sex = sex;
        this.nationality = nationality;
        this.address = address;
        this.birthDate = birthDate;
        this.issueDate = issueDate;
        this.drivingType = drivingType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cardNo = cardNo;
        this.fileNumber = fileNumber;
        this.record = record;
        this.imgType = imgType;
        this.flag = flag;
    }

    public DriversLicense() {
    }

    @Override
    public String toString() {
        return "DriversLicense{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nationality='" + nationality + '\'' +
                ", address='" + address + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", drivingType='" + drivingType + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                ", record='" + record + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
