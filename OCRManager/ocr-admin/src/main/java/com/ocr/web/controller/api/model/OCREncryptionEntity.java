package com.ocr.web.controller.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("加密访问参数实体1")
public class OCREncryptionEntity {

    @ApiModelProperty("影像渠道")
    private String type;
    @ApiModelProperty("图片url")
    private String imgUrl;
    @ApiModelProperty("图片Base64")
    private String imgStr;
    @ApiModelProperty("图片类型")
    private String imgType;
    @ApiModelProperty("参数验证")
    private String sign;

    public OCREncryptionEntity() {

    }

    public OCREncryptionEntity(String type, String imgUrl, String imgStr, String imgType, String sign) {
        this.type = type;
        this.imgUrl = imgUrl;
        this.imgStr = imgStr;
        this.imgType = imgType;
        this.sign = sign;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

}
