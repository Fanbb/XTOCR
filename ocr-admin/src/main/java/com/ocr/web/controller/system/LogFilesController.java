package com.ocr.web.controller.system;

import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.text.Convert;
import com.ocr.common.enums.BusinessType;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.model.LogFile;
import com.ocr.system.service.ISysDeptService;
import com.ocr.system.service.LogFilesService;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/system/logFile")
public class LogFilesController extends BaseController {

    private String prefix = "system/log";

    @Value("${ocr.logFilePath}")
    private String logFilePath;

    @Autowired
    private LogFilesService logFilesService;

    @RequiresPermissions("system:log:view")
    @GetMapping()
    public String dept()
    {
        return prefix + "/log";
    }

    /**
     * 查询识别影像列表
     */
    @RequiresPermissions("system:log:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        startPage();
        List<LogFile> list = getFileList(logFilePath);
        return getDataTable(list);
    }

    /**
     * 日志批量下载
     */
    @Log(title = "日志批量下载", businessType = BusinessType.EXPORTLOGFILES)
    @GetMapping("/batchLogFiles")
    @ResponseBody
    public void batchLogFiles(HttpServletResponse response, String tables) throws IOException
    {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = logFilesService.generatorCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     *
     * @param response
     * @param data
     * @throws IOException
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"logFiles.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    public static List<LogFile> getFileList(String path) {
        List list = new ArrayList<>();
        try {
            File file = new File(path);
            File[] filelist = file.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                LogFile logFile = new LogFile();
                logFile.setFileName(filelist[i].getName());
                list.add(logFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
