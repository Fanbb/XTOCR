package com.ocr.system.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ocr.common.constant.Constants;
import com.ocr.common.utils.DateUtils;
import com.ocr.common.utils.StringUtils;
import com.ocr.common.utils.file.FileUtils;
import com.ocr.common.utils.file.ImageBase64;
import com.ocr.system.model.BankCard;
import com.ocr.system.model.DepositReceipt;
import com.ocr.system.model.IDCardBack;
import com.ocr.system.model.IDCardFront;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocr.system.mapper.OcrTradeMapper;
import com.ocr.system.domain.OcrTrade;
import com.ocr.system.service.IOcrTradeService;
import com.ocr.common.core.text.Convert;

import javax.imageio.stream.FileImageInputStream;

/**
 * 识别流水 服务层实现
 *
 * @author ocr
 * @date 2020-05-20
 */
@Service
public class OcrTradeServiceImpl implements IOcrTradeService {
    @Autowired
    private OcrTradeMapper ocrTradeMapper;

    /**
     * 查询识别流水信息
     *
     * @param id 识别流水ID
     * @return 识别流水信息
     */
    @Override
    public OcrTrade selectOcrTradeById(String id) {
        return ocrTradeMapper.selectOcrTradeById(id);
    }

    /**
     * 查询识别流水列表
     *
     * @param ocrTrade 识别流水信息
     * @return 识别流水集合
     */
    @Override
    public List<OcrTrade> selectOcrTradeList(OcrTrade ocrTrade) {
        return ocrTradeMapper.selectOcrTradeList(ocrTrade);
    }

    /**
     * 新增识别流水
     *
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
    @Override
    public int insertOcrTrade(OcrTrade ocrTrade) {
        return ocrTradeMapper.insertOcrTrade(ocrTrade);
    }

    /**
     * 修改识别流水
     *
     * @param ocrTrade 识别流水信息
     * @return 结果
     */
    @Override
    public int updateOcrTrade(OcrTrade ocrTrade) {
        return ocrTradeMapper.updateOcrTrade(ocrTrade);
    }

    /**
     * 删除识别流水对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOcrTradeByIds(String ids) {
        return ocrTradeMapper.deleteOcrTradeByIds(Convert.toStrArray(ids));
    }

    @Override
    public String insertIDCardFront(IDCardFront idCardFront, String channelCode, String imgId) {
        return getString(channelCode, imgId, idCardFront.getImgType(), idCardFront.getIdCardNo(), JSON.toJSONString(idCardFront));

    }

    @Override
    public String insertIDCardBack(IDCardBack idCardBack, String channelCode, String imgId) {
        return getString(channelCode, imgId, idCardBack.getImgType(), null, JSON.toJSONString(idCardBack));
    }

    @Override
    public String insertBankCard(BankCard bankCard, String channelCode, String imgId) {
        return getString(channelCode, imgId, bankCard.getImgType(), bankCard.getBankCardNo(), JSON.toJSONString(bankCard));
    }

    @Override
    public String insertDeposit(DepositReceipt depositReceipt, String channelCode, String imgId) {
        return getString(channelCode, imgId, depositReceipt.getImgType(), depositReceipt.getDepositNo(), JSON.toJSONString(depositReceipt));
    }

    @Override
    public String[] selectImagePath(String[] tradeIds) {
        return ocrTradeMapper.selectImagePath(tradeIds);
    }


    private String getString(String channelCode, String imgId, String imgType, String ocrSeq, String json) {
        OcrTrade ocrTrade = new OcrTrade();
        String id = System.currentTimeMillis() + "";
        ocrTrade.setId(id);
        ocrTrade.setChannel(channelCode);
        ocrTrade.setOcrSeq(ocrSeq);
        ocrTrade.setImageId(imgId);
        ocrTrade.setImageType(imgType);
        switch (imgType){
            case "IDCardFront":
            case "IDCardBack":
                ocrTrade.setImageName("1");
                break;
            case "BankCard":
                ocrTrade.setImageName("2");
                break;
            case "Deposit":
                ocrTrade.setImageName("3");
                break;
        }

        ocrTrade.setOcrStatus("0");
        ocrTrade.setOcrPoint("0");
        ocrTrade.setRemark2("0");
        ocrTrade.setOcrDate(DateUtils.dateTime("yyyy-MM-dd", DateUtils.getDate()));
        ocrTrade.setOcrTime(DateUtils.getTimeShort());
        ocrTrade.setRemark1(json);
        if (ocrTradeMapper.insertOcrTrade(ocrTrade) > 0) {
            return id;
        } else {
            return "";
        }
    }

}
