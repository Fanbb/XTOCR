package com.ocr.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.core.page.TableDataInfo;
import com.ocr.common.core.text.Convert;
import com.ocr.common.enums.BusinessType;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.poi.ExcelUtil;
import com.ocr.framework.util.ShiroUtils;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.model.*;
import com.ocr.system.service.IChannelService;
import com.ocr.system.service.IOcrImageService;
import com.ocr.system.service.IOcrTradeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 识别流水 信息操作处理
 *
 * @author ocr
 * @date 2020-05-20
 */
@Controller
@RequestMapping("/system/ocrTrade")
public class OcrTradeController extends BaseController {
    private String prefix = "system/ocrTrade";

    @Autowired
    private IOcrTradeService ocrTradeService;

    @Autowired
    private IOcrImageService iOcrImageService;

    @Autowired
    private IChannelService channelService;

    @Value("${ocr.profile}")
    private String imgUploadPath;

    @RequiresPermissions("system:ocrTrade:view")
    @GetMapping()
    public String ocrTrade(ModelMap mmap) {
        if (ShiroUtils.getSysUser().getRemark().equals("渠道业务员")) {
            mmap.addAttribute("channels", channelService.selectChannelsByUserId(ShiroUtils.getSysUser().getUserId()));
        } else {
            mmap.addAttribute("channels", channelService.selectChannelAll());
        }
        return prefix + "/ocrTrade";
    }

    /**
     * 查询识别流水列表
     */
    @PostMapping("/listByImgId")
    @ResponseBody
    public TableDataInfo listByImgId(OcrTrade ocrTrade) {
        startPage();
        List<OcrTrade> list = ocrTradeService.selectOcrTradeList(ocrTrade);
        return getDataTable(list);
    }

    /**
     * 查询识别流水列表
     */
    @PostMapping("/getTradeListByIds")
    @ResponseBody
    public TableDataInfo getTradeListByIds(String tradeIds) {
        startPage();
        List<OcrTrade> list = ocrTradeService.selectOcrTradeListByIds(tradeIds);
        return getDataTable(list);
    }

    /**
     * 查询识别流水列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OcrTrade ocrTrade) {
        startPage();
        if (ShiroUtils.getSysUser().getRemark().equals("渠道业务员")) {
            ocrTrade.setUserId(ShiroUtils.getSysUser().getUserId());
        }
        List<OcrTrade> list = ocrTradeService.selectOcrTradeList(ocrTrade);
        return getDataTable(list);
    }


    /**
     * 导出识别流水列表
     */
    @RequiresPermissions("system:ocrTrade:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OcrTrade ocrTrade) {
        List<OcrTrade> list = ocrTradeService.selectOcrTradeList(ocrTrade);
        ExcelUtil<OcrTrade> util = new ExcelUtil<OcrTrade>(OcrTrade.class);
        return util.exportExcel(list, "ocrTrade");
    }

    /**
     * 新增识别流水
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存识别流水
     */
    @RequiresPermissions("system:ocrTrade:add")
    @Log(title = "识别流水", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(OcrTrade ocrTrade) {
        return toAjax(ocrTradeService.insertOcrTrade(ocrTrade));
    }

    @RequiresPermissions("system:ocrTrade:blend")
    @Log(title = "流水勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/blend")
    @ResponseBody
    public AjaxResult blend(OcrTrade ocrTrade) {
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "身份证正面流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBlendIdCardFront")
    @ResponseBody
    public AjaxResult fieldBlendIdCardFront(IDCardFrontBack idCardFrontBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(idCardFrontBack.getTradeId());
        Integer fieldTotal = IDCardFrontBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(idCardFrontBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(idCardFrontBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "身份证反面流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBlendIdCardBack")
    @ResponseBody
    public AjaxResult fieldBlendIdCardBack(IDCardBackBack idCardBackBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(idCardBackBack.getTradeId());
        Integer fieldTotal = IDCardBackBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(idCardBackBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(idCardBackBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "银行卡流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBlendBankCardBack")
    @ResponseBody
    public AjaxResult fieldBlendBankCardBack(BankCardBack bankCardBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(bankCardBack.getTradeId());
        Integer fieldTotal = BankCardBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(bankCardBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(bankCardBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "存单流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBlendDepositReceiptBack")
    @ResponseBody
    public AjaxResult fieldBlendDepositReceiptBack(DepositReceiptBack depositReceiptBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(depositReceiptBack.getTradeId());
        Integer fieldTotal = DepositReceiptBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(depositReceiptBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(depositReceiptBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }


    @Log(title = "房本流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBlendPremisesPermitBack")
    @ResponseBody
    public AjaxResult fieldBlendPremisesPermitBack(PremisesPermitBack premisesPermitBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(premisesPermitBack.getTradeId());
        Integer fieldTotal = PremisesPermitBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(premisesPermitBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(premisesPermitBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "户口本流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldResidenceBookletBack")
    @ResponseBody
    public AjaxResult fieldResidenceBookletBack(ResidenceBookletBack residenceBookletBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(residenceBookletBack.getTradeId());
        Integer fieldTotal = ResidenceBookletBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(residenceBookletBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(residenceBookletBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "结婚证流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldMarriageLicenseBack")
    @ResponseBody
    public AjaxResult fieldMarriageLicenseBack(MarriageLicenseBack marriageLicenseBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(marriageLicenseBack.getTradeId());
        Integer fieldTotal = MarriageLicenseBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(marriageLicenseBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(marriageLicenseBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "行驶证流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldDrivingLicenseBack")
    @ResponseBody
    public AjaxResult fieldDrivingLicenseBack(DrivingLicenseBack drivingLicenseBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(drivingLicenseBack.getTradeId());
        Integer fieldTotal = DrivingLicenseBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(drivingLicenseBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(drivingLicenseBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "驾驶证流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldDriversLicenseBack")
    @ResponseBody
    public AjaxResult fieldDriversLicenseBack(DriversLicenseBack driversLicenseBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(driversLicenseBack.getTradeId());
        Integer fieldTotal = DriversLicenseBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(driversLicenseBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(driversLicenseBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "车牌号流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldPlateNumberBack")
    @ResponseBody
    public AjaxResult fieldPlateNumberBack(PlateNumberBack plateNumberBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(plateNumberBack.getTradeId());
        Integer fieldTotal = PlateNumberBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(plateNumberBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(plateNumberBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    @Log(title = "营业执照流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldBusinessLicenseBack")
    @ResponseBody
    public AjaxResult fieldBusinessLicenseBack(BusinessLicenseBack businessLicenseBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(businessLicenseBack.getTradeId());
        Integer fieldTotal = BusinessLicenseBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(businessLicenseBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(businessLicenseBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "普通发票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldInvoiceBack")
    @ResponseBody
    public AjaxResult fieldInvoiceBack(InvoiceBack invoiceBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(invoiceBack.getTradeId());
        Integer fieldTotal = InvoiceBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(invoiceBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(invoiceBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "航空运输电子客票行程单流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldItineraryBack")
    @ResponseBody
    public AjaxResult fieldItineraryBack(ItineraryBack itineraryBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(itineraryBack.getTradeId());
        Integer fieldTotal = ItineraryBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(itineraryBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(itineraryBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "通用定额发票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldQuotaInvoiceBack")
    @ResponseBody
    public AjaxResult fieldQuotaInvoiceBack(QuotaInvoiceBack quotaInvoiceBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(quotaInvoiceBack.getTradeId());
        Integer fieldTotal = QuotaInvoiceBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(quotaInvoiceBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(quotaInvoiceBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "火车票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldRalTicketBack")
    @ResponseBody
    public AjaxResult fieldRalTicketBack(RalTicketBack ralTicketBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(ralTicketBack.getTradeId());
        Integer fieldTotal = RalTicketBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(ralTicketBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(ralTicketBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "火车票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldTollInvoiceBack")
    @ResponseBody
    public AjaxResult fieldTollInvoiceBack(TollInvoiceBack rollInvoiceBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(rollInvoiceBack.getTradeId());
        Integer fieldTotal = TollInvoiceBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(rollInvoiceBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(rollInvoiceBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "增值税发票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldVatInvoiceBack")
    @ResponseBody
    public AjaxResult fieldVatInvoiceBack(VatInvoiceBack vatInvoiceBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(vatInvoiceBack.getTradeId());
        Integer fieldTotal = VatInvoiceBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(vatInvoiceBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(vatInvoiceBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    @Log(title = "电子发票流水字段勾对", businessType = BusinessType.UPDATE)
    @PostMapping("/fieldEleInvoiceBack")
    @ResponseBody
    public AjaxResult fieldEleInvoiceBack(EleInvoiceBack eleInvoiceBack) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(eleInvoiceBack.getTradeId());
        Integer fieldTotal = EleInvoiceBack.class.getDeclaredFields().length - 1;
        Integer rightTotal = 0;
        try {
            rightTotal = testReflect(eleInvoiceBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fieldTotal == rightTotal) {
            ocrTrade.setTickStatus("1");
        } else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(eleInvoiceBack));
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }
    /**
     * 修改识别流水
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
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
    public AjaxResult editSave(OcrTrade ocrTrade) {
        return toAjax(ocrTradeService.updateOcrTrade(ocrTrade));
    }

    /**
     * 根据流水Id批量下载图片
     *
     * @param ids
     * @return
     */
    @Log(title = "批量下载图片", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:ocrTrade:downloadImages")
    @GetMapping("/downloadImages")
    @ResponseBody
    public void downloadImages(HttpServletResponse response, String ids) throws IOException {
        try {
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=\"imgFiles.zip\"");// 设置在下载框默认显示的文件名
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            String[] files = ocrTradeService.selectImagePath(Convert.toStrArray(ids));
            for (int i = 0; i < files.length; i++) {
                //查询img path 获取路径名称
                String imgPath = files[i];
                String fileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
                File file = getFileByUrl(imgPath, fileName.substring(fileName.lastIndexOf(".")));
                zos.putNextEntry(new ZipEntry(fileName));
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ocrTradeService.updateRemark2ByIds(Convert.toStrArray(ids));
    }

    /**
     * url转file
     *
     * @param fileUrl
     * @param suffix
     * @return
     */
    private File getFileByUrl(String fileUrl, String suffix) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedOutputStream stream = null;
        InputStream inputStream = null;
        File file = null;
        try {
            // 创建URL
            URL url = new URL(fileUrl);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            inputStream = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            file = File.createTempFile("file", suffix, new File(imgUploadPath));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(outStream.toByteArray());
        } catch (Exception e) {
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (stream != null)
                    stream.close();
                outStream.close();
            } catch (Exception e) {
            }
        }
        return file;
    }


    /**
     * 删除识别流水
     */
    @RequiresPermissions("system:ocrTrade:remove")
    @Log(title = "识别流水", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(ocrTradeService.deleteOcrTradeByIds(ids));
    }


    /**
     * 流水详情跳转
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        OcrTrade ocrTrade = ocrTradeService.selectOcrTradeById(id);
        mmap.addAttribute("ocrTrade", ocrTrade);
        OcrImage ocrImage = iOcrImageService.selectOcrImageById(ocrTrade.getImageId());
        mmap.addAttribute("imgUrl", ocrImage.getLocalPath());

        if (ocrTrade.getImageType().equals("IDCardFront")) {
            IDCardFront idCardFront = JSON.parseObject(ocrTrade.getRemark1(), IDCardFront.class);
            IDCardFrontBack idCardFrontBack = new IDCardFrontBack("", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                idCardFrontBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardFrontBack.class);
            }
            idCardFrontBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardFront", idCardFront);
            mmap.put("idCardFrontBack", idCardFrontBack);
            return prefix + "/detail/cardFront";
        } else if (ocrTrade.getImageType().equals("IDCardBack")) {
            IDCardBack idCardBack = JSON.parseObject(ocrTrade.getRemark1(), IDCardBack.class);
            IDCardBackBack idCardBackBack = new IDCardBackBack("", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                idCardBackBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardBackBack.class);
            }
            idCardBackBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardBackBack", idCardBackBack);
            mmap.put("idCardBack", idCardBack);
            return prefix + "/detail/cardBack";
        } else if (ocrTrade.getImageType().equals("BankCard")) {
            BankCard bankCard = JSON.parseObject(ocrTrade.getRemark1(), BankCard.class);
            BankCardBack bankCardBack = new BankCardBack("", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                bankCardBack = JSON.parseObject(ocrTrade.getRemark3(), BankCardBack.class);
            }
            bankCardBack.setTradeId(ocrTrade.getId());
            mmap.put("bankCardBack", bankCardBack);
            mmap.put("bankCard", bankCard);
            return prefix + "/detail/bankCard";
        } else if (ocrTrade.getImageType().equals("Deposit")) {
            DepositReceipt depositReceipt = JSON.parseObject(ocrTrade.getRemark1(), DepositReceipt.class);
            DepositReceiptBack depositReceiptBack = new DepositReceiptBack("", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                depositReceiptBack = JSON.parseObject(ocrTrade.getRemark3(), DepositReceiptBack.class);
            }
            depositReceiptBack.setTradeId(ocrTrade.getId());
            mmap.put("depositReceiptBack", depositReceiptBack);
            mmap.put("depositReceipt", depositReceipt);
            return prefix + "/detail/depositReceipt";
        } else if (ocrTrade.getImageType().equals("PremisesPermit")) {
            PremisesPermit premisesPermit = JSON.parseObject(ocrTrade.getRemark1(), PremisesPermit.class);
            PremisesPermitBack premisesPermitBack = new PremisesPermitBack("", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                premisesPermitBack = JSON.parseObject(ocrTrade.getRemark3(), PremisesPermitBack.class);
            }
            premisesPermitBack.setTradeId(ocrTrade.getId());
            mmap.put("premisesPermitBack", premisesPermitBack);
            mmap.put("premisesPermit", premisesPermit);
            return prefix + "/detail/premisesPermit";
        } /*else if (ocrTrade.getImageType().equals("Bankbook")) {
            PremisesPermit premisesPermit = JSON.parseObject(ocrTrade.getRemark1(), PremisesPermit.class);
            PremisesPermitBack premisesPermitBack = new PremisesPermitBack("", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                premisesPermitBack = JSON.parseObject(ocrTrade.getRemark3(), PremisesPermitBack.class);
            }
            premisesPermitBack.setTradeId(ocrTrade.getId());
            mmap.put("premisesPermitBack", premisesPermitBack);
            mmap.put("premisesPermit", premisesPermit);
            return prefix + "/detail/bankbook";
        } */ else if (ocrTrade.getImageType().equals("ResidenceBooklet")) {
            ResidenceBooklet residenceBooklet = JSON.parseObject(ocrTrade.getRemark1(), ResidenceBooklet.class);
            ResidenceBookletBack residenceBookletBack = new ResidenceBookletBack("", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                residenceBookletBack = JSON.parseObject(ocrTrade.getRemark3(), ResidenceBookletBack.class);
            }
            residenceBookletBack.setTradeId(ocrTrade.getId());
            mmap.put("residenceBookletBack", residenceBookletBack);
            mmap.put("residenceBooklet", residenceBooklet);
            return prefix + "/detail/residenceBooklet";
        } else if (ocrTrade.getImageType().equals("MarriageLicense")) {
            MarriageLicense marriageLicense = JSON.parseObject(ocrTrade.getRemark1(), MarriageLicense.class);
            MarriageLicenseBack marriageLicenseBack = new MarriageLicenseBack("", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                marriageLicenseBack = JSON.parseObject(ocrTrade.getRemark3(), MarriageLicenseBack.class);
            }
            marriageLicenseBack.setTradeId(ocrTrade.getId());
            mmap.put("marriageLicenseBack", marriageLicenseBack);
            mmap.put("marriageLicense", marriageLicense);
            return prefix + "/detail/marriageLicense";
        } else if (ocrTrade.getImageType().equals("DrivingLicense")) {
            DrivingLicense drivingLicense = JSON.parseObject(ocrTrade.getRemark1(), DrivingLicense.class);
            DrivingLicenseBack drivingLicenseBack = new DrivingLicenseBack("", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                drivingLicenseBack = JSON.parseObject(ocrTrade.getRemark3(), DrivingLicenseBack.class);
            }
            drivingLicenseBack.setTradeId(ocrTrade.getId());
            mmap.put("drivingLicenseBack", drivingLicenseBack);
            mmap.put("drivingLicense", drivingLicense);
            return prefix + "/detail/drivingLicense";
        } else if (ocrTrade.getImageType().equals("DriversLicense")) {
            DriversLicense driversLicense = JSON.parseObject(ocrTrade.getRemark1(), DriversLicense.class);
            DriversLicenseBack driversLicenseBack = new DriversLicenseBack("", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                driversLicenseBack = JSON.parseObject(ocrTrade.getRemark3(), DriversLicenseBack.class);
            }
            driversLicenseBack.setTradeId(ocrTrade.getId());
            mmap.put("driversLicenseBack", driversLicenseBack);
            mmap.put("driversLicense", driversLicense);
            return prefix + "/detail/driversLicense";
        } else if (ocrTrade.getImageType().equals("PlateNumber")) {
            PlateNumber plateNumber = JSON.parseObject(ocrTrade.getRemark1(), PlateNumber.class);
            PlateNumberBack plateNumberBack = new PlateNumberBack("", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                plateNumberBack = JSON.parseObject(ocrTrade.getRemark3(), PlateNumberBack.class);
            }
            plateNumberBack.setTradeId(ocrTrade.getId());
            mmap.put("plateNumberBack", plateNumberBack);
            mmap.put("plateNumber", plateNumber);
            return prefix + "/detail/plateNumber";
        } else if (ocrTrade.getImageType().equals("BusinessLicense")) {
            BusinessLicense businessLicense = JSON.parseObject(ocrTrade.getRemark1(), BusinessLicense.class);
            BusinessLicenseBack businessLicenseBack = new BusinessLicenseBack("", "0", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                businessLicenseBack = JSON.parseObject(ocrTrade.getRemark3(), BusinessLicenseBack.class);
            }
            businessLicenseBack.setTradeId(ocrTrade.getId());
            mmap.put("businessLicenseBack", businessLicenseBack);
            mmap.put("businessLicense", businessLicense);
            return prefix + "/detail/businessLicense";
        }else if (ocrTrade.getImageType().equals("VatInvoice")) {
            VatInvoice vatInvoice = JSON.parseObject(ocrTrade.getRemark1(), VatInvoice.class);
            VatInvoiceBack vatInvoiceBack = new VatInvoiceBack("", "0", "0", "0", "0", "0", "0", "0", "0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                vatInvoiceBack = JSON.parseObject(ocrTrade.getRemark3(), VatInvoiceBack.class);
            }
            vatInvoiceBack.setTradeId(ocrTrade.getId());
            mmap.put("vatInvoiceBack", vatInvoiceBack);
            mmap.put("vatInvoice", vatInvoice);
            return prefix + "/detail/vatInvoice";
        }else if (ocrTrade.getImageType().equals("Invoice")) {
            Invoice invoice = JSON.parseObject(ocrTrade.getRemark1(), Invoice.class);
            InvoiceBack invoiceBack = new InvoiceBack("", "0", "0", "0", "0", "0", "0", "0", "0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                invoiceBack = JSON.parseObject(ocrTrade.getRemark3(), InvoiceBack.class);
            }
            invoiceBack.setTradeId(ocrTrade.getId());
            mmap.put("invoiceBack", invoiceBack);
            mmap.put("invoice", invoice);
            return prefix + "/detail/invoice";
        }else if (ocrTrade.getImageType().equals("Itinerary")) {
            Itinerary itinerary = JSON.parseObject(ocrTrade.getRemark1(), Itinerary.class);
            ItineraryBack itineraryBack = new ItineraryBack("", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                itineraryBack = JSON.parseObject(ocrTrade.getRemark3(), ItineraryBack.class);
            }
            itineraryBack.setTradeId(ocrTrade.getId());
            mmap.put("itineraryBack", itineraryBack);
            mmap.put("itinerary", itinerary);
            return prefix + "/detail/itinerary";
        }else if (ocrTrade.getImageType().equals("RalTicket")) {
            RalTicket ralTicket = JSON.parseObject(ocrTrade.getRemark1(), RalTicket.class);
            RalTicketBack ralTicketBack = new RalTicketBack("", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                ralTicketBack = JSON.parseObject(ocrTrade.getRemark3(), RalTicketBack.class);
            }
            ralTicketBack.setTradeId(ocrTrade.getId());
            mmap.put("ralTicketBack", ralTicketBack);
            mmap.put("ralTicket", ralTicket);
            return prefix + "/detail/ralTicket";
        }else if (ocrTrade.getImageType().equals("TollInvoice")) {
            TollInvoice tollInvoice = JSON.parseObject(ocrTrade.getRemark1(), TollInvoice.class);
            TollInvoiceBack tollInvoiceBack = new TollInvoiceBack("", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                tollInvoiceBack = JSON.parseObject(ocrTrade.getRemark3(), TollInvoiceBack.class);
            }
            tollInvoiceBack.setTradeId(ocrTrade.getId());
            mmap.put("tollInvoiceBack", tollInvoiceBack);
            mmap.put("tollInvoice", tollInvoice);
            return prefix + "/detail/tollInvoice";
        }else if (ocrTrade.getImageType().equals("QuotaInvoice")) {
            QuotaInvoice quotaInvoice = JSON.parseObject(ocrTrade.getRemark1(), QuotaInvoice.class);
            QuotaInvoiceBack quotaInvoiceBack = new QuotaInvoiceBack("", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                quotaInvoiceBack = JSON.parseObject(ocrTrade.getRemark3(), QuotaInvoiceBack.class);
            }
            quotaInvoiceBack.setTradeId(ocrTrade.getId());
            mmap.put("quotaInvoiceBack", quotaInvoiceBack);
            mmap.put("quotaInvoice", quotaInvoice);
            return prefix + "/detail/quotaInvoice";
        }else if (ocrTrade.getImageType().equals("EleInvoice")) {
            EleInvoice eleInvoice = JSON.parseObject(ocrTrade.getRemark1(), EleInvoice.class);
            EleInvoiceBack eleInvoiceBack = new EleInvoiceBack("", "0", "0", "0", "0", "0", "0", "0", "0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                eleInvoiceBack = JSON.parseObject(ocrTrade.getRemark3(), EleInvoiceBack.class);
            }
            eleInvoiceBack.setTradeId(ocrTrade.getId());
            mmap.put("eleInvoiceBack", eleInvoiceBack);
            mmap.put("eleInvoice", eleInvoice);
            return prefix + "/detail/eleInvoice";
        }else if (ocrTrade.getImageType().equals("GeneralText")) {//通用文字识别
            GeneralText generalText = JSON.parseObject(ocrTrade.getRemark1(), GeneralText.class);
            mmap.put("generalText", generalText);
            return prefix + "/detail/generalText";
        }else if(ocrTrade.getImageType().equals("IDCardFront_Video")) {//视频身份证正面
            IDCardFront idCardFront = JSON.parseObject(ocrTrade.getRemark1(), IDCardFront.class);
            IDCardFrontBack idCardFrontBack = new IDCardFrontBack("", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                idCardFrontBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardFrontBack.class);
            }
            idCardFrontBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardFront", idCardFront);
            mmap.put("idCardFrontBack", idCardFrontBack);
            return prefix + "/detail/CardFront_Video";
        }else if (ocrTrade.getImageType().equals("IDCardBack_Video")) {//视频身份证背面
            IDCardBack idCardBack = JSON.parseObject(ocrTrade.getRemark1(), IDCardBack.class);
            IDCardBackBack idCardBackBack = new IDCardBackBack("", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                idCardBackBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardBackBack.class);
            }
            idCardBackBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardBackBack", idCardBackBack);
            mmap.put("idCardBack", idCardBack);
            return prefix + "/detail/IDCardBack_Video";
        }else if (ocrTrade.getImageType().equals("PremisesPermit_Video")) {//视频房本
            PremisesPermit premisesPermit = JSON.parseObject(ocrTrade.getRemark1(), PremisesPermit.class);
            PremisesPermitBack premisesPermitBack = new PremisesPermitBack("", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                premisesPermitBack = JSON.parseObject(ocrTrade.getRemark3(), PremisesPermitBack.class);
            }
            premisesPermitBack.setTradeId(ocrTrade.getId());
            mmap.put("premisesPermitBack", premisesPermitBack);
            mmap.put("premisesPermit", premisesPermit);
            return prefix + "/detail/premisesPermit_Video";
        }else if (ocrTrade.getImageType().equals("BusinessLicense_Video")) {//视频营业执照
            BusinessLicense businessLicense = JSON.parseObject(ocrTrade.getRemark1(), BusinessLicense.class);
            BusinessLicenseBack businessLicenseBack = new BusinessLicenseBack("", "0", "0", "0", "0", "0", "0", "0", "0", "0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())) {
                businessLicenseBack = JSON.parseObject(ocrTrade.getRemark3(), BusinessLicenseBack.class);
            }
            businessLicenseBack.setTradeId(ocrTrade.getId());
            mmap.put("businessLicenseBack", businessLicenseBack);
            mmap.put("businessLicense", businessLicense);
            return prefix + "/detail/businessLicense_Video";
        }


        else {
            return prefix + "/detail/data";
        }

    }

    /**
     * 首页加载数量问题
     * dataTotal
     * typeTotal
     * pointTotal
     */
    @PostMapping("/pointTotal")
    @ResponseBody
    public Map pointTotal() {
        List<String> listType = new ArrayList<>();
        listType.add("勾对成功状态");
        listType.add("勾对失败状态");
        List<EchartsEntity> EnchantsEntity = new ArrayList<>();
        EnchantsEntity.add(new EchartsEntity("勾对成功状态", ocrTradeService.selectCountByTrickStatus("1")));
        EnchantsEntity.add(new EchartsEntity("勾对失败状态", ocrTradeService.selectCountByTrickStatus("2")));
        Map map = new HashMap();
        map.put("name", listType);
        map.put("data", EnchantsEntity);
        return map;
    }

    @PostMapping("/typeTotal")
    @ResponseBody
    public Map typeTotal() {
        List<String> listName = ocrTradeService.selectNameToTypeTotal();
        List<String> listCount = ocrTradeService.selectValueToTypeTotal();
        List<EchartsEntity> EnchantsEntity = new ArrayList<>();
        for (int i = 0; i < listName.size(); i++) {
            EnchantsEntity.add(new EchartsEntity(listName.get(i), listCount.get(i)));
        }
        Map map = new HashMap();
        map.put("name", listName);
        map.put("data", EnchantsEntity);
        return map;
    }

    @PostMapping("/dataTotal")
    @ResponseBody
    public Map dataTotal() {
        List<String> listName = ocrTradeService.selectNameToDataTotal();
        List<String> listCount = ocrTradeService.selectValueToDataTotal();
        List<EchartsEntity> EnchantsEntity = new ArrayList<>();
        for (int i = 0; i < listName.size(); i++) {
            EnchantsEntity.add(new EchartsEntity(listName.get(i), listCount.get(i)));
        }
        Map map = new HashMap();
        map.put("name", listName);
        map.put("data", EnchantsEntity);
        return map;
    }

    public static Integer testReflect(Object model) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Integer count = 0;
        Field[] field = model.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组
        for (int j = 0; j < field.length; j++) {     //遍历所有属性
            String name = field[j].getName();    //获取属性的名字
            if (!name.equals("tradeId")) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
                Method m = model.getClass().getMethod("get" + name);
                String value = (String) m.invoke(model);    //调用getter方法获取属性值
                if (null != value && value.equals("0")) {
                    count++;
                }
            }
        }
        return count;
    }


}
