///////////////////////////////////////////////////////////////
//文件名称： JbfImageMng.js
//文件功能： 为IE封装京北方图像管理控件
//公司名称： 北京京北方科技
//建立时间： 2005-12-01
//建立作者： 郑邦东
//修改时间： 
//修改人名： bd.zheng@northking.net,stdio_01@163.com
//使用方法： 
//          在head域直接引入页面就可以使用CJbfImageMng类
//文件描述：
//
//
//----------------------------------------------------------------------------
//类名   CJbfImageMng
//功能   封装页面京北方图像管理控件的类,调用我们公司自己的图像处理控件
//参数   
//说明   在页面中的使用例子
//      <html><head>
//		<SCRIPT LANGUAGE="JavaScript" src="js/JbfImageMng.js"></SCRIPT>
//		<SCRIPT LANGUAGE="JavaScript">
//		<!--
//			var g_oImageMng = new CJbfImageMng
//		-->
//		</SCRIPT>
//		</head><body>
//		<SCRIPT LANGUAGE="JavaScript">
//		<!--  
//			InitLpk();
//			obj.InitImageMng("JBFImageMng_01",800,600);
//			obj.LoadListImage("c:\\1.jpg");
//			obj.ShowFirstPage();
//		-->
//		</SCRIPT>
//		</body></html>
//调用接口 方法类
//      void	InitImageMng(id,width,height)                 //初始化图像管理控件
//-----------------------------------------------------------------------------
//      boolean LoadBatchImage(strbatchpath)                  //加载一批图像
//
//  需要获得消息，实现 GetScanEvent_OnTwainScanOneOver 函数里面的内容
//
//////////////////////////////////////////////////////////////
//名称   InitLpk
//功能   初始化权限验证文件
function InitLpk()
{
	document.write("<OBJECT CLASSID=\"CLSID:5220CB21-C88D-11CF-B347-00AA00A28331\">");
	//document.write("<PARAM NAME=\"LPKPath\" VALUE=\"/icc/activeX/scan/BJJbfImageMng.LPK\">");
	document.write("<PARAM NAME=\"LPKPath\" VALUE=\"/scan/BJJbfImageMng.LPK\">");
	document.write("</OBJECT>");
}

//////////////////////////////////////////////////////////////
//名称   CJbfImageMng
//功能   CJbfImageMng封装类
function CJbfImageMng()
{
	var m_szName              = "JBFImageMng001";
	var m_nWidth              = 800;
	var m_nHeight             = 600;
	var m_bInitFinished       = 0;                   //是否初始化完成
	var m_fZoomRate           = 0.5;

  var m_nMaxRows            = 2;
  var m_nMaxCols            = 2;

	//var m_szConstVersion      = "1,0,4,23";
	//var m_szConstVersion      = "1,0,4,51"; 
  	var m_szConstVersion      = "1,0,4,80"; 
  
	var m_szConstID           = "2077BC61-659B-4DF5-BA08-8996FBB78A6D";
	var m_szPackagePath       = "/scan/JBFImgMng.CAB";

	this.SetName   = function(name) {Name  = name;}
	this.GetName   = function()     {return  Name;}

	this.SetWidth  = function(width){Width = width;}
	this.GetWidth  = function()     {return  Width;}

	this.SetHeight = function(height) {Height = height;}
	this.GetHeight = function() {return Height;}

	this.SetInitFlag = function(flag)  {m_bInitFinished = flag;}
	this.GetInitFlag = function()      {return m_bInitFinished;}

	this.SetZoomRate = function(zoom)  {m_fZoomRate = zoom;}
	this.GetZoomRate = function()      {return m_fZoomRate;}

	this.GetVersion  = function()      {return m_szConstVersion;}

	this.GetClassID  = function()      {return m_szConstID;}

	this.SetPackagePath    = function(packagepath)  {m_szPackagePath = packagepath;}
	this.GetPackagePath    = function()      {return m_szPackagePath;}

	this.obj = function() {return document.getElementById(Name);}
	
	this.SetMaxRows = function(maxrows) {m_nMaxRows = maxrows;}
	this.GetMaxRows = function() {return m_nMaxRows;}
	
	this.SetMaxCols = function(maxcols) {m_nMaxCols = maxcols;}
	this.GetMaxCols = function() {return m_nMaxCols;}
	
	//this.GetErrDes = function() {return m_szErrDes;}

	this.m_szErrDes = new Array(
	0,"成功",
	1,"装载的目录不存在",
	2,"当前用户对目录没有访问权限"
	);
	this.TwainSetDuplexDes = new Array(
	0,"成功",
	1,"设置单双面扫描失败,未知原因",
	2,"设置单双面扫描失败,可能不支持双面扫描"
	);
	this.TwainSetContrast = new Array(
	0,"成功",
	1,"超出范围，不在[0-255]之间",
	2,"从设备获取范围失败！"
	);
}

//////////////////////////////////////////////////////////////
//名称   InitImageMng
//功能   在IE页面中初始化图像控件
//参数   
//       name    在页面中的ID，要唯一区别于其他页面中的元素ID或者名称
//       width   控件的宽度，像素
//       height  控件的高度，像素
//说明   这个方法一定要在其他方法使用之前调用,不指属性设置函数 如：SetPackagePath()。
CJbfImageMng.prototype.InitImageMng = function(name,width,height)
{
	//初始化图像处理控件
	if (this.GetInitFlag() == 1) return;

	this.SetName(name);
	this.SetWidth(width);
	this.SetHeight(height);
	document.write ("<CENTER><OBJECT ");
	document.write ("ID='" + this.GetName() + "' WIDTH='" + this.GetWidth() + "' HEIGHT='" + this.GetHeight() +  "' ");
	document.write ("CLASSID='CLSID:" + this.GetClassID() + "' CODEBASE='" + this.GetPackagePath() + "#version=" + this.GetVersion() + "'>");
	document.write ("<PARAM NAME=\"Vender\" VALUE=\"北京京北方科技股份有限公司\"></OBJECT></CENTER>");
	
	//绑定事件 --  扫描完成一张
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='TwainScanOneOver(strFileName,nSize,bObversed)'>");
	document.write("GetScanEvent_OnTwainScanOneOver(strFileName,nSize,bObversed);");
	document.write("</SCRIPT>");
	//绑定事件 --  扫描结束
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='TwainScanOver(flag,szDes)'>");
	document.write("GetScanEvent_OnTwainScanOver(flag,szDes);");
	document.write("</SCRIPT>");	
	//绑定事件 --  装载图像完成
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='LoadOk(count)'>");
	document.write("GetImgEvent_OnLoadOk(count);");
	document.write("</SCRIPT>");
	//绑定事件 --  传输完成一张
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='UploadOnefile(filepath,filesize,nflag)'>");
	document.write("GetScanEvent_OnUploadOnefile(filepath,filesize,nflag);");
	document.write("</SCRIPT>");
	//绑定事件 --  显示的当前页数以及总页数
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='SelectImage(nIndex,nCount)'>");
	document.write("GetScanEvent_OnSelectImage(nIndex,nCount);");
	document.write("</SCRIPT>");
	//绑定事件 --  控件加载完成
	document.write("<SCRIPT FOR='" + this.GetName() + "' EVENT='InitFinish()'>");
	document.write("GetScanEvent_OnInitFinish();");
	document.write("</SCRIPT>");

	this.SetInitFlag(1);
}


//////////////////////////////////////////////////////////////
//名称   GetScanEvent_OnTwainScanOneOver
//功能   扫描完成一张图像接收的事件
//参数   
//			 strFileName	文件名称
//			 nSize        文件大小
//			 bObversed    正反面
//返回   
function GetScanEvent_OnTwainScanOneOver(strFileName,nSize,bObversed)
{
	  if (nSize == 0)
	  {
	  	alert("保存文件：" + strFileName + " 失败，原因是色彩模式不匹配！");
	  }
	  else
	  {
	  	 try
	  	 {
	  	 		OnTwainScanOneOver(strFileName,nSize,bObversed)
	  	 }
	  	 catch(e)
	  	 {
	  	     alert("Please implement OnTwainScanOneOver(strFileName,nSize,bObversed)! 消息信息为： 文件名称：　"+ strFileName +"尺寸：　" +　nSize　+ "正方面："　+ bObversed);		
	  	 }
	  }
}

//////////////////////////////////////////////////////////////
//名称   GetImgEvent_OnLoadOk
//功能   装载一批图像完成
//参数   
//			  count   数量
//返回   
function GetImgEvent_OnLoadOk(count)
{
		   try
	  	 {
	  	 		OnImageLoadOk(count);
	  	 }
	  	 catch(e)
	  	 {
	  	     alert("Please implement OnImageLoadOk(count)! 消息信息为：数量 " + count);
	  	 }
}

//////////////////////////////////////////////////////////////
//名称   GetScanEvent_OnTwainScanOver
//功能   扫描完成接收的事件
//参数   
//			 flag  标志信息 0 是 扫描一次完成指自动进纸的时候，放的纸已经扫描完成 1 是扫描结束
//			 szDes 描述信息
//返回   
function GetScanEvent_OnTwainScanOver(flag,szDes)
{
//	  	try
//	  	 {
//	  	 	OnTwainScanOver(flag,szDes);
//	  	 }
//	  	 catch(e)
//	  	 {
//	  	     alert("Please implement OnTwainScanOver(flag,szDes)! 消息信息为：标志 " + flag + "描述 " + szDes);
//	  	 }
}

//////////////////////////////////////////////////////////////
//名称   GetScanEvent_OnInitFinish()
//功能   控件加载完成,实现方式有问题，暂时不支持，仍然使用Settimeout初始化
//参数   
//返回   

function GetScanEvent_OnInitFinish()
{
	try
	 {
		OnInitFinish();
	 }
	 catch(e)
	 {
		 alert("Please implement OnInitFinish()! 消息信息为控件已经加载完成");
	 }
}

//////////////////////////////////////////////////////////////
//名称   GetScanEvent_OnSelectImage(nIndex,nCount)
//功能   扫描完成接收的事件
//参数   
//			 nIndex  当前页序号，此页序号是指某个文件在整个扫描文件中的序号，和显示的页不属于同一概念
//			 nCount  总页数
//返回   
function GetScanEvent_OnSelectImage(nIndex,nCount)
{
	try
	 {
		OnSelectImage(nIndex,nCount);
	 }
	 catch(e)
	 {
		 alert("Please implement OnSelectImage(nIndex,nCount)! 消息信息为：第 " + nIndex + " 页/共 " + nCount + " 页");
	 }
}


//////////////////////////////////////////////////////////////
//名称   GetScanEvent_OnUploadOnefile
//功能   上传一张图像完成接收的事件
//参数   
//           filepath -  文件名称
//           filesize - 文件大小
//			 nflag -   标志信息 
//                 -1 出现异常，异常信息有filepath带回 
//                0 失败
//                1 成功
//                2 打开本地文件失败
function GetScanEvent_OnUploadOnefile(filepath,filesize,nflag)
{
	try{
		OnUploadOnefile(filepath,filesize,nflag);
	}catch(e)
	{
        alert("Please implement OnUploadOnefile(filepath,filesize,nflag)! 消息信息为：文件 " + filepath + " 长度 " + filesize + " 结果标志 " + nflag);
	}
}


//////////////////////////////////////////////////////////////
//名称   GetLastErrorNo
//功能   获取错误代码
//参数   
//返回   
CJbfImageMng.prototype.GetLastErrorNo = function()
{
	return this.obj().GetLastErrorNo();
}

//////////////////////////////////////////////////////////////
//名称   GetLastErrorDes
//功能   获取错误描述
//参数   errno 错误代码，为空，返回最后错误描述
//返回   
CJbfImageMng.prototype.GetLastErrorDes = function(errno)
{
	var ErrNo = (errno == null ? this.GetLastErrorNo() : errno);
	for (var i=0;i<this.m_szErrDes.length; i=i+2 )
	{
		if (this.m_szErrDes[i] == ErrNo)
		{
			return this.m_szErrDes[i+1];
		}
	}
	return "没有指明的错误信息，请联系程序开发商！";
}

//////////////////////////////////////////////////////////////
//名称   SetCells
//功能   设置单元格的个数
//参数   rows: 行数
//       cols: 列数 
//返回   成功的 0 成功 其他失败
//说明   注意，目前支持 8*8
CJbfImageMng.prototype.SetImageCells = function(rows,cols)
{
	var nRet = this.obj().ImgSetImageShowCells(rows,cols);
	if (nRet != 0)
	{
		this.SetMaxRows(rows);
		this.SetMaxCols(cols);
	}
		
	return nRet;
}

//////////////////////////////////////////////////////////////
//名称   LoadBatchImage
//功能   加载一个目录的图像到图像处理控件里，但是不会显示，要显示需要用显示影像的函数
//参数   batchpath 批量图像所在的目录，全路径名称
//返回   true 成功 false 失败
//说明   注意，这里没有校验目录，即使你传一个非法的，控件也认为是正确的
CJbfImageMng.prototype.LoadBatchImage = function(batchpath)
{
	return this.obj().ImgLoadBatchImage(batchpath);
}

//////////////////////////////////////////////////////////////
//名称   LoadListImage
//功能   加载一批图像到图像处理控件里，但是不会显示，要显示需要用显示影像的函数
//参数   filelist 格式： filepathname|filepathname|filepathname
//返回   true 成功 false 失败
//说明   注意，这里没有校验目录，即使你传一个非法的，控件也认为是正确的
CJbfImageMng.prototype.LoadListImage = function(filelist)
{
	return this.obj().ImgLoadListImage(filelist);
}

//////////////////////////////////////////////////////////////
//名称   AppendImage
//功能   添加一张指定的图像到控件的列表中
//参数   strfilename 文件名称全路径
//返回   true 成功 false 失败
//说明   需要先调用加载图像的函数
CJbfImageMng.prototype.AppendImage = function(strfilename)
{
	return this.obj().ImgAppendImage(strfilename);
}

//////////////////////////////////////////////////////////////
//名称   ShowFirstPage
//功能   显示列表中的第一张影像
//参数   
//返回   显示成功的图像个数
CJbfImageMng.prototype.ShowFirstPage = function()
{
	return this.obj().ImgShowFirstPage(); 
}

//////////////////////////////////////////////////////////////
//名称   ShowPreviousPage
//功能   显示列表中 当前显示图像的上一张影像
//参数   
//返回   显示成功的图像个数
CJbfImageMng.prototype.ShowPreviousPage = function()
{
	return this.obj().ImgShowPreviousPage();
}

//////////////////////////////////////////////////////////////
//名称   ShowNextPage
//功能   显示列表中 当前显示图像的下一张影像
//参数   
//返回   显示成功的图像个数
CJbfImageMng.prototype.ShowNextPage = function()
{
	return this.obj().ImgShowNextPage();
}

//////////////////////////////////////////////////////////////
//名称   ShowLastPage
//功能   显示列表中的最后一张影像
//参数   
//返回   显示成功的图像个数
CJbfImageMng.prototype.ShowLastPage = function()
{
	return this.obj().ImgShowLastPage();
}

//////////////////////////////////////////////////////////////
//名称   ZoomImage
//功能   调整图像大小
//参数   
//       fzoomrate 浮点数 
//       范围 r = (0.03,16) 表示显示的倍数 1 表示原图大小显示 
//返回   
CJbfImageMng.prototype.ZoomImage = function(fzoomrate)
{
	fzoomrate = fzoomrate == null ? this.GetZoomRate() : fzoomrate;
	this.SetZoomRate(fzoomrate);
	this.obj().ImgZoomImage(fzoomrate);
}

//////////////////////////////////////////////////////////////
//名称   ZoomRate
//功能   放大图像
//参数   
//       fzoomrate 浮点数 
//       范围 r = (0,10) 表示显示的倍数 1 表示原图大小显示 
//返回   
//说明   如果没有传递参数 就是用默认设置,默认设置是0.5
CJbfImageMng.prototype.ZoomRate = function(fzoomrate)
{
	if (fzoomrate == null)
	{
		fzoomrate = 0.5;
	}
	this.obj().ImgZoomImage(fzoomrate);
}

//////////////////////////////////////////////////////////////
//名称   ZoomOut
//功能   放大图像
//参数   
//       fzoomrate 浮点数 
//       范围 r = (0,10) 表示显示的倍数 1 表示原图大小显示 
//返回   
//说明   如果没有传递参数 就是用默认设置,默认设置是再放大0.2的参数
CJbfImageMng.prototype.ZoomOut = function(fzoomrate)
{
	if (fzoomrate == null)
	{
		this.SetZoomRate(this.GetZoomRate() + this.GetZoomRate() * 0.2);
	}
	else
	{
		this.SetZoomRate(fzoomrate);
	}
	this.obj().ImgZoomImage(this.GetZoomRate());
}

//////////////////////////////////////////////////////////////
//名称   ZoomIn
//功能   放大图像
//参数   
//       fzoomrate 浮点数 
//       范围 r = (0,10) 表示显示的倍数 1 表示原图大小显示 
//返回   
//说明   如果没有传递参数 就是用默认设置,默认设置是再 缩小0.2的参数
CJbfImageMng.prototype.ZoomIn = function(fzoomrate)
{
	if (fzoomrate == null)
	{
		this.SetZoomRate(this.GetZoomRate() - this.GetZoomRate() * 0.2);
	}
	else
	{
		this.SetZoomRate(fzoomrate);
	}
	this.obj().ImgZoomImage(this.GetZoomRate());
}

//////////////////////////////////////////////////////////////
//名称   ShowPageByIndex
//功能   根据索引顺序显示图像
//参数   iindex 索引数
//返回   true 成功 false 失败
//说明   参数为空，显示当前图像
CJbfImageMng.prototype.ShowPageByIndex = function (lindex)
{
   var index = parseInt(lindex,10);
   if (lindex == null)
   {
	   return true;
   }
   if (isNaN(index) || index < 0)
   {
	   index = 0;
   }
   if (index >= this.GetImagesCount())
   {
	   flag = this.GetImagesCount() - 1;
   }
   return this.obj().ImgShowFileByIndex(index);
}

//////////////////////////////////////////////////////////////
//名称   GetImagesCount
//功能   得到列表中可显示的图像个数
//参数  
//返回   
CJbfImageMng.prototype.GetImagesCount = function ()
{
   return this.obj().ImgGetImageCount();
}

//////////////////////////////////////////////////////////////
//名称   GetImageFileName
//功能   得到指定位置的影像名称
//参数   iindex 文件序号
//返回   
CJbfImageMng.prototype.GetImageFileName = function (index)
{
   return this.obj().ImgGetFileNameByIndex(index);
}

//////////////////////////////////////////////////////////////
//名称   PrintImage
//功能   打印图像
//参数   
//返回   
CJbfImageMng.prototype.PrintImage = function()
{
	return this.obj().ImgPrint();
}

//////////////////////////////////////////////////////////////
//名称   Rotate
//功能   旋转图像
//参数   
//返回   
CJbfImageMng.prototype.Rotate = function(fangle)
{
	//alert("按照９０度旋转，可以自己设定角度");
	return this.obj().ImgRotate(90);
}

//////////////////////////////////////////////////////////////
//名称   ImgAutoTrim
//功能   去边
// 参  数: [in] short nEdge - 边缘标志 0-黑边 1-白边
//       : [in] short nThreshold - 前景与背景的最小亮度差值
//       : [in] long nPixelNum - 边缘上的最大象素个数
//       : [in] float fratio - 边缘上象素所占的最小比例   
//返回   
CJbfImageMng.prototype.AutoTrim = function(nEdge,nThreshold, nPixelNum,fratio)
{
	//alert("去黑边\r\n 前景与背景的最小亮度差值：20 \r\n 边缘上的最大象素个数：0 \r\n 边缘上象素所占的最小比例:1%");
	return this.obj().ImgAutoTrim(0,20,0,0.01);
}


///////////////////////////////////////////////////////////////
//
//					扫描仪的设置
//
//////////////////////////////////////////////////////////////
//名称   SelectSource
//功能   选择扫描仪
//参数   
//返回   
CJbfImageMng.prototype.SelectSource = function()
{
	return this.obj().TwainSelectSource();
}

//选择扫描仪
CJbfImageMng.prototype.ChoiceScanner = function()
{
	return this.SelectSource();
}

//扫描时，是否显示界面
CJbfImageMng.prototype.SetShowUI = function(flag)
{
	return this.obj().TwainSetShowUI(flag);
}

//扫描时，是否自动进纸
CJbfImageMng.prototype.SetAutoFeed = function(flag)
{
	return this.obj().TwainSetAutoFeed(flag);
}


//////////////////////////////////////////////////////////////
//名称   SetScanPath
//功能   设置路径
//参数   
//返回   
CJbfImageMng.prototype.SetScanPath = function(pathfile)
{
	return this.obj().ImgSetPathNameforSave(pathfile);
}

//////////////////////////////////////////////////////////////
//名称   Scan
//功能   扫描
//参数   
//返回   
CJbfImageMng.prototype.Scan = function()
{
	return this.obj().TwainAcquire();
}

//////////////////////////////////////////////////////////////
//名称   Rescan
//功能   重新扫描
//参数   
//返回   
//说明：应该先插入，然后再删除。以后有时间在修改
CJbfImageMng.prototype.Rescan = function()
{
	return this.obj().TwainReplace(0);
}

//////////////////////////////////////////////////////////////
//名称   InsertScan
//功能   插入
//参数   
//返回   
CJbfImageMng.prototype.InsertScan = function()
{
	this.obj().TwainInsertFile();
}

//////////////////////////////////////////////////////////////
//名称   DeleteFile
//功能   删除扫描的文件
//参数   -1 删除当前选中的，其他的，以后补充 -----
//       -1 delete current choiced file
//       -2 delete all file
//		   -3 delete all file after current choiced file
//       -4 delete all file befor current choiced file
//       >=0 && < count, delete file in pagelist's nindex 
//返回   
CJbfImageMng.prototype.DeleteFile = function(flag)
{
	this.obj().TwainDeleteFile(flag);
}

CJbfImageMng.prototype.DeleteCurrentFile = function()
{
	this.obj().TwainDeleteFile(-1);
}

CJbfImageMng.prototype.DeleteAllFiles = function()
{
	this.obj().TwainDeleteFile(-2);
}

CJbfImageMng.prototype.DeleteCurAfterFiles = function()
{
	this.obj().TwainDeleteFile(-3);
}

//////////////////////////////////////////////////////////////
//名称   SetScanPath
//功能   设置扫描路径
//参数   
//返回   
CJbfImageMng.prototype.SetScanPath = function(filepath)
{
	this.obj().ImgSetPathNameforSave(filepath);
}


//////////////////////////////////////////////////////////////
//名称   SetMaxPages
//功能   设置最大扫描页数
//参数   
//返回   
CJbfImageMng.prototype.SetMaxPages = function(maxpages)
{
	this.obj().TwainSetMaxPageNum(maxpages);
}

//////////////////////////////////////////////////////////////
//名称   TwainSetEnableDuplex
//功能   设置扫描单双面
//参数   flag  0 单面
//			   1 双面	
//返回   
CJbfImageMng.prototype.SetEnableDuplex = function(flag)
{
	 return this.obj().TwainSetEnableDuplex(flag);
}

//////////////////////////////////////////////////////////////
//名称   SetImgResolution
//功能   设置扫描分辨率X
//参数   
//返回   
CJbfImageMng.prototype.SetImgResolution = function(xRes,yRes)
{
	this.SetImgXResolution(xRes);
	this.SetImgYResolution(yRes);
}


//////////////////////////////////////////////////////////////
//名称   SetImgXResolution
//功能   设置扫描分辨率X
//参数   
//返回   
CJbfImageMng.prototype.SetImgXResolution = function(xRes)
{
	this.obj().TwainSetImgXResolution(xRes);
}


//////////////////////////////////////////////////////////////
//名称   SetImgYResolution
//功能   设置扫描分辨率Y
//参数   
//返回   
CJbfImageMng.prototype.SetImgYResolution = function(yRes)
{
	this.obj().TwainSetImgYResolution(yRes);
}

//////////////////////////////////////////////////////////////
//名称   SetColorMode
//功能   设置扫描颜色深度
//参数   
//返回   
CJbfImageMng.prototype.SetColorMode = function(nCurSel)
{

	var bits;
	if (nCurSel == 0)
	{
		bits = 1;
	}
	else if (nCurSel == 1)
	{
		bits = 4;
	}
	else if (nCurSel == 2)
	{
		bits = 24;
	}
	try{
	this.obj().TwainSetImgPixelBits(bits);
	this.obj().TwainSetColorMode(nCurSel);
	}
	catch(e)
	{
		alert("出现错误：" + e);
	}
		
}

//////////////////////////////////////////////////////////////
//名称   SetImgContrast
//功能   设置扫描图像对比度
//参数   
//返回  0 成功 1 不在 (0-255) 2 获取信息失败
CJbfImageMng.prototype.SetImgContrast = function(nContrast)
{
	return this.obj().TwainSetImgContrast(nContrast);
}

//////////////////////////////////////////////////////////////
//名称   SetImgBrightness
//功能   设置扫描图像亮度
//参数   
//返回    0 成功 1 不在 (0-255) 2 获取信息失败
CJbfImageMng.prototype.SetImgBrightness = function(nBright)
{
	return this.obj().TwainSetImgBrightness(nBright);
}

//////////////////////////////////////////////////////////////
//名称   SaveAs
//功能   图像另存为
//参数   
//返回   
CJbfImageMng.prototype.SaveAs = function(srcfilename)
{
	return this.obj().ImgSaveAs(srcfilename);
}

//////////////////////////////////////////////////////////////
//名称   SaveAs
//功能   图像另存为
//参数   
//返回   
CJbfImageMng.prototype.GetCurrentImageName = function()
{
	return this.obj().ImgGetFileNameByIndex(this.obj().ImgGetCurrentImageIndex());
}

//////////////////////////////////////////////////////////////
//名称   FilterWhite
//功能   图像另存为
//参数   
//返回   
CJbfImageMng.prototype.FilterWhite = function(flag)
{
	//alert("参数为２，只滤背面");
	this.obj().ImgRemoveWhitePage(flag);
}

//////////////////////////////////////////////////////////////
//名称   FilterWhite
//功能   图像另存为
//参数   
//返回   
CJbfImageMng.prototype.SetShowType = function(flag)
{
	//alert("参数为0，显示全部１显示正面　２显示背面");
	this.obj().ImgSetShowway(flag);
}

//////////////////////////////////////////////////////////////
//名称   ShowUrlFile
//功能   显示http或者file://的文件，也能显示本地的文件
//参数   
//返回   
CJbfImageMng.prototype.ShowUrlFile = function(filename)
{
	//alert("参数为0，显示全部１显示正面　２显示背面");
	this.obj().ImgShowFile(filename);
}

//////////////////////////////////////////////////////////////
//名称   SetImageType
//功能   设置文件的格式类型 
//参数   0 BMP 1 JPG 2 TIF
//返回   
CJbfImageMng.prototype.SetImageType = function(imagetype)
{
	this.obj().ImgSetType(imagetype);
}

//////////////////////////////////////////////////////////////
//名称   SetImageType
//功能   设置文件的格式类型 
//参数   0 BMP 1 JPG 2 TIF
//返回   
CJbfImageMng.prototype.TwainGetImgBrightnessInfo = function( VMin, VMax, VStep,VDefault)
{
	this.obj().TwainGetImgBrightnessInfo(VMin,VMax,VStep,VDefault);
}

//////////////////////////////////////////////////////////////
//名称   SetParameters
//功能   弹出参数设置对话框，设置部分参数 
//参数   
//返回   
CJbfImageMng.prototype.SetParameters = function()
{
	this.obj().TwainSetParameter();
}

//////////////////////////////////////////////////////////////
//名称   Close
//功能   关闭扫描设备,在页面的unload里面调用
//参数   
//返回   
CJbfImageMng.prototype.Close = function()
{
	this.obj().Close();
}


//////////////////////////////////////////////////////////////
//名称   GetBase64StringFrom
//功能   得到图像的base64的字符
//参数   文件名称
//返回   
CJbfImageMng.prototype.GetBase64StringFrom = function(FilePath,lReserved)
{
	return this.obj().GetBase64StringFrom(FilePath,lReserved);
}


//////////////////////////////////////////////////////////////
//名称   GetBase64StringFrom
//功能   得到图像的base64的字符
//参数   文件名称
//返回   
CJbfImageMng.prototype.TwainSetDeletefileFlag = function(isRealDelete)
{
	return this.obj().TwainSetDeletefileFlag(isRealDelete);
}


//////////////////////////////////////////////////////////////////////
// 函数名: SetDemoImgInfo
// 功  能: 为扫描仪提供虚拟的扫描结果，预先准备好图像，依照顺序模拟扫描，出现预先定义的图像结果
// 返  回: void  - 
// 参  数: [in] LPCTSTR FilenameTemplate - 预定义图像的路径名称,路径和文件名直接加起来成为文件的绝对路径。
//       : [in] long DemoImgMaxCount - 最多图像张数，当为0时候取消DEMO,按照实际上的情况扫描
//       : [in] long DemoGrade - 模拟级别 
//              0 调用扫描接口就给图像，不需要扫描仪,暂时不支持
//              非零 正常调用扫描仪，接收到扫描图像替换为预先准备的图像
//说明： 只是支持两种格式HTTP以及本地
//    SetDemoImgInfo("c:\\DEMO\\",3,1);
//    SetDemoImgInfo("http://127.0.0.1/DEMO/",3,1);
//    表示c:\\DEMO　或者　http://127.0.0.1/DEMO/　目录下有FIlE0001.jpg,FIlE0002.jpg,FIlE0003.jpg文件。  
CJbfImageMng.prototype.SetDemoImgInfo = function(FilenameTemplate, DemoImgMaxCount, DemoGrade)
{
	this.obj().SetDemoImgInfo(FilenameTemplate, DemoImgMaxCount, DemoGrade);
}


//////////////////////////////////////////////////////////////
//名称   SetJPEGQuality
//功能   设置图像的压缩质量
//参数   Quality 取值 0 - 100
//返回   
CJbfImageMng.prototype.SetJPEGQuality = function(Quality)
{
	this.obj().ImgSetJPEGQuality(Quality);
}



//////////////////////////////////////////////////////////////
//名称   ImgDeskew
//功能   图像自动纠偏，不好使用
//参数   
//返回   
CJbfImageMng.prototype.ImgDeskew = function()
{
	this.obj().ImgDeskew();
}

//////////////////////////////////////////////////////////////
//名称   SetMaxCashCount
//功能   设置最大缓存页面数
//参数   页面数，整数，注意此参数一定要大于 单页显示的最多文件数，如 8×8 maxcount 必须大于64
//返回   
CJbfImageMng.prototype.SetMaxCashCount = function(maxcount)
{
	this.obj().ImgSetMaxCashCount(maxcount);
}

//////////////////////////////////////////////////////////////
//名称   SubmitBatch
//功能   提交批量
//参数   在这里起到的作用仅为，把文件名称整理一下。整理成为顺序的号码。
//返回   
CJbfImageMng.prototype.SubmitBatch = function()
{
	this.obj().SubmitBatch();
}

//////////////////////////////////////////////////////////////
//名称   SetVender
//功能   设置生产厂商
//参数   厂商名称：固定输入 "北京京北方科技股份有限公司"
//返回   
CJbfImageMng.prototype.SetVender = function(vender)
{	
	this.obj().SetVender(vender);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::Upload2Ftp
// 功  能: 把扫描文件传输到FTP服务器上去
// 返  回: long  - 传输成功的文件个数
// 参  数: [] LPCTSTR sServerName -  ftp Server 形如："10.10.208.38" 或者 "www.northking.net"
//									 如果本参数内容为 http:// 开头，函数就会去下载 sServerName,
//									 从文件中读取 sServerName，sUserName，sPassword, nPort, 加密密码
//       : [] LPCTSTR sUserName -    ftp user name 
//       : [] LPCTSTR sPassword -    ftp user password 明码
//       : [] short nPort -          ftp port
//       : [] short bPassive -       ftp passive 
//       : [] LPCTSTR sRDir -    必须以 / 结尾 ,只能创建一级目录  ftpdir/a/b/c/
//								 当前FTP用户的用户主目录下的目录 
//       : [] LPCTSTR sLFileList - 传输的本地列表，名称由"|"隔开   形如："c:\\JBFScan\\0000001.JPG|c:\\JBFScan\\0000003.JPG|c:\\JBFScan\\0000003.JPG"
//									当本参数不为 "" ,传输的所有文件就来源于此列表,为 "" 时，传输目前扫描组件内装载的所有文件,必须是可加载的图像文件。

CJbfImageMng.prototype.Upload2Ftp = function(server,user,pwd,port,passive,rpath,filelist)
{	
	return this.obj().Upload2Ftp(server,user,pwd,port,passive,rpath,filelist);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::MakeFtpConnectInfo
// 功  能: 生成FTP传输参数，保存到文件中
// 返  回: void  - 
// 参  数: [] LPCTSTR sServerName - 服务器地址
//       : [] short nPort - 服务器端口号
//       : [] LPCTSTR sUserName - FTP用户名称
//       : [] LPCTSTR sPassword - FTP用户密码
//       : [] short nPassive -    FTP主动模式或者被动模式
//       : [] LPCTSTR sFilePath -  存储加密的FTP参数的本地文件全路径
//说   明: 生成的文件大小504字节，注意，V1.0.3.0只支持从HTTP下载加密参数，不支持从本地加载参数。

CJbfImageMng.prototype.MakeFtpConnectInfo = function(sServerName,nPort,sUserName,sPassword,nPassive,sFilePath)
{	
	return this.obj().MakeFtpConnectInfo(sServerName,nPort,sUserName,sPassword,nPassive,sFilePath);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::Upload2FtpByHttp
// 功  能: 使用Http代理FTP,完成FTP传输
// 返  回: long  - 成功传输的文件数
// 参  数: [] LPCTSTR sHttpServerName - http Server 形如："10.10.208.38" 或者 "www.northking.net"
//       : [] short nHttpPort -   http端口, 形如： 80 或者 8080
//       : [] LPCTSTR sHttpUrl -  实现FTP代理的HttpUrl, 形如："/icc/servlet/HttpAgentFileStoreServlet"
//       : [] LPCTSTR sFtpRootPath - FTP服务器上的路径,形如："ftp://192.168.1.133////0001/abc/" 或者 "httpagentftpServer////0001/abc/"
//									 注意，ftp://192.168.1.133 只是在服务器上配置的一个FTP的名称。
//									 0001/abc/ 当前FTP用户的用户主目录下的目录
//       : [] LPCTSTR sLocalFilelist - 传输的本地列表，名称由"|"隔开   形如："c:\\JBFScan\\0000001.JPG|c:\\JBFScan\\0000003.JPG|c:\\JBFScan\\0000003.JPG"
//									当本参数不为 "" ,传输的所有文件就来源于此列表,为 "" 时，传输目前扫描组件内装载的所有文件,必须是可加载的图像文件。

CJbfImageMng.prototype.Upload2FtpByHttp = function(server,port,url,ftproot,filelist)
{	
	return this.obj().Upload2FtpByHttp(server,port,url,ftproot,filelist);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::Init
// 功  能: 
// 返  回: short  - 
// 参  数: [] short nFlag - 1 扫描初始化   - 2 图像浏览初始化
// 说  明: 此函数必须在扫描动作或者设置扫描参数之前设置。一般在控件初始化完成时候加载

CJbfImageMng.prototype.InitControlType = function(nFlag)
{	
	return this.obj().Init(nFlag);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::SetShowFrontBackIdentity
// 功  能: 设置，显示正反面以及序号标志
// 返  回: void  - 
// 参  数: [] short nFlag - TRUE 显示 FALSE 不显示
//         默认不显示
CJbfImageMng.prototype.SetShowFrontBackIdentity = function(nFlag)
{	
	this.obj().SetShowFrontBackIdentity(nFlag);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::SetShowFrontBackIdentity
// 功  能: 获得，显示正反面以及序号标志
// 返  回: void  - 
// 参  数: [] short nFlag - TRUE 显示 FALSE 不显示
//         默认不显示
CJbfImageMng.prototype.IsShowFrontBackIdentity = function()
{	
	return this.obj().IsShowFrontBackIdentity();
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::ImgRateAll
// 功  能: 按照指定的角度旋转所有的图像
// 返  回: long  - 
// 参  数: [] float fRate - 旋转角度
//         90  - left
//		   180
//         270 - right          
CJbfImageMng.prototype.RotateAll = function(fRate)
{
	this.obj().ImgRateAll(fRate);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::TwainSetDefaultRate
// 功  能: 按照指定的角度旋转所有的图像
// 返  回: long  - 
// 参  数: [] float fRate - 旋转角度
//         90  - left
//		   180
//         270 - right          
CJbfImageMng.prototype.SetScanDefaultRate = function(fRate)
{
	this.obj().TwainSetDefaultRate(fRate);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::ImgImport
// 功  能: 模拟导入
// 返  回: short  - 
// 参  数: [] LPCTSTR FilePath - 
//       : [] short nFlag - 0 导入目录 1 导入 FilePath的文件 格式形如LoadListImageA       
CJbfImageMng.prototype.ImgImport = function(filePath,nFlag)
{
	return this.obj().ImgImport(filePath,nFlag);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::ChoosePath
// 功  能: 选择路径
// 返  回: BSTR  - 选择的目录
// 参  数: [] LPCTSTR szCaption - 标题 
//       : [] LPCTSTR szTitle - 提示信息
//       : [] LPCTSTR szInitialPath - 初始路径     
CJbfImageMng.prototype.ChoosePath = function(szCaption, szTitle, szInitialPath)
{
	return this.obj().ChoosePath(szCaption, szTitle, szInitialPath);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::ImgSwapByIndex
// 功  能: 交换
// 返  回: short  - 
// 参  数: [] long nIndex1 - 
//       : [] long nIndex2 - 
//       : [] short nFlag - 是否刷新     
CJbfImageMng.prototype.ImgSwapByIndex = function(nIndex1, nIndex2, nFlag)
{
	return this.obj().ImgSwapByIndex(nIndex1, nIndex2, nFlag);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::TwainSwapFrontBack
// 功  能: 交换
// 返  回:
// 参  数: 
//     
CJbfImageMng.prototype.TwainSwapFrontBack = function()
{
	return this.obj().TwainSwapFrontBack();
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJbfImageMng::SetScanFrontAndBackJPEGQuality
// 功  能: 设置正反面的压缩质量
// 返  回: void  - 
// 参  数: [in] short frontQuality - 正面的 10 - 100 
//      : [in] short backQuality -  反面的 10 - 100
CJbfImageMng.prototype.SetScanFrontAndBackJPEGQuality = function(frontQuality, backQuality)
{
	this.obj().SetScanFrontAndBackJPEGQuality(frontQuality, backQuality);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::PutHttpFiles
// 功  能: 传输目录文件到HTTP服务器
// 创  建: 2009-2-22 15:53:44 by bd.zheng@northking.net stdio_01@163.com
// 修  改: 
// 返回值: BSTR   -  "" 空就OK 否则 ErrorNO: XXXX \r\n FailedList: FilePath1 | FilePath2 | \r\n ErrorDescription
// 参  数: [] LPCTSTR host - 服务器地址
//       : [] long port - 服务器端口
//       : [] LPCTSTR url - 请求远程的地址
//       : [] LPCTSTR lpath - 本地路径   形如：d:/abc/*.*  或者  d:/abc/0001.jpg  
//       : [] long nFlag -  确定第三个参数表示的含义
//				1  表示，远程的相对路径 简单的 RemoteDir/abc/bcd
//				2  表示， 请求的URL地址，可以控制路由,以及自定义服务器段实现 
//				如: /httpfiletrans/HttpFileTrans?method=uploads&BufferSize=65536&FlushSize=65536&IsCreateDir=TRUE&FilePath=TEMP1/TestUploadA
// 说  明: 没有进行任何重试操作，如果网络状况不好，需要调用者自己重试
CJbfImageMng.prototype.PutHttpFiles = function(host, port, url, lpath, nFlag) 
{
	return this.obj().PutHttpFiles(host, port, url, lpath, nFlag);
}

//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::GetHttpFile
// 功  能: 获得文件
// 创  建: 2009-2-22 16:29:08 by bd.zheng@northking.net stdio_01@163.com
// 修  改: 
// 返回值: BSTR   -
// 参  数: [] LPCTSTR host -
//       : [] long port -
//       : [] LPCTSTR url -
//       : [] LPCTSTR lfilepath -
//       : [] long nFlag - 
// 说  明: 参数说明参见 PutHttpFiles 
CJbfImageMng.prototype.GetHttpFile = function(host, port, url, lfilepath, nFlag) 
{
	return this.obj().GetHttpFile(host, port, url, lfilepath, nFlag);
}
//////////////////////////////////////////////////////////////////////
// 函数名: CJBFImgMngCtrl::SetCommFlag
// 功  能: 设置控件显示正面背面以及扫描影像是否加密
// 创  建: 2010-11-22 16:29:08 by bd.zheng@northking.net stdio_01@163.com
// 修  改: 
// 返回值: 无   -
// 参  数: [] int displayFlag -
//       : [] int encryptFlag -
// 说  明: displayFlag - 0-显示全部;１-显示正面;　２-显示背面
//        encryptFlag - 0-不加密;１-加密，加密后的影像只能由本控件打开
CJbfImageMng.prototype.SetCommFlag = function(displayFlag, encryptFlag)
{
	//alert("参数为0，显示全部１显示正面　２显示背面");
	this.obj().SetCommFlag(displayFlag, encryptFlag);
}