package com.ocr.web.controller.system;

import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.utils.poi.ExcelUtil;
import com.ocr.system.domain.OcrTotalReport;
import com.ocr.system.domain.OcrTradeView;
import com.ocr.system.service.IOcrTotalReportService;
import com.ocr.system.service.IOcrTradeViewService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 识别流水汇总 信息操作处理
 *
 * @author ocr
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/system/ocrTotalReport")
public class OcrTotalReportController extends BaseController {
    private String prefix = "system/ocrTotalReport";

    @Autowired
    private IOcrTotalReportService iOcrTotalReportService;

    @RequiresPermissions("system:ocrTotalReport:view")
    @GetMapping()
    public String ocrTrade() {
        return prefix + "/ocrTotalReport";
    }

    /**
     * 查询识别流水列表
     */
    @RequiresPermissions("system:ocrTotalReport:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OcrTotalReport ocrTotalReport) {
        startPage();
        List<OcrTotalReport> list = iOcrTotalReportService.selectOcrTradeViewList(ocrTotalReport);
        return getDataTable(list);
    }


}
