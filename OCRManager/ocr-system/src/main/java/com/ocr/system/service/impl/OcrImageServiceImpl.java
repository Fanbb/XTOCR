package com.ocr.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ocr.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocr.system.mapper.OcrImageMapper;
import com.ocr.system.domain.OcrImage;
import com.ocr.system.service.IOcrImageService;
import com.ocr.common.core.text.Convert;

/**
 * 识别影像 服务层实现
 * 
 * @author ocr
 * @date 2020-05-20
 */
@Service
public class OcrImageServiceImpl implements IOcrImageService 
{
	@Autowired
	private OcrImageMapper ocrImageMapper;

	/**
     * 查询识别影像信息
     * 
     * @param id 识别影像ID
     * @return 识别影像信息
     */
    @Override
	public OcrImage selectOcrImageById(String id)
	{
	    return ocrImageMapper.selectOcrImageById(id);
	}
	
	/**
     * 查询识别影像列表
     * 
     * @param ocrImage 识别影像信息
     * @return 识别影像集合
     */
	@Override
	public List<OcrImage> selectOcrImageList(OcrImage ocrImage)
	{
	    return ocrImageMapper.selectOcrImageList(ocrImage);
	}
	
    /**
     * 新增识别影像
     * 
     * @param ocrImage 识别影像信息
     * @return 结果
     */
	@Override
	public int insertOcrImage(OcrImage ocrImage)
	{
	    return ocrImageMapper.insertOcrImage(ocrImage);
	}

    @Override
    public String insertOcrImage(Long fileName, String filePath) {
		OcrImage ocrImage = new OcrImage();
		ocrImage.setId(fileName+"");
		ocrImage.setOcrDate(new Date(fileName));
		ocrImage.setOcrTime(DateUtils.dateTime( "yyyy-MM-dd",DateUtils.getDate()));
		ocrImage.setParentId(fileName+"");
		ocrImage.setLocalPath(filePath);
		int result = ocrImageMapper.insertOcrImage(ocrImage);
		if (result>0){
			return "影像信息插入成功";

		} else {
			return "影像信息插入失败";
		}
    }

    /**
     * 修改识别影像
     * 
     * @param ocrImage 识别影像信息
     * @return 结果
     */
	@Override
	public int updateOcrImage(OcrImage ocrImage)
	{
	    return ocrImageMapper.updateOcrImage(ocrImage);
	}

	/**
     * 删除识别影像对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOcrImageByIds(String ids)
	{
		return ocrImageMapper.deleteOcrImageByIds(Convert.toStrArray(ids));
	}
	
}
