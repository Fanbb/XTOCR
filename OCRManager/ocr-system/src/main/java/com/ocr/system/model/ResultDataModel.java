package com.ocr.system.model;

public class ResultDataModel {
    private String batchNumber;
    private String identificationCode;
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

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public ResultDataModel(String batchNumber, String identificationCode, Object resultData) {
        this.batchNumber = batchNumber;
        this.identificationCode = identificationCode;
        this.resultData = resultData;
    }

    public ResultDataModel() {
    }
}
