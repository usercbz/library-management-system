package com.cbz.librarymanagementsystem.service;

import com.cbz.librarymanagementsystem.dto.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.cbz.librarymanagementsystem.utils.SystemConst.IMAGE_FILE_PATH;

@Service
public class UploadService {

    public Result uploadFile(MultipartFile imageFile) {
        if (imageFile == null) {
            return Result.fail("文件上传失败！");
        }
        //获取文件
        String filename = imageFile.getOriginalFilename();
        if (filename == null) {
            return Result.fail("文件不存在！");
        }
        //获取文件后缀
        String fileSuffix = filename.substring(filename.lastIndexOf('.'));
        //创建UUID作为标识
        String uuID = UUID.randomUUID().toString();
        //加上后缀
        String name = uuID + fileSuffix;

        //复制文件到本地
        try {
            imageFile.transferTo(new File(IMAGE_FILE_PATH, name));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败！");
        }
        //返回文件标识 给前端
        return Result.succeed(name);
    }

}
