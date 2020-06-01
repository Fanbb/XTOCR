package com.ocr.web.controller.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("非加密访问参数实体1")
public class OCRDiscernEntity {
    @ApiModelProperty("影像渠道号")
    private String channelCode;
    @ApiModelProperty("图片url")
    private String imgUrl;
    @ApiModelProperty("图片Base64")
    private String imgStr;
    @ApiModelProperty("图片类型")
    private String imgType;

    public OCRDiscernEntity() {

    }

    public OCRDiscernEntity(String channelCode, String imgUrl, String imgStr, String imgType) {
        this.channelCode = channelCode;
        this.imgUrl = imgUrl;
        this.imgStr = imgStr;
        this.imgType = imgType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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