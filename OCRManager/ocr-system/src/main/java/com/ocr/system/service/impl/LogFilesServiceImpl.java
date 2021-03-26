package com.ocr.system.service.impl;

import com.ocr.common.constant.Constants;
import com.ocr.common.utils.file.ImageBase64;
import com.ocr.system.service.LogFilesService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LogFilesServiceImpl implements LogFilesService {
    @Value("${ocr.logFilePath}")
    private String logFilePath;

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            try {
                // 添加到zip
                byte[] buffer = ImageBase64.getByte(new File(logFilePath+"/"+tableName));

                zip.putNextEntry(new ZipEntry(tableName));
                IOUtils.write(new String(buffer), zip, Constants.UTF8);
                IOUtils.closeQuietly();
                zip.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


}
