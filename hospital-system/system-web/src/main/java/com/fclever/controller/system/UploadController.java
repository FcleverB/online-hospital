package com.fclever.controller.system;

import com.fclever.config.upload.UploadService;
import com.fclever.controller.BaseController;
import com.fclever.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 文件上传控制器
 * @Author 尚学堂 雷哥
 */
@RestController
@RequestMapping("system/upload")
public class UploadController extends BaseController {

    @Autowired
    private UploadService uploadService;

    /**
     * 文件图片 ---检查结果
     */
    @PostMapping("doUploadImage")
    public AjaxResult uploadFile(MultipartFile mf){
        Map<String,Object> map=new HashMap<>();
        if(null!=mf){
            String path = this.uploadService.uploadImage(mf);
            map.put("name",mf.getOriginalFilename());
            map.put("url",path);
            // 返回文件名+访问路径URL
            return  AjaxResult.success(map);
        }else{
            return AjaxResult.error("上传文件失败");
        }
    }
}
