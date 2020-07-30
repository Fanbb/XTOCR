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
        Integer fieldTotal=IDCardFrontBack.class.getDeclaredFields().length-1;
        Integer rightTotal = 0;
        try{
            rightTotal = testReflect(idCardFrontBack);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fieldTotal==rightTotal){
            ocrTrade.setTickStatus("1");
        }else {
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
        Integer fieldTotal=IDCardBackBack.class.getDeclaredFields().length-1;
        Integer rightTotal = 0;
        try{
            rightTotal = testReflect(idCardBackBack);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fieldTotal==rightTotal){
            ocrTrade.setTickStatus("1");
        }else {
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
        Integer fieldTotal=BankCardBack.class.getDeclaredFields().length-1;
        Integer rightTotal = 0;
        try{
            rightTotal = testReflect(bankCardBack);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fieldTotal==rightTotal){
            ocrTrade.setTickStatus("1");
        }else {
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
        Integer fieldTotal=DepositReceiptBack.class.getDeclaredFields().length-1;
        Integer rightTotal = 0;
        try{
            rightTotal = testReflect(depositReceiptBack);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fieldTotal==rightTotal){
            ocrTrade.setTickStatus("1");
        }else {
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
        Integer fieldTotal=PremisesPermitBack.class.getDeclaredFields().length-1;
        Integer rightTotal = 0;
        try{
            rightTotal = testReflect(premisesPermitBack);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fieldTotal==rightTotal){
            ocrTrade.setTickStatus("1");
        }else {
            ocrTrade.setTickStatus("2");
        }
        ocrTrade.setFieldTotal(fieldTotal.toString());
        ocrTrade.setRightTotal(rightTotal.toString());
        ocrTrade.setRemark3(JSON.toJSONString(premisesPermitBack));
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
            IDCardFrontBack idCardFrontBack = new IDCardFrontBack("","0","0","0","0","0","0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())){
                idCardFrontBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardFrontBack.class);
            }
            idCardFrontBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardFront", idCardFront);
            mmap.put("idCardFrontBack", idCardFrontBack);
            return prefix + "/detail/cardFront";
        } else if (ocrTrade.getImageType().equals("IDCardBack")) {
            IDCardBack idCardBack = JSON.parseObject(ocrTrade.getRemark1(), IDCardBack.class);
            IDCardBackBack idCardBackBack = new IDCardBackBack("","0","0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())){
                idCardBackBack = JSON.parseObject(ocrTrade.getRemark3(), IDCardBackBack.class);
            }
            idCardBackBack.setTradeId(ocrTrade.getId());
            mmap.put("idCardBackBack", idCardBackBack);
            mmap.put("idCardBack", idCardBack);
            return prefix + "/detail/cardBack";
        } else if (ocrTrade.getImageType().equals("BankCard")) {
            BankCard bankCard = JSON.parseObject(ocrTrade.getRemark1(), BankCard.class);
            BankCardBack bankCardBack = new BankCardBack("","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())){
                bankCardBack = JSON.parseObject(ocrTrade.getRemark3(), BankCardBack.class);
            }
            bankCardBack.setTradeId(ocrTrade.getId());
            mmap.put("bankCardBack", bankCardBack);
            mmap.put("bankCard", bankCard);
            return prefix + "/detail/bankCard";
        } else if (ocrTrade.getImageType().equals("Deposit")) {
            DepositReceipt depositReceipt = JSON.parseObject(ocrTrade.getRemark1(), DepositReceipt.class);
            DepositReceiptBack depositReceiptBack = new DepositReceiptBack("","0","0","0","0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())){
                depositReceiptBack = JSON.parseObject(ocrTrade.getRemark3(), DepositReceiptBack.class);
            }
            depositReceiptBack.setTradeId(ocrTrade.getId());
            mmap.put("depositReceiptBack", depositReceiptBack);
            mmap.put("depositReceipt", depositReceipt);
            return prefix + "/detail/depositReceipt";
        } else if (ocrTrade.getImageType().equals("PremisesPermit")) {
            PremisesPermit premisesPermit = JSON.parseObject(ocrTrade.getRemark1(), PremisesPermit.class);
            PremisesPermitBack premisesPermitBack = new PremisesPermitBack("","0","0","0","0","0","0","0","0");
            if (StringUtils.isNotEmpty(ocrTrade.getRemark3())){
                premisesPermitBack = JSON.parseObject(ocrTrade.getRemark3(), PremisesPermitBack.class);
            }
            premisesPermitBack.setTradeId(ocrTrade.getId());
            mmap.put("premisesPermitBack", premisesPermitBack);
            mmap.put("premisesPermit", premisesPermit);
            return prefix + "/detail/premisesPermit";
        } else {
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
            if (!name.equals("tradeId")){
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
