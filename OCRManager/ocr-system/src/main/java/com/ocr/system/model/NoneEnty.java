package com.ocr.system.model;

/**
 * 无效识别类型流水返回结果
 */
public class NoneEnty {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 图片类型
     */
    private String imgType;

    public NoneEnty() {
    }

    public NoneEnty(String tradeId, String imgType) {
        this.tradeId = tradeId;
        this.imgType = imgType;
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
}
