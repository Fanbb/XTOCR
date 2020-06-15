package com.ocr.web.controller.api.ctrl;

import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.enums.BusinessType;
import com.ocr.common.utils.StringUtils;
import com.ocr.system.model.ResultData;
import com.ocr.system.service.IChannelService;
import com.ocr.system.service.OCRDiscernService;
import com.ocr.web.controller.api.model.ModifyResult;
import com.ocr.web.controller.api.model.OCRDiscernEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("OCR内网识别非加密接口")
@RestController
@RequestMapping("/ocrDiscern")
public class OCRDiscernController extends BaseController {

    @Autowired
    private OCRDiscernService ocrDiscernService;

    @Autowired
    private IChannelService iChannelService;


    @Log(title = "OCR内网调用api接口调用", businessType = BusinessType.OTHER)
    @ApiOperation("OCR内网调用api接口")
    @ApiImplicitParam(name = "ocrDiscernEntity", value = "OCR内网调用接口参数", dataType = "OCRDiscernEntity")
    @PostMapping("/outUrlDiscern")
    public AjaxResult outUrlDiscern(OCRDiscernEntity ocrDiscernEntity) {
        if (StringUtils.isEmpty(ocrDiscernEntity.getChannelCode())||StringUtils.isEmpty(ocrDiscernEntity.getImgType())) {
            return error("用户平台类别或图片类型不能为空！");
        } else {
            if (StringUtils.isEmpty(ocrDiscernEntity.getImgUrl()) && StringUtils.isEmpty(ocrDiscernEntity.getImgStr())) {
                return error("图片路径与图片Base64不能同时为空");
            }
            if (iChannelService.channelStatus(ocrDiscernEntity.getChannelCode())) {
                return error("渠道停用状态，无权限访问OCR平台！");
            }

            ResultData resultData = ocrDiscernService.runOne(ocrDiscernEntity.getChannelCode(), ocrDiscernEntity.getImgUrl(), ocrDiscernEntity.getImgStr(),ocrDiscernEntity.getImgType());

            if (resultData.getType().equals("1")) {
                return AjaxResult.success(resultData.getMsg(), resultData.getData());
            } else {
                return AjaxResult.error(resultData.getMsg());
            }
        }
    }


    @Log(title = "前端更改识别结果状态调用api接口", businessType = BusinessType.OTHER)
    @ApiOperation("前端更改识别结果状态调用api接口")
    @ApiImplicitParam(name = "modifyResult", value = "前端更改识别结果状态接口参数", dataType = "ModifyResult")
    @PostMapping("/modifyResult")
    public AjaxResult modifyResult(ModifyResult modifyResult) {
        int result = ocrDiscernService.modifyResult(modifyResult.getTradeId());
        if (result>0){
            return success();
        }
        return error();
    }


}



