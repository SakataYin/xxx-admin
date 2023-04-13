package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/admin/upload")
@Slf4j
public class UploadController {
    @Resource
    private OSSUtils OSSUtils;

    /**
     * 图片上传
     * @param uploadImage   图片
     * @return              图片地址
     */
    @PostMapping("/image")
    @AuthAccess
    public Result uploadImage(@RequestParam("image") MultipartFile uploadImage) throws IOException {
        String imagePath = OSSUtils.upload(uploadImage.getOriginalFilename(), uploadImage.getInputStream());
        log.debug(imagePath);
        return Result.success(imagePath);
    }

}
