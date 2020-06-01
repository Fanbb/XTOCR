package com.ocr.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ocr.common.annotation.Log;
import com.ocr.common.enums.BusinessType;
import com.ocr.system.domain.ChannelType;
import com.ocr.system.service.IChannelTypeService;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.poi.ExcelUtil;

/**
 * 渠道识别类型参数 信息操作处理
 *
 * @author ocr
 * @date 2020-05-29
 */
@Controller
@RequestMapping("/system/channelType")
public class ChannelTypeController extends BaseController {
    private String prefix = "system/channelType";

    @Autowired
    private IChannelTypeService channelTypeService;

    @RequiresPermissions("system:channelType:view")
    @GetMapping()
    public String channelType() {
        return prefix + "/channelType";
    }

    /**
     * 查询渠道识别类型参数列表
     */
    @RequiresPermissions("system:channelType:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ChannelType channelType) {
        startPage();
        List<ChannelType> list = channelTypeService.selectChannelTypeList(channelType);
        return getDataTable(list);
    }


    /**
     * 导出渠道识别类型参数列表
     */
    @RequiresPermissions("system:channelType:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ChannelType channelType) {
        List<ChannelType> list = channelTypeService.selectChannelTypeList(channelType);
        ExcelUtil<ChannelType> util = new ExcelUtil<ChannelType>(ChannelType.class);
        return util.exportExcel(list, "channelType");
    }

    /**
     * 新增渠道识别类型参数
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道识别类型参数
     */
    @RequiresPermissions("system:channelType:add")
    @Log(title = "渠道识别类型参数", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ChannelType channelType) {
        /**
         * 插入前进行判断
         */
        ChannelType channelType1 = channelTypeService.selectByNoAndType(channelType.getChannelCode(), channelType.getOcrType());
        if (null==channelType1){
            return error("该渠道的ocr识别类型已经添加");
        }
        return toAjax(channelTypeService.insertChannelType(channelType));
    }

    /**
     * 修改渠道识别类型参数
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        ChannelType channelType = channelTypeService.selectChannelTypeById(id);
        mmap.put("channelType", channelType);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道识别类型参数
     */
    @RequiresPermissions("system:channelType:edit")
    @Log(title = "渠道识别类型参数", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ChannelType channelType) {
        return toAjax(channelTypeService.updateChannelType(channelType));
    }

    /**
     * 删除渠道识别类型参数
     */
    @RequiresPermissions("system:channelType:remove")
    @Log(title = "渠道识别类型参数", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(channelTypeService.deleteChannelTypeByIds(ids));
    }

}
