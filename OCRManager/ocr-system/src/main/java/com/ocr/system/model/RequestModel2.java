package com.ocr.system.model;

public class RequestModel2 {
    private String path;
    private String image_result;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage_result() {
        return image_result;
    }

    public void setImage_result(String image_result) {
        this.image_result = image_result;
    }

    public RequestModel2() {
    }

    public RequestModel2(String path, String image_result) {
        this.path = path;
        this.image_result = image_result;
    }

    @Override
    public String toString() {
        return "RequestModel2{" +
                "path='" + path + '\'' +
                ", image_result='" + image_result + '\'' +
                '}';
    }
}
