package com.ocr.web.controller.demo.controller;

import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.file.FileUploadUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class DemoTest {
    @GetMapping(value = "/upload")
    public String autocomplete() {
        System.out.println("test");
        return "/demo/form/upload";
    }

    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) throws IOException {
        //返回给前台
        AjaxResult results= AjaxResult.success("success");
        System.out.println(request.getParameter("username"));
        System.out.println(request.getParameter("password"));

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String fileName = null;
                fileName = FileUploadUtils.upload_2(files[i]);//FileUploadUtils.upload_OCR(files[i]);
                    //e.printStackTrace();
                System.out.println("++++++++++++++++++++++++++++"+fileName);
            }
        }

        return results;
    }
}
