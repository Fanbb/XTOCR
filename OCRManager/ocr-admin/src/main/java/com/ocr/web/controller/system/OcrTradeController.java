package com.ocr.web.controller.system;

import java.util.List;

import com.ocr.system.domain.OcrImage;
import com.ocr.system.model.BankCard;
import com.ocr.system.model.DepositReceipt;
import com.ocr.system.model.IDCardBack;
import com.ocr.system.model.IDCardFront;
import com.ocr.system.service.IOcrImageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ocr.common.annotation.Log;
import com.ocr.common.enums.BusinessType;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.service.IOcrTradeService;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.poi.ExcelUtil;

/**
 * 识别流水 信息操作处理
 * 
 * @author ocr
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/system/ocrTrade")
public class OcrTradeController extends BaseController
{
    private String prefix = "system/ocrTrade";
	
	@Autowired
	private IOcrTradeService ocrTradeService;

	@Autowired
	private IOcrImageService iOcrImageService;

	@Value("${ocr.profile}")
	private String imgUploadPath;
	
	@RequiresPermissions("system:ocrTrade:view")
	@GetMapping()
	public String ocrTrade()
	{
	    return prefix + "/ocrTrade";
	}
	
	/**
	 * 查询识别流水列表
	 */
	@RequiresPermissions("system:ocrTrade:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(OcrTrade ocrTrade)
	{
		startPage();
        List<OcrTrade> list = ocrTradeService.selectOcrTradeList(ocrTrade);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出识别流水列表
	 */
	@RequiresPermissions("system:ocrTrade:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OcrTrade ocrTrade)
    {
    	List<OcrTrade> list = ocrTradeService.selectOcrTradeList(ocrTrade);
        ExcelUtil<OcrTrade> util = new ExcelUtil<OcrTrade>(OcrTrade.class);
        return util.exportExcel(list, "ocrTrade");
    }
	
	/**
	 * 新增识别流水
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存识别流水
	 */
	@RequiresPermissions("system:ocrTrade:add")
	@Log(title = "识别流水", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(OcrTrade ocrTrade)
	{		
		return toAjax(ocrTradeService.insertOcrTrade(ocrTrade));
	}

	/**
	 * 修改识别流水
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(id);
		mmap.put("ocrTrade", ocrTrade);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存识别流水
	 */
	@RequiresPermissions("system:ocrTrade:edit")
	@Log(title = "识别流水", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(OcrTrade ocrTrade)
	{		
		return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
	}
	
	/**
	 * 删除识别流水
	 */
	@RequiresPermissions("system:ocrTrade:remove")
	@Log(title = "识别流水", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(ocrTradeService.deleteOcrTradeByIds(ids));
	}


	/**
	 * 修改识别流水
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") String id, ModelMap mmap)
	{
		OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(id);
		OcrImage ocrImage = iOcrImageService.selectOcrImageById(ocrTrade.getImageId());
		String imgUrl=ocrImage.getLocalPath();
		if (ocrImage.getLocalPath().indexOf(imgUploadPath)==0){
			imgUrl = ocrImage.getLocalPath().replace( imgUploadPath,"/profile/");
			mmap.addAttribute("imgUrl", imgUrl);
		}else {
			mmap.addAttribute("imgUrl", imgUrl);
		}


		if (ocrTrade.getImageType().equals("IDCardFront")){
			IDCardFront idCardFront= new IDCardFront();

			idCardFront.setName("张三");
			idCardFront.setNation("汉");
			idCardFront.setSex("男");
			idCardFront.setAddress("威海市统一路39号");
			idCardFront.setIdCardNo("411823199117211112");
			mmap.put("idCardFront", idCardFront);
			return prefix + "/detail/cardFront";
		}else if (ocrTrade.getImageType().equals("IDCardBack")){
			IDCardBack idCardBack = new IDCardBack();
			idCardBack.setEndDate("2020-05-23");
			idCardBack.setStartDate("1999-05-23");
			mmap.put("idCardBack", idCardBack);
			return prefix + "/detail/cardBack";
		}else if (ocrTrade.getImageType().equals("BankCard")){
			BankCard bankCard = new BankCard();
			bankCard.setBankCardNo("623058000026511659");
			mmap.put("bankCard", bankCard);
			return prefix + "/detail/bankCard";
		}else if (ocrTrade.getImageType().equals("Deposit")){
			DepositReceipt depositReceipt = new DepositReceipt();
			depositReceipt.setAccNo("994562556354556");
			depositReceipt.setAmt("CNY.500000");
			depositReceipt.setAmtCapital("伍拾万圆整");
			depositReceipt.setName("妹妹");
			mmap.put("depositReceipt", depositReceipt);
			return prefix + "/detail/depositReceipt";
		}else {
			return "";
		}

	}
	
}
