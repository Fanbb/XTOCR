package com.ocr.system.model;

/**
 * 身份证正面副例
 *
 * @author Jocker
 * @date 2020/5/25
 */
public class IDCardFrontBack {

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
     * 民族
     */
    private String nation;
    /**
     * 地址
     */
    private String address;
    /**
     * 身份证号
     */
    private String idCardNo;
    /**
     * 出生年月
     */
    private String birthday;


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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public IDCardFrontBack(String tradeId, String name, String sex, String nation, String address, String idCardNo, String birthday, String imgType) {
        this.tradeId = tradeId;
        this.name = name;
        this.sex = sex;
        this.nation = nation;
        this.address = address;
        this.idCardNo = idCardNo;
        this.birthday = birthday;
    }

    public IDCardFrontBack() {
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "IDCardFrontBack{" +
                "tradeId='" + tradeId + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", address='" + address + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }

}
