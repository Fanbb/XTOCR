package com.ocr.web.controller.system;

import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.utils.poi.ExcelUtil;
import com.ocr.system.domain.PlatStatusView;
import com.ocr.system.service.IPlatStatusViewService;
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
@RequestMapping("/system/platStatusView")
public class PlatStatusViewController extends BaseController {
    private String prefix = "system/platStatusView";

    @Autowired
    private IPlatStatusViewService iPlatStatusViewService;

    @RequiresPermissions("system:platStatusView:view")
    @GetMapping()
    public String ocrTrade() {
        return prefix + "/platStatusView";
    }

    /**
     * 查询识别流水列表
     */
    @RequiresPermissions("system:platStatusView:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PlatStatusView platStatusView) {
        startPage();
        List<PlatStatusView> list = iPlatStatusViewService.selectPlatStatusViewList(platStatusView);
        return getDataTable(list);
    }

    /**
     * 导出识别流水列表
     */
    @RequiresPermissions("system:platStatusView:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PlatStatusView platStatusView) {
        List<PlatStatusView> list = iPlatStatusViewService.selectPlatStatusViewList(platStatusView);
        ExcelUtil<PlatStatusView> util = new ExcelUtil<PlatStatusView>(PlatStatusView.class);
        return util.exportExcel(list, "platStatusView");
    }

}
