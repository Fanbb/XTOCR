package com.ocr.system.model;

import com.ocr.common.utils.StringUtils;

public class GeneralText {
    /**
     * 流水ID
     */
    private String tradeId;

    /**
     * 图片类型
     */
    private String imgType;

    /**
     * 识别结果
     */
    private String flag;

    /**
     * 文本
     */
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "GeneralText{" +
                "tradeId='" + tradeId + '\'' +
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", test='" + text + '\'' +
                '}';
    }

    public boolean hasEmptyField() {
        return StringUtils.isEmpty(getText());
    }

}
