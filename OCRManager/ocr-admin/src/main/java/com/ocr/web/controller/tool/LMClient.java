//package com.ocr.web.controller.tool;
//
//import com.sunyard.client.SunEcmClientApi;
//import com.sunyard.client.bean.*;
//import com.sunyard.client.impl.SunEcmClientSocketApiImpl;
//import com.sunyard.ecm.server.bean.BatchBean;
//import com.sunyard.ecm.server.bean.BatchFileBean;
//import com.sunyard.ecm.server.bean.FileBean;
//import com.sunyard.util.OptionKey;
//import com.sunyard.util.TransOptionKey;
//import com.sunyard.ws.utils.XMLUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.net.URL;
//import java.util.List;
//
///**
// * 客户端使用示例
// *
// * @author Warren
// *
// */
//public class LMClient {
//	private final static Logger log = LoggerFactory.getLogger(LMClient.class);
//
//	String ip = "192.168.111.91";
//	int socketPort = 8022;
//	String groupName = "WHGroup"; // 内容存储服务器组名
//	SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ip, socketPort);
//	String BEGIN_COLUMN = "CREATEDATE";
//	static String BEGIN_VALUE = "20200511";
//	// 下载文件的路径
//	static String DOWN_LOAD_FILE_PATH = "D://image/queryFromOffline/" + BEGIN_VALUE + "/";
//	// =========================批次信息设定=========================
//	String modelCode = "TEST"; // 内容模型代码
//	String filePartName = "TEST_PART"; // 文档部件模型代码
//	String userName = "admin";
//	static String contentID = "2016_749_0FBD9A6E-5968-B0A8-5701-402E863967E4-22"; // 8位日期+随机路径+36位GUID+内容存储服务器ID
//	static String contentID2 = "20200703_404_988_A25E671E-4965-C178-143C-BD884B69A001-21"; // 8位日期+随机路径+36位GUID+内容存储服务器ID
//	String fileNO1 = "FF1C004F-08FD-88C2-6C8D-2706EC878EAF";
//	String passWord = "111";
//	String fileNO2s = "";
//	String fileNO3 = "381CA442-A7DC-1D11-59C0-9B493C997167";
//
//	String annoID = "92E0A6BC-94CA-2F47-3EF3-A57FB34A69B5";
//
//	String checkToken = "23ac283f5c07646d4d0c"; // 检入检出随机数
//
//	// =========================批次信息设定=========================
//	public String getContentID() {
//		return contentID;
//	}
//
//	public void setContentID(String contentID) {
//		this.contentID = contentID;
//	}
//
//	/**
//	 * 上传接口调用示例 -------------------------------------------------------
//	 */
//	public void uploadExample() {
//		ClientBatchBean clientBatchBean = new ClientBatchBean();
//		clientBatchBean.setModelCode(modelCode);
//		clientBatchBean.setUser(userName);
//		clientBatchBean.setPassWord(passWord);
//		clientBatchBean.setBreakPoint(false); // 是否作为断点续传上传
//		clientBatchBean.setOwnMD5(true); // 是否为批次下的文件添加MD5码
//
//		// =========================设置索引对象信息开始=========================
//		ClientBatchIndexBean clientBatchIndexBean = new ClientBatchIndexBean();
//		// 索引自定义属性
//		//clientBatchIndexBean.addCustomMap("BUSI_SERIAL_NO", "2014040120");
//		clientBatchIndexBean.addCustomMap(BEGIN_COLUMN, BEGIN_VALUE);
//
//		// clientBatchIndexBean.addCustomMap("STARTDATE", STARTDATE);
//		// clientBatchIndexBean.addCustomMap("STARTDATE", "20130824");
//		// clientBatchIndexBean.addCustomMap("UPDATETIME", "20130828114548");
//		// clientBatchIndexBean.addCustomMap("FILE_DATE", "20130412");
//		// clientBatchIndexBean.addCustomMap("VALID_PERIOD", "20130412");
//		// clientBatchIndexBean.addCustomMap("CONTENT_STATUS", "1");
//		// clientBatchIndexBean.addCustomMap("COMPANY_CODE", "1");
//		// clientBatchIndexBean.addCustomMap("SYSTEM_TYPE", "1");
//		// clientBatchIndexBean.addCustomMap("LANGUAGE", "1");
//		// clientBatchIndexBean.addCustomMap("OPERATOR_ID", "1");
//		// clientBatchIndexBean.addCustomMap("CUSTOMER_NAME", "GG");
//		// clientBatchIndexBean.addCustomMap("CREATEDATE", "20130801");
//		// clientBatchIndexBean.addCustomMap("LOAN", "打算多少电");
//		// =========================设置索引对象信息结束=========================
//
//		// =========================设置文档部件信息开始=========================
//		ClientBatchFileBean clientBatchFileBeanA = new ClientBatchFileBean();
//		clientBatchFileBeanA.setFilePartName(filePartName);
//		// ClientBatchFileBean clientBatchFileBeanB = new ClientBatchFileBean();
//		// clientBatchFileBeanB.setFilePartName("LINK_IMG_B");
//		// =========================设置文档部件信息结束=========================
//
//		// =========================添加文件=========================
//
//		//for (int i = 0; i < 2; i++) {
//		ClientFileBean fileBean1 = new ClientFileBean();
//		// fileBean1.setFileName("E:\\image\\"+i+".jpg");
//		fileBean1.setFileName("D:\\image\\1.jpg");
//		fileBean1.setFileFormat("jpg");
//
//		fileBean1.setFilesize("148670"); // 设置大小图的时候
//		clientBatchFileBeanA.addFile(fileBean1);
//		//}
//		// fileBean1.addOtherAtt(R"RECEIVE_TIME", "20120702");
//		// fileBean1.addOtherAtt("IMAGECONFKIND", "test012");
//		// ClientFileBean fileBean2 = new ClientFileBean();
//		// fileBean2.setFileName("E:\\image\\2.jpg");
//		// fileBean2.setFileFormat("jpg");
//		// ClientFileBean fileBean3 = new ClientFileBean();
//		// fileBean3.setFileName("D:\\3.jpg");
//		// fileBean3.setFileFormat("jpg");
//		// ClientFileBean fileBean4 = new ClientFileBean();
//		// fileBean4.setFileName("D:\\4.jpg");
//		// fileBean4.setFileFormat("jpg");
//		// ClientFileBean fileBean5 = new ClientFileBean();
//		// fileBean5.setFileName("D:\\5.jpg");
//		// fileBean5.setFileFormat("jpg");
//		// fileBean2.addOtherAtt("TEST01","test012");
//		// 文件自定义属性
//		// fileBean1.addOtherAtt("START_TIME", "20120702");
//
//		// ClientFileBean fileBean2 = new ClientFileBean();
//		// fileBean2.setFileName("E:\\testfiles\\2.txt");
//		// fileBean2.setFileFormat("txt");
//		//
//		// ClientFileBean fileBean3 = new ClientFileBean();
//		// fileBean3.setFileName("E:\\testfiles\\3.txt");
//		// fileBean3.setFileFormat("txt");
//		//
//		// ClientFileBean fileBean4 = new ClientFileBean();
//		// fileBean4.setFileName("E:\\testfiles\\4.txt");
//		// fileBean4.setFileFormat("txt");
//
//		// fileBean2.setFilesize("123"); // 设置大小图的时候
//		// 文件自定义属性
//		// fileBean2.addOtherAtt("START_TIME", "20120612");
//		// clientBatchFileBeanB.addFile(fileBean2);
//		// clientBatchFileBeanB.addFile(fileBean3);
//		// clientBatchFileBeanB.addFile(fileBean4);
//		// clientBatchFileBeanB.addFile(fileBean5);
//		// clientBatchFileBean.addFile(fileBean3);
//		// clientBatchFileBean.addFile(fileBean4);
//		// =========================添加文件=========================
//		clientBatchBean.setIndex_Object(clientBatchIndexBean);
//		clientBatchBean.addDocument_Object(clientBatchFileBeanA);
//		try {
//			String resultMsg = clientApi.upload(clientBatchBean, groupName);
//			System.out.println("#######上传批次返回的信息[" + resultMsg + "]#######");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 查询接口调用示例 -------------------------------------------------------
//	 */
//	public void queryExample(String batchId) {
//		ClientBatchBean clientBatchBean = new ClientBatchBean();
//		clientBatchBean.setModelCode(modelCode);
//		clientBatchBean.setUser(userName);
//		clientBatchBean.setPassWord(passWord);
//		clientBatchBean.setDownLoad(true);
//		clientBatchBean.getIndex_Object().setContentID(batchId);
//		clientBatchBean.getIndex_Object().addCustomMap("BUSI_DATE", "20140906");
//		clientBatchBean.getIndex_Object().setVersion("0");
//		ClientBatchFileBean documentObjectB = new ClientBatchFileBean();
//		documentObjectB.setFilePartName("HOUDU_P"); // 要查询的文档部件名
//		// documentObjectB.addFilter("FILE_NO",
//		// "D02716BB-F9CA-3F85-477D-25069D8CCF6B"); // 增加过滤条件
//		// documentObjectB.addFilter("SQL_FILTER",
//		// " FILE_NO='D02716BB-F9CA-3F85-477D-25069D8CCF6B' or FILE_NO ='D02716BB-F9CA-3F85-477D-25069D8CCF6B' ");
//
//		clientBatchBean.addDocument_Object(documentObjectB);
//
//		try {
//			String resultMsg = clientApi.queryBatch(clientBatchBean, groupName);
//			log.debug("#######查询批次返回的信息[" + resultMsg + "]#######");
//			System.out.println("#######查询批次返回的信息[" + resultMsg + "]#######");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 高级搜索调用示例 -------------------------------------------------------
//	 * 最后结果为组上最大版本的批次号
//	 */
//	public void heightQueryExample() {
//		ClientHeightQuery heightQuery = new ClientHeightQuery();
//		heightQuery.setUserName(userName);
//		heightQuery.setPassWord(passWord);
//		heightQuery.setLimit(10);
//		heightQuery.setPage(1);
//		heightQuery.setModelCode(modelCode);
//		heightQuery.addCustomAtt("TEST", "1");
//		// heightQuery.addCustomAtt("BUSI_SERIAL_NO", "2013092701");
//		// heightQuery.addfilters("VARCHARTYPE='varchartype'");
//		try {
//			String resultMsg = clientApi.heightQuery(heightQuery, "WHGroup");
//			log.info("#######调用高级搜索返回的信息[" + resultMsg + "]#######");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 删除接口调用示例 -------------------------------------------------------
//	 */
//	public void deleteExample() {
//		ClientBatchBean clientBatchBean = new ClientBatchBean();
//		clientBatchBean.setModelCode(modelCode);
//		clientBatchBean.setPassWord(passWord);
//		clientBatchBean.setUser(userName);
//		clientBatchBean.getIndex_Object().setContentID(contentID);
//		clientBatchBean.getIndex_Object().addCustomMap(BEGIN_COLUMN, BEGIN_VALUE);
//		// 若内容模型配置有安全校验
//		// clientBatchBean.setToken_check_value(token_check_value);
//		// clientBatchBean.setToken_code(tokenCode);
//		System.out.println(XMLUtil.bean2XML(clientBatchBean));
//
//		try {
//			// String resultMsg = clientApi.delete(clientBatchBean, groupName);
//			// log.debug("#######删除批次返回的信息[" + resultMsg + "]#######");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 更新时需要注明版本号则表示自第几个版本更新,
//	 * ------------------------------------------------------- 没有版本控制则无需注明
//	 *
//	 */
//	public void updateExample(String contentID) {
//		ClientBatchBean clientBatchBean = new ClientBatchBean();
//		clientBatchBean.setModelCode(modelCode);
//		clientBatchBean.setUser(userName);
//		clientBatchBean.setPassWord(passWord);
//		clientBatchBean.getIndex_Object().setContentID(contentID);
//
//		clientBatchBean.getIndex_Object().addCustomMap(BEGIN_COLUMN, BEGIN_VALUE);
//
//		ClientBatchFileBean batchFileBean = new ClientBatchFileBean();
//		batchFileBean.setFilePartName(filePartName);
//
//		// // 新增一个文件
//		ClientFileBean fileBean1 = new ClientFileBean();
//		fileBean1.setOptionType(OptionKey.U_ADD);
//		fileBean1.setFileName("D:\\image\\2.jpg");
//		fileBean1.setFileFormat("jpg");
//		batchFileBean.addFile(fileBean1);
//		// //
//		//
//		// // 替换一个文件
//		// ClientFileBean clientFileBean2 = new ClientFileBean();
//		// clientFileBean2.setOptionType(OptionKey.U_REPLACE);
//		// clientFileBean2.setFileNO("C7DBF2A4-7603-0DEC-144F-01A1EF2C7C53");
//		// clientFileBean2.setFileName("e:/image/5.jpg");
//		// clientFileBean2.setFileFormat("jpg");
//		// batchFileBean.addFile(clientFileBean2);
//
//		// 删除一个文件
////		ClientFileBean clientFileBean3 = new ClientFileBean();
////		clientFileBean3.setOptionType(OptionKey.U_DEL);
////		clientFileBean3.setFileNO("C7DBF2A4-7603-0DEC-144F-01A1EF2C7C53");
////		batchFileBean.addFile(clientFileBean3);
//		//
//		//
//		// // // 修改文档部件字段
//		// ClientFileBean clientFileBean = new ClientFileBean();
//		// clientFileBean.setOptionType(OptionKey.U_MODIFY);
//		// clientFileBean.setFileNO("6DE52B71-3818-1A5D-5BFF-B9684C1DAC11");
//		// clientFileBean.addOtherAtt("START_DATA", "1");
//		// batchFileBean.addFile(clientFileBean);
//		// //
//		clientBatchBean.addDocument_Object(batchFileBean);
//		try {
//			String resultMsg = clientApi.update(clientBatchBean, "WHGroup", true);
//			log.debug("#######更新批次返回的信息[" + resultMsg + "]#######");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//	/**
//	 * 查询影像并将影像下载到DOWN_LOAD_FILE_PATH目录下
//	 */
//	public void queryAndDownload(String contentID) {
//		ClientBatchBean clientBatchBean = new ClientBatchBean();
//		clientBatchBean.setModelCode(modelCode);
//		clientBatchBean.setUser(userName);
//		clientBatchBean.setPassWord(passWord);
//		// clientBatchBean.getIndex_Object().setVersion("2");
//		clientBatchBean.getIndex_Object().setContentID(contentID);
//		clientBatchBean.getIndex_Object().addCustomMap(BEGIN_COLUMN, BEGIN_VALUE);
//		clientBatchBean.setDownLoad(true);
//		try {
//			String resultMsg = clientApi.queryBatch(clientBatchBean, groupName);
//			log.info("#######查询批次返回的信息[" + resultMsg + "]#######");
//
//			String batchStr = resultMsg.split(TransOptionKey.SPLITSYM)[1];
//
//			List<BatchBean> batchBeans = XMLUtil.xml2list(XMLUtil.removeHeadRoot(batchStr), BatchBean.class);
//			for (BatchBean batchBean : batchBeans) {
//				List<BatchFileBean> fileBeans = batchBean.getDocument_Objects();
//				for (BatchFileBean batchFileBean : fileBeans) {
//					List<FileBean> files = batchFileBean.getFiles();
//					for (FileBean fileBean : files) {
//						String urlStr = fileBean.getUrl();
//						String fileName = fileBean.getFileNO() + "-" + Thread.currentThread().getId() + "." + fileBean.getFileFormat();
//						log.debug("#######文件访问链接为[" + urlStr + "], 文件名为[" + fileName + "]#######");
//						// 调用下载文件方法
//						receiveFileByURL(urlStr, fileName, contentID);
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 将文件下载到DOWN_LOAD_FILE_PATH路径下
//	 *
//	 * @param urlStr
//	 * @param fileName
//	 * @param contentID
//	 *            批次号
//	 */
//	private void receiveFileByURL(String urlStr, String fileName, String contentID) {
////		String path = DOWN_LOAD_FILE_PATH + "/" + contentID + "/";
//		String path = DOWN_LOAD_FILE_PATH + "/" + contentID + "/";
//		File file = new File(path + fileName);
//		File pareFile = file.getParentFile();
//		if (pareFile == null || !pareFile.exists()) {
//			log.info("no parefile ,begin to create mkdir,path=" + pareFile.getPath());
//			pareFile.mkdirs();
//		}
//
//		URL url;
//		InputStream in = null;
//		FileOutputStream fos=null;
//		try {
//			url = new URL(urlStr);
//			in = url.openStream();
//			fos = new FileOutputStream(file);
//			if (in != null) {
//				byte[] b = new byte[1024];
//				int len = 0;
//				while ((len = in.read(b)) != -1) {
//					fos.write(b, 0, len);
//				}
//			}
//		} catch (FileNotFoundException e) {
//			log.error("unitedaccess http -- GetFileServer: " + e.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (in != null) {
//					in.close();
//				}
//				if (fos != null) {
//					fos.close();
//				}
//			} catch (IOException e) {
//				log.error("unitedaccess http -- GetFileServer: " + e.toString());
//			}
//		}
//	}
//
//
////	public static void main(String[] args) {
////		LMClient client = new LMClient();
////		//更新接口
//////		client.updateExample(contentID);
////		// 上传接口
//////		 client.uploadExample();
////		// 高级查询接口
//////		 client.heightQueryExample();
////		//详细查询接口
////		 client.queryExample("20200703_404_988_A25E671E-4965-C178-143C-BD884B69A001-21");
////		//影像下载接口
//////		 client.queryAndDownload(contentID);
////
////	}
//
//}
