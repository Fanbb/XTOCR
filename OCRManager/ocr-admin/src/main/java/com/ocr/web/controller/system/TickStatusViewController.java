package com.ocr.web.controller.system;

import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.utils.poi.ExcelUtil;
import com.ocr.system.domain.TickStatusView;
import com.ocr.system.service.ITickStatusViewService;
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
@RequestMapping("/system/tickStatusView")
public class TickStatusViewController extends BaseController {
    private String prefix = "system/tickStatusView";

    @Autowired
    private ITickStatusViewService iTickStatusViewService;

    @RequiresPermissions("system:tickStatusView:view")
    @GetMapping()
    public String ocrTrade() {
        return prefix + "/tickStatusView";
    }

    /**
     * 查询识别流水列表
     */
    @RequiresPermissions("system:tickStatusView:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TickStatusView tickStatusView) {
        startPage();
        List<TickStatusView> list = iTickStatusViewService.selectTickStatusViewwList(tickStatusView);
        return getDataTable(list);
    }

    /**
     * 导出识别流水列表
     */
    @RequiresPermissions("system:tickStatusView:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TickStatusView tickStatusView) {
        List<TickStatusView> list = iTickStatusViewService.selectTickStatusViewwList(tickStatusView);
        ExcelUtil<TickStatusView> util = new ExcelUtil<TickStatusView>(TickStatusView.class);
        return util.exportExcel(list, "tickStatusView");
    }

}
