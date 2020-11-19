package com.ocr.common.utils.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.ocr.common.config.Global;
import com.ocr.common.utils.DateUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * Created by doumenwangjian on 2020/11/10.
 */
public class VideoUtils {
    /**
     * 获取视频时长，单位为秒
     *
     * @param video 源视频文件
     * @return 时长（s）
     */
    public static long getVideoDuration(File video) {
        long duration = 0L;
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        try {
            ff.start();
            duration = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return duration;
    }


    /*
     * 给文件路径和保存路径自动生成3张图片
     * video ：视频文件
     * picPath : 保存文件路径
     */
    public static void videoExtractPic(File video, String picPath){
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        String name=video.getName().substring(0,video.getName().length()-4);
        try {
            ff.start();
            // 截取中间帧图片(具体依实际情况而定)
            Frame frame = null;
            int length = ff.getLengthInFrames();//获取总帧数
            int stepLength=6;
            int num=length/stepLength; //步长
            int pre=length % stepLength/2;
            //前面的帧数不要
            for(int m=0;m<pre;m++){
                frame = ff.grabFrame();
            }
            int cout=1;
            boolean flag=false;
            for(int i=1;i<=stepLength;i++){
                for(int k=1;k<=num;k++){

                    frame = ff.grabFrame();
                    //偶数区间且有图片的 取出来
                    if(i%2 ==0 && frame.image!=null && flag == false){
                        //1.实际存储的路径
                        StringBuffer sb=new StringBuffer();
                        sb.append(picPath).append("/");
                        String fileMkPath=sb.toString();
                        //创建文件夹
                        File file=new File(fileMkPath);
                        if(!file.exists()) {
                            file.mkdirs();
                        }
                        sb.append(name).append("_").append(cout).append(".jpg");
                        String pathName=sb.toString();

                        //2.服务器存储路径
                        StringBuffer sb2=new StringBuffer();
                        sb2.append(Global.getServerProfile()).append(DateUtils.datePath()).append("/").append(name).append("_").append(cout).append(".jpg");
                        String serverPath= sb2.toString() ;

                        //图片转换
                        Java2DFrameConverter converter = new Java2DFrameConverter();
                        BufferedImage srcImage = converter.getBufferedImage(frame);
//                        String pathName=picPath+"_"+i+k+".jpg";
                        File picFile = new File(pathName);
                        ImageIO.write(srcImage, "jpg", picFile);
                        flag=true;
                        cout++;
                    }
                }
                flag=false;
            }

            ff.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 给视频路径和保存路径自动生成3张图片并把文件名存到数组中
     * video ：视频文件
     * picPath : 保存文件路径
     * list :存储生成图片的路径
     * filename: 文件名
     */

    public static void videoExtractPic(File video, String picPath,ArrayList<String> list){
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
        String name=video.getName().substring(0,video.getName().length()-4);
        try {
            ff.start();
            // 截取中间帧图片(具体依实际情况而定)
            Frame frame = null;
            int length = ff.getLengthInFrames();//获取总帧数
            int stepLength=6;
            int num=length/stepLength; //步长
            int pre=length % stepLength/2;
            //前面的帧数不要
            for(int m=0;m<pre;m++){
                frame = ff.grabFrame();
            }
            int cout=1;
            boolean flag=false;
            for(int i=1;i<=stepLength;i++){
                for(int k=1;k<=num;k++){

                    frame = ff.grabFrame();
                    //偶数区间且有图片的 取出来
                    if(i%2 ==0 && frame.image!=null && flag == false){
                        //1.实际存储的路径
                        StringBuffer sb=new StringBuffer();
                        sb.append(picPath).append("/");
                        String fileMkPath=sb.toString();
                        //创建文件夹
                        File file=new File(fileMkPath);
                        if(!file.exists()) {
                            file.mkdirs();
                        }
                        sb.append(name).append("_").append(cout).append(".jpg");
                        String pathName=sb.toString();

                        //2.服务器存储路径
                        StringBuffer sb2=new StringBuffer();
                        sb2.append(Global.getServerProfile()).append(picPath.substring(picPath.lastIndexOf("IMAGE")+6, picPath.length())).append("/").append(name).append("_").append(cout).append(".jpg");
                        String serverPath= sb2.toString() ;
                        list.add(serverPath);

                        //图片转换
                        Java2DFrameConverter converter = new Java2DFrameConverter();
                        BufferedImage srcImage = converter.getBufferedImage(frame);
//                        String pathName=picPath+"_"+i+k+".jpg";
                        File picFile = new File(pathName);
                        ImageIO.write(srcImage, "jpg", picFile);
                        flag=true;
                        cout++;
                    }
                }
                flag=false;
            }

            ff.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 根据视频名分析视频的日期（注意：必须满足是以年月日开头的：如，20201120_ldjlsldwe,20221028_suiyi）
     * name ：视频文件的名字
     * picPath : 保存文件路径
     * list :存储生成图片的路径
     * filename: 文件名
     */
    public static String CreatePathByVideoName(String name){
        //"20201117_92030839";
        StringBuilder sb=new StringBuilder();
        sb.append(name.substring(0,4)).append("/").append(name.substring(4,6)).append("/").append(name.substring(6,8)).append("/");
        return sb.toString();
    }

}
