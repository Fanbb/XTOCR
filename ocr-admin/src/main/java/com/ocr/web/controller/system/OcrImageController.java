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
import com.ocr.system.domain.OcrImage;
import com.ocr.system.service.IOcrImageService;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.poi.ExcelUtil;

/**
 * 识别影像 信息操作处理
 * 
 * @author ocr
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/system/ocrImage")
public class OcrImageController extends BaseController
{
    private String prefix = "system/ocrImage";
	
	@Autowired
	private IOcrImageService ocrImageService;
	
	@RequiresPermissions("system:ocrImage:view")
	@GetMapping()
	public String ocrImage()
	{
	    return prefix + "/ocrImage";
	}
	
	/**
	 * 查询识别影像列表
	 */
	@RequiresPermissions("system:ocrImage:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OcrImage ocrImage)
	{
		startPage();
        List<OcrImage> list = ocrImageService.selectOcrImageList(ocrImage);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出识别影像列表
	 */
	@RequiresPermissions("system:ocrImage:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OcrImage ocrImage)
    {
    	List<OcrImage> list = ocrImageService.selectOcrImageList(ocrImage);
        ExcelUtil<OcrImage> util = new ExcelUtil<OcrImage>(OcrImage.class);
        return util.exportExcel(list, "ocrImage");
    }
	
	/**
	 * 新增识别影像
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存识别影像
	 */
	@RequiresPermissions("system:ocrImage:add")
	@Log(title = "识别影像", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OcrImage ocrImage)
	{		
		return toAjax(ocrImageService.insertOcrImage(ocrImage));
	}

	/**
	 * 修改识别影像
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		OcrImage ocrImage = ocrImageService.selectOcrImageById(id);
		mmap.put("ocrImage", ocrImage);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存识别影像
	 */
	@RequiresPermissions("system:ocrImage:edit")
	@Log(title = "识别影像", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(OcrImage ocrImage)
	{		
		return toAjax(ocrImageService.updateOcrImage(ocrImage));
	}
	
	/**
	 * 删除识别影像
	 */
	@RequiresPermissions("system:ocrImage:remove")
	@Log(title = "识别影像", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(ocrImageService.deleteOcrImageByIds(ids));
	}
	
}
