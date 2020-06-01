package com.ocr.system.service;

import com.ocr.system.domain.OcrImage;
import java.util.List;

/**
 * 识别影像 服务层
 * 
 * @author ocr
 * @date 2020-05-20
 */
public interface IOcrImageService 
{
	/**
     * 查询识别影像信息
     * 
     * @param id 识别影像ID
     * @return 识别影像信息
     */
	public OcrImage selectOcrImageById(String id);
	
	/**
     * 查询识别影像列表
     * 
     * @param ocrImage 识别影像信息
     * @return 识别影像集合
     */
	public List<OcrImage> selectOcrImageList(OcrImage ocrImage);
	
	/**
     * 新增识别影像
     * 
     * @param ocrImage 识别影像信息
     * @return 结果
     */
	public int insertOcrImage(OcrImage ocrImage);

	/**
	 * 新增影像识别
	 * @param fileName 用作id和影像id
	 * @param filePath 图片路径
	 * @return
	 */
	public String insertOcrImage(Long fileName,String filePath);
	
	/**
     * 修改识别影像
     * 
     * @param ocrImage 识别影像信息
     * @return 结果
     */
	public int updateOcrImage(OcrImage ocrImage);
		
	/**
     * 删除识别影像信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteOcrImageByIds(String ids);
	
}
