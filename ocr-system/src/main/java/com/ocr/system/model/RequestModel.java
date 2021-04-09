package com.ocr.system.model;

public class RequestModel {
    private String class_name;
    private String ocr_result;
    private String risk_flag;

    public RequestModel(String class_name, String ocr_result, String risk_flag) {
        this.class_name = class_name;
        this.ocr_result = ocr_result;
        this.risk_flag = risk_flag;
    }

    public RequestModel() {
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getOcr_result() {
        return ocr_result;
    }

    public void setOcr_result(String ocr_result) {
        this.ocr_result = ocr_result;
    }

    public String getRisk_flag() {
        return risk_flag;
    }

    public void setRisk_flag(String risk_flag) {
        this.risk_flag = risk_flag;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "class_name='" + class_name + '\'' +
                ", ocr_result='" + ocr_result + '\'' +
                ", risk_flag='" + risk_flag + '\'' +
                '}';
    }
}
