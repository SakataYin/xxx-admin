package com.example.springboot.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OSSUtils {
    private static String ENDPOINT = "oss-cn-beijing.aliyuncs.com";//地域节点
    private static String ACCESSKeyID = "LTAI5tMCWpi8E7rR8NhpXmMi";//访问ID
    private static String ACCESSKEYSECRET = "zK4n4KbXvWahQ5CRpgoLyOjL3iuPgQ";//访问秘钥
    private static String BUCKETNAME = "img-lzm123";//仓库名称
    private static String SUFFER_URL = "http://img-lzm123.oss-cn-beijing.aliyuncs.com";//外网域名
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//日期格式化


    public String upload(String fileName, InputStream inputStream) {

        //创建OSS
        OSS ossClient = new OSSClientBuilder()
                .build(ENDPOINT, ACCESSKeyID, ACCESSKEYSECRET);

        //上传文件流
        String objectName = "image/" + sdf.format(new Date())
                + "/" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));

        //meta请求头，解决图片访问地址直接下载
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
        meta.setContentDisposition("inline");

        ossClient.putObject(BUCKETNAME, objectName, inputStream, meta);

        ossClient.shutdown();

        return SUFFER_URL + "/" + objectName;

    }

    private String getContentType(String FilenameExtension) {
        if (FilenameExtension.equals(".jpg")) {
            return "image/jpg";
        }
        if (FilenameExtension.equals(".png")) {
            return "image/png";
        }
        return FilenameExtension;
    }


}
