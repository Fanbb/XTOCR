package com.ocr.system.model;

public class EchartsEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * 数值
     */
    private String value;

    public EchartsEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public EchartsEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
