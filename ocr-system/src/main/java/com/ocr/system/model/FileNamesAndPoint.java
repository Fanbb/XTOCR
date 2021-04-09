package com.ocr.system.model;

/**
 * 影像对应路径实体
 * @author Jocker
 */
public class FileNamesAndPoint {
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 影像唯一标识
     */
    private String identificationCode;

    public FileNamesAndPoint() {
    }

    public FileNamesAndPoint(String filePath, String identificationCode) {
        this.filePath = filePath;
        this.identificationCode = identificationCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }



}
