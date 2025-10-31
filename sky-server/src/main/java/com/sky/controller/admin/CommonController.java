package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
         log.info("文件上传：{}",file);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = uuid + extension;
        String filePath="D:\\mypic\\"+newFileName;

        try {
            file.transferTo(new File(filePath));
            return Result.success("api/pic/"+newFileName);
        } catch (IOException e) {
            log.info("上传失败：{}",e.getMessage());
            return Result.error("UPLOAD_FAILED");
        }

    }

}
