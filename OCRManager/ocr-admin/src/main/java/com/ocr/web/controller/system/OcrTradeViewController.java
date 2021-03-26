package com.ocr.web.controller.system;

import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.utils.poi.ExcelUtil;
import com.ocr.system.domain.OcrTradeView;
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
 * 识别流水 信息操作处理
 *
 * @author ocr
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/system/ocrTradeView")
public class OcrTradeViewController extends BaseController {
    private String prefix = "system/ocrTradeView";

    @Autowired
    private IOcrTradeViewService iOcrTradeViewService;

    @RequiresPermissions("system:ocrTradeView:view")
    @GetMapping()
    public String ocrTrade() {
        return prefix + "/ocrTradeView";
    }

    /**
     * 查询识别流水列表
     */
    @RequiresPermissions("system:ocrTradeView:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OcrTradeView ocrTradeView) {
        startPage();
        List<OcrTradeView> list = iOcrTradeViewService.selectOcrTradeViewList(ocrTradeView);
        return getDataTable(list);
    }

    /**
     * 导出识别流水列表
     */
    @RequiresPermissions("system:ocrTradeView:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OcrTradeView ocrTradeView) {
        List<OcrTradeView> list = iOcrTradeViewService.selectOcrTradeViewList(ocrTradeView);
        ExcelUtil<OcrTradeView> util = new ExcelUtil<OcrTradeView>(OcrTradeView.class);
        return util.exportExcel(list, "ocrTradeView");
    }

}
