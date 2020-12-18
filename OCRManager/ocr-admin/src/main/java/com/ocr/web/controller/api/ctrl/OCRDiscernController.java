package com.ocr.web.controller.api.ctrl;

import com.ocr.common.annotation.Log;
import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.enums.BusinessType;
import com.ocr.common.utils.StringUtils;
import com.ocr.system.model.ResultData;
import com.ocr.system.service.IChannelService;
import com.ocr.system.service.OCRDiscernService;
import com.ocr.web.controller.api.model.DiscernResult;
import com.ocr.web.controller.api.model.ModifyResult;
import com.ocr.web.controller.api.model.OCRDiscernEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ocr.profile}")
    private String imgUploadPath;


    @Log(title = "OCR内网调用api接口调用", businessType = BusinessType.API)
    @ApiOperation("OCR内网调用api接口")
    @ApiImplicitParam(name = "ocrDiscernEntity", value = "OCR内网调用接口参数", dataType = "OCRDiscernEntity")
    @PostMapping("/outUrlDiscern")
    public AjaxResult outUrlDiscern(OCRDiscernEntity ocrDiscernEntity) {
        //首先判断渠道是否为停用状态
        if (iChannelService.channelStatus(ocrDiscernEntity.getChannelCode())) {
            return error("渠道停用状态或渠道值为空，无权限访问OCR平台！");
        }
        if (StringUtils.isEmpty(ocrDiscernEntity.getChannelCode())||StringUtils.isEmpty(ocrDiscernEntity.getImgType())) {
            return error("用户平台类别或图片类型不能为空！");
        } else {
            if (StringUtils.isEmpty(ocrDiscernEntity.getImgUrl()) && StringUtils.isEmpty(ocrDiscernEntity.getImgStr())) {
                return error("图片路径与图片Base64不能同时为空");
            }
            //if (iChannelService.channelStatus(ocrDiscernEntity.getChannelCode())) {
            //    return error("渠道停用状态，无权限访问OCR平台！");
            //}

            ResultData resultData = ocrDiscernService.runOneAgain(ocrDiscernEntity.getChannelCode(), ocrDiscernEntity.getImgUrl(), ocrDiscernEntity.getImgStr(),ocrDiscernEntity.getImgType());
//            ResultData resultData = ocrDiscernService.runOne(ocrDiscernEntity.getChannelCode(), ocrDiscernEntity.getImgUrl(), ocrDiscernEntity.getImgStr(),ocrDiscernEntity.getImgType());

            if (resultData.getType().equals("1")) {
                return AjaxResult.success(resultData.getMsg(), resultData.getData());
            } else {
                return AjaxResult.error(resultData.getMsg());
            }
        }
    }


    @Log(title = "前端更改识别结果状态调用api接口", businessType = BusinessType.API)
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


    @Log(title = "api接口影像平台影像识别", businessType = BusinessType.API)
    @ApiOperation("对影像平台影像识别")
    @ApiImplicitParam(name = "discernResult", value = "影像平台影像识别参数", dataType = "DiscernResult")
    @PostMapping("/videoPlatformDiscern")
    public AjaxResult videoPlatformDiscern(DiscernResult discernResult) {
        //首先判断渠道是否为停用状态
        if (iChannelService.channelStatus(discernResult.getChannelCode())) {
            return error("渠道停用状态，无权限访问OCR平台！");
        }

        if (StringUtils.isEmpty(discernResult.getBatchNumber())){
            return error("批次号为空！");
        }else if (StringUtils.isEmpty(discernResult.getChannelCode())){
            return error("渠道号为空！");
        }else if (StringUtils.isEmpty(discernResult.getImgType())){
            return error("影像类型为空！");
        }else if (StringUtils.isEmpty(discernResult.getCreateDate())){
            return error("影像创建日期为空！");
        }else if (StringUtils.isEmpty(discernResult.getFilePartName())){
            return error("文档部件名为空！");
        }else if (StringUtils.isEmpty(discernResult.getModelCode())){
            return error("影像渠道为空！");
        }else if (StringUtils.isEmpty(discernResult.getPassWord())){
            return error("密码为空！");
        }else if (StringUtils.isEmpty(discernResult.getUserName())){
            return error("用户名为空！");
        }else {
            ResultData resultData = ocrDiscernService.videoPlatformDiscernReal(discernResult.getBatchNumber(),discernResult.getChannelCode(),discernResult.getIdentificationCode(),discernResult.getImgType(),discernResult.getUserName(),discernResult.getPassWord(),discernResult.getModelCode(),discernResult.getCreateDate(),discernResult.getFilePartName());
            //ResultData resultData = ocrDiscernService.videoPlatformDiscern(discernResult.getBatchNumber(),discernResult.getChannelCode(),discernResult.getIdentificationCode(),discernResult.getImgType());
            if (resultData.getType().equals("1")) {
                return AjaxResult.success(resultData.getMsg(), resultData.getData());
            } else {
                return AjaxResult.error(resultData.getMsg());
            }
        }
    }

    @Log(title = "视频调用接口", businessType = BusinessType.API)
    @ApiOperation("视频调用接口")
    @PostMapping("/runOneVideo")
    public AjaxResult runOneVideo(OCRDiscernEntity ocrDiscernEntity) {
        //首先判断渠道是否为停用状态
        if (iChannelService.channelStatus(ocrDiscernEntity.getChannelCode())) {
            return error("渠道停用状态，无权限访问OCR平台！");
        }
        if (StringUtils.isEmpty(ocrDiscernEntity.getChannelCode())||StringUtils.isEmpty(ocrDiscernEntity.getImgType())) {
            return error("用户平台类别或视频类型不能为空！");
        } else {
            if (StringUtils.isEmpty(ocrDiscernEntity.getImgUrl())) {
                return error("视频名称不对，无法生成路径");
            }
            //if (iChannelService.channelStatus(ocrDiscernEntity.getChannelCode())) {
            //    return error("渠道停用状态，无权限访问OCR平台！");
            //}

            ResultData resultData = ocrDiscernService.runOneVideo(ocrDiscernEntity.getChannelCode(), ocrDiscernEntity.getImgUrl(), ocrDiscernEntity.getImgType());
//            ResultData resultData = ocrDiscernService.runOne(ocrDiscernEntity.getChannelCode(), ocrDiscernEntity.getImgUrl(), ocrDiscernEntity.getImgStr(),ocrDiscernEntity.getImgType());

            if (resultData.getType().equals("1")) {
                return AjaxResult.success(resultData.getMsg(), resultData.getData());
            } else {
                return AjaxResult.error(resultData.getMsg());
            }
        }
    }

}



