package com.ocr.web.controller.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("影像平台接口访问参数实体")
public class DiscernResult {
    @ApiModelProperty("渠道号")
    private String channelCode;
    @ApiModelProperty("批次号")
    private String batchNumber;
    @ApiModelProperty("唯一标识(多个以逗号分隔)")
    private String identificationCode;
    @ApiModelProperty("图片类型")
    private String imgType;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String passWord;
    @ApiModelProperty("影像渠道")
    private String modelCode;
    @ApiModelProperty("影像创建日期")
    private String createDate;
    @ApiModelProperty("文档部名")
    private String filePartName;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public DiscernResult() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public DiscernResult(String channelCode, String batchNumber, String identificationCode, String imgType, String userName, String passWord, String modelCode, String createDate, String filePartName) {
        this.channelCode = channelCode;
        this.batchNumber = batchNumber;
        this.identificationCode = identificationCode;
        this.imgType = imgType;
        this.userName = userName;
        this.passWord = passWord;
        this.modelCode = modelCode;
        this.createDate = createDate;
        this.filePartName = filePartName;
    }

    public String getFilePartName() {
        return filePartName;
    }

    public void setFilePartName(String filePartName) {
        this.filePartName = filePartName;
    }
}
