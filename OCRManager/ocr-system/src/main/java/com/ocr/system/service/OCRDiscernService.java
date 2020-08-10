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

    /**
     * 影像平台接口识别 真实业务逻辑处理
     * @param batchNumber 批次号
     * @param channelCode 渠道号
     * @param identificationCode 唯一标识
     * @param imgType 影像类型
     * @param userName 用户名
     * @param password 密码
     * @param modelCode 影像渠道
     * @param createDate 创建日期
     * @param filePartName 文档部件名
     * @return
     */
    public ResultData videoPlatformDiscernReal(String batchNumber, String channelCode, String identificationCode, String imgType, String userName, String password, String modelCode, String createDate, String filePartName);

    ResultData runOneAgain(String channelCode, String imgUrl, String imgStr, String imgType);
}
