package com.ocr.web.controller.api.ctrl;

import com.ocr.common.core.controller.BaseController;
import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.StringUtils;
import com.ocr.web.controller.api.model.OCREncryptionEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("OCR外网识别加密接口")
@RestController
@RequestMapping("/ocrEncryption")
public class OCREncryptionController extends BaseController {

    @ApiOperation("OCR外网调用api接口")
    @ApiImplicitParam(name = "encryptionEntity", value = "OCR外网调用接口参数", dataType = "OCREncryptionEntity")
    @PostMapping("/outUrlDiscern")
    public AjaxResult outUrlDiscern(OCREncryptionEntity encryptionEntity)
    {
        if (StringUtils.isNull(encryptionEntity) || StringUtils.isEmpty(encryptionEntity.getType()))
        {
            return error("用户平台类别不能为空");
        }
        return AjaxResult.success("调用成功！");
    }


}



