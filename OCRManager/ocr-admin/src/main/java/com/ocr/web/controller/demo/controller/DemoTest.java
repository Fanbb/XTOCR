package com.ocr.web.controller.demo.controller;

import com.ocr.common.core.domain.AjaxResult;
import com.ocr.common.utils.file.FileUploadUtils;
//import com.ocr.common.utils.file.VideoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

@Controller
public class DemoTest {
    @GetMapping(value = "/upload")
    public String autocomplete() {
        System.out.println("test");
        return "/demo/form/upload";
    }

    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) throws IOException {
        //返回给前台
        AjaxResult results= AjaxResult.success("success");
        System.out.println(request.getParameter("username"));
        System.out.println(request.getParameter("password"));

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String fileName = null;
                fileName = FileUploadUtils.upload_2(files[i]);//FileUploadUtils.upload_OCR(files[i]);
                    //e.printStackTrace();
                System.out.println("++++++++++++++++++++++++++++"+fileName);
            }
        }

        return results;
    }

    @GetMapping(value = "/uploadFileVideo")
    @ResponseBody
    public AjaxResult uploadFileVideo(HttpServletRequest request) throws Exception {
        AjaxResult results= AjaxResult.success("success");
        File file=new File("D:\\通用文字测试图片\\测试视频\\范彬彬.mp4");
        String imgName="D:\\通用文字测试图片\\测试视频\\";
        //VideoUtils.fetchPics(file,"C:\\Users\\doume\\Desktop\\图片\\test.jpg");

        //Long time=VideoUtils.getVideoDuration(file);

        //VideoUtils.videoExtractPic(file,imgName);
        // System.out.println(time);
        return results;
    }

    @GetMapping(value = "/clientTest")
    @ResponseBody
    public AjaxResult clientTest(HttpServletRequest request) throws Exception {
        AjaxResult results= AjaxResult.success("success");

        String IP="192.168.117.108";
        int PORT=2323;
        System.out.println("我要把图片上传到服务器："+IP);
        try {
            //建立连接
            Socket socket=new Socket(IP,PORT);
            //读取图像文件上传
            FileInputStream fis=new FileInputStream("D:\\ocr\\uploadPath\\IMAGE\\2020\\11\\16\\20201117_2323.mp4");
            OutputStream os=socket.getOutputStream();

            String fileName="20201117_8888";
            //重点：start
            //向服务器端传文件名
            DataOutputStream dataOutputStream = new DataOutputStream(os);
            dataOutputStream.writeUTF(fileName);
            dataOutputStream.flush();//刷新流，传输到服务端
            //重点：end

            byte[] buff=new byte[1024*1024];
            int length=0;

            while((length=fis.read(buff))!=-1) {
                os.write(buff,0,length);
            }
            socket.shutdownOutput();

            //读取响应的数据
            InputStream is = socket.getInputStream();
            byte[] readBuff=new byte[1024];
            length=is.read(readBuff);
            String message=new String(readBuff,0,length);

            System.out.println("服务器的响应结束："+message);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    @GetMapping(value = "/ByteArrayOutputStream")
    @ResponseBody
    public static ByteArrayOutputStream imgUrl() {
        String imgURL="http://192.168.110.230:8080/OCR/profile/IMAGE/2020/09/10/范彬彬.mp4";
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(50000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
