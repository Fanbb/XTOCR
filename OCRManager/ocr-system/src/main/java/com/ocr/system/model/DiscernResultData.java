package com.ocr.system.model;

public class DiscernResultData {

    /**
     * 批次号
     */
    private String batchNumber;
    /**
     * 唯一标识
     */
    private String identificationCode;
    /**
     * 识别结果
     */
    private Object resultData;

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



    public DiscernResultData() {
    }

    public DiscernResultData(String batchNumber, String identificationCode, Object resultData) {
        this.batchNumber = batchNumber;
        this.identificationCode = identificationCode;
        this.resultData = resultData;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
