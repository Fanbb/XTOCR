package com.ocr.web.controller.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel("更改识别结果参数实体")
public class DiscernResult {
    @NotBlank(message = "渠道号")
    @ApiModelProperty("渠道号")
    private String channelCode;
    @NotBlank(message = "批次号不能为空")
    @ApiModelProperty("批次号")
    private String batchNumber;
    @NotBlank(message = "唯一标识不能为空")
    @ApiModelProperty("唯一标识(多个以逗号分隔)")
    private String identificationCode;
    @NotBlank(message = "图片类型不能为空")
    @ApiModelProperty("图片类型")
    private String imgType;

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

    public DiscernResult(@NotBlank(message = "渠道号") String channelCode, @NotBlank(message = "批次号不能为空") String batchNumber, @NotBlank(message = "唯一标识不能为空") String identificationCode, @NotBlank(message = "图片类型不能为空") String imgType) {
        this.channelCode = channelCode;
        this.batchNumber = batchNumber;
        this.identificationCode = identificationCode;
        this.imgType = imgType;
    }
}
