package com.ocr.web.controller.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("更改识别结果参数实体")
public class ModifyResult {
    @ApiModelProperty("流水ID")
    private String tradeId;

    public ModifyResult(String tradeId) {
        this.tradeId = tradeId;
    }

    public ModifyResult() {
    }


    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
