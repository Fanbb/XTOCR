package com.ocr.system.outService;

import com.ocr.common.config.Global;
import com.ocr.common.utils.file.VideoUtils;
import com.ocr.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by doumenwangjian on 2020/11/16.
 */
@Component
public class VideoServer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(VideoServer.class);

    @Value("${ocr.profile}")
    private String imgUploadPath;

    @Autowired
    private ISysConfigService configService;

    @Override
    public void run(String... strings) throws Exception {
        new Thread() {
            public void run() {
                System.out.println("对外视频服务已经启动");
                ThreadPoolExecutor service = new ThreadPoolExecutor(5, 30,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(1024),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
                try {
                    //1.创建服务器serverSocket对象和系统要指定的端口号
                    int port = Integer.parseInt(configService.selectConfigByKey("sys.video.port"));
                    ServerSocket server= new ServerSocket(port);
                    //升级：外部可以一直调用，而不是调用一次之后就结束
                    while(true) {
                        //2.使用Server Socket对象中的方法accept,获取请求的客户端对象Socket
                        Socket socket = server.accept();
                        //升级：使用多线程，提高效率，有一个客户端上传文件，就开启一个线程，完成文件的上传
                        Runnable runnable = () -> {
                            //1.完成文件的上传
                            String localFileName="";
                            try{
                                //3.使用Socket对象中的方法getInputStream()获取网络字节输入流InputStream对象
                                InputStream is= socket.getInputStream();

                                //使用DataInputStream包装输入流，获取文件名
                                DataInputStream dataInputStream = new DataInputStream(is);
                                localFileName = dataInputStream.readUTF();//文件名

                                String timePath=VideoUtils.CreatePathByVideoName(localFileName);//根据文件名生成日期路径

                                //4.判断文件夹是否存在，不存在则创建
                                StringBuilder sb=new StringBuilder();
                                sb.append(Global.getProfile()).append("/IMAGE/").append(timePath);
                                //String profile= Global.getProfile();//平台配置的指定文件路径
                                //String pathName=profile+"/IMAGE/"+timePath;
                                String path=sb.toString();
                                File file =new File(path);
                                if(!file.exists()) {
                                    file.mkdirs();
                                }
                                sb.append(localFileName).append(".mp4");
                                //5.使用Socket对象中的方法getOutputStream()获取网络字节输出流Output Stream对象
                                //String fileName=pathName + localFileName+".mp4";
                                FileOutputStream fos = new FileOutputStream(sb.toString());

                                //6.使用网络字节输出流OutputStream对象中的方法write,给客户端回写数据
                                int length = 0;
                                byte[] buff=new byte[1024*512];
                                while((length=is.read(buff))!=-1) {
                                    //7.使用本地字节输出流FileOutputStream 对象中的方法write,把读取到的文件保存到服务器的
                                    fos.write(buff,0,length);
                                }
                                log.info("视频上传路径：" +sb);

                                //8.使用Socket对象中的方法getOutputStream，获取到网络字节输出流OutputStream对象
                                //9.使用网络字节输出流OutputStream对象中的方法write,给客户端回写“上传成功”
                                socket.getOutputStream().write("{\"msg\":\"上传成功\",\"code\":\"1\"}".getBytes(StandardCharsets.UTF_8));
                                //10.释放资源（FileOutputStream,Socket,ServerSocket）
                                fos.close();
                                socket.close();

                            }catch (IOException e) {
                                System.out.println("对外视频文件上传出错");
                                //9.使用网络字节输出流OutputStream对象中的方法write,给客户端回写“上传成功”
                                try {
                                    socket.getOutputStream().write("{\"msg\":\"上传失败\",\"code\":\"0\"}".getBytes(StandardCharsets.UTF_8));
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                        };

                        service.execute(runnable);
                    }

                    //服务不用关闭了
                    //server.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("服务器关闭");
            };
        }.start();


    }
}
