package com.ocr.common.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片转码工具类
 */
public class ImageBase64 {
    static BASE64Encoder encoder = new BASE64Encoder();
    static BASE64Decoder decoder = new BASE64Decoder();

    private static final Logger log = LoggerFactory.getLogger(ImageBase64.class);

    /**
     * 将base64转为图片上传到服务器 并返回文件路径
     * @param base64String
     */
    public static Boolean base64StringToImageSave(String base64String,String filePath) {
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes1);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            String substring = filePath.substring(0, filePath.length() - 17);
            File dir = new File(substring);
            log.info("上传路径:" + substring);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(filePath);
            ImageIO.write(bufferedImage, "jpg", file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过图片url获取图片Base64String
     * @param imgURL
     * @return
     */
    public static String imageToBase64ByUrl(String imgURL) {
        ByteArrayOutputStream data = imgUrl(imgURL);
        // 对字节数组Base64编码
        return encoder.encode(data.toByteArray());
    }

    public static ByteArrayOutputStream imgUrl(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
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

    /**
     * 把一个文件转化为字节
     * @param file
     * @return   byte[]
     * @throws Exception
     */
    public static byte[] getByte(File file) throws Exception
    {
        byte[] bytes = null;
        if(file!=null)
        {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if(length>Integer.MAX_VALUE)   //当文件的长度超过了int的最大值
            {
                log.error("this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while(offset<bytes.length&&(numRead=is.read(bytes,offset,bytes.length-offset))>=0)
            {
                offset+=numRead;
            }
            //如果得到的字节长度和file实际的长度不一致就可能出错了
            if(offset<bytes.length)
            {
                log.error("file length is error");
                return null;
            }
            is.close();
        }
        return bytes;
    }


}