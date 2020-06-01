package com.ocr.web.controller.system;

import java.util.List;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.UUIDEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ocr.common.annotation.Log;
import com.ocr.common.enums.BusinessType;
import com.ocr.system.domain.Channel;
import com.ocr.system.service.IChannelService;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.poi.ExcelUtil;

/**
 * 渠道 信息操作处理
 *
 * @author ocr
 * @date 2020-05-19
 */
@Controller
@RequestMapping("/system/channel")
public class ChannelController extends BaseController {
    private String prefix = "system/channel";

    @Autowired
    private IChannelService channelService;

    @RequiresPermissions("system:channel:view")
    @GetMapping()
    public String channel() {
        return prefix + "/channel";
    }

    /**
     * 查询渠道列表
     */
    @RequiresPermissions("system:channel:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Channel channel) {
        startPage();
        List<Channel> list = channelService.selectChannelList(channel);
        return getDataTable(list);
    }


    /**
     * 导出渠道列表
     */
    @Log(title = "导出渠道", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:channel:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Channel channel) {
        List<Channel> list = channelService.selectChannelList(channel);
        ExcelUtil<Channel> util = new ExcelUtil<Channel>(Channel.class);
        return util.exportExcel(list, "channel");
    }

    /**
     * 新增渠道
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道
     */
    @RequiresPermissions("system:channel:add")
    @Log(title = "渠道", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Channel channel) {
        channel.setId(UUID.randomUUID().toString());
        return toAjax(channelService.insertChannel(channel));
    }

    /**
     * 修改渠道
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        Channel channel = channelService.selectChannelById(id);
        mmap.put("channel", channel);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道
     */
    @RequiresPermissions("system:channel:edit")
    @Log(title = "渠道", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Channel channel) {
        return toAjax(channelService.updateChannel(channel));
    }

    /**
     * 删除渠道
     */
    @RequiresPermissions("system:channel:remove")
    @Log(title = "渠道", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(channelService.deleteChannelByIds(ids));
    }

}
