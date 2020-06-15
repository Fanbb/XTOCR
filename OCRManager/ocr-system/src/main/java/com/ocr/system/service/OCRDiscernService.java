package com.ocr.system.service;

import com.ocr.system.model.ResultData;


public interface OCRDiscernService {
    /**
     * 单个图片识别单结果返回
     *
     * @param type 渠道
     * @param url  图片路径
     * @param str  图片64
     * @return
     */
    public ResultData runOne(String channelCode, String url, String str, String imgType);

    /**
     * 单个图片识别多结果返回
     *
     * @param channelCode
     * @param url
     * @param str
     * @return
     */
    public ResultData runMore(String channelCode, String url, String str);

    /**
     * 更新前端传值状态
     *
     * @param tradeId
     * @return
     */
    public int modifyResult(String tradeId);
}
