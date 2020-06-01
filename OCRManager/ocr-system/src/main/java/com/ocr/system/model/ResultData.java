package com.ocr.system.model;

public class ResultData {
    private String msg;
    private String type;

    public ResultData(String msg, String type, Object data) {
        this.msg = msg;
        this.type = type;
        this.data = data;
    }

    private Object data;

    public ResultData() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
