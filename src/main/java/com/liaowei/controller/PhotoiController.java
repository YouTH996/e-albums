package com.liaowei.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liaowei.model.Photo;
import com.liaowei.model.User;
import com.liaowei.model.UserPhoto;
import com.liaowei.service.PhotoService;
import com.liaowei.service.UserPhotoService;
import com.liaowei.service.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.liaowei.util.DateUtil.uploadMsg;

/**
 * @author Zhan Xinjian
 * @date 2020/2/27 16:24
 * <p></p>
 */
@Controller
public class PhotoiController {
    private static final Logger log = LoggerFactory.getLogger(PhotoiController.class);
    @Autowired
    PhotoService photoService;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    UserPhotoService userPhotoService;
    @Autowired
    Environment environment;

    @RequestMapping(value = "uploadPhoto",method = RequestMethod.POST)
    public String uploadPhoto(HttpServletRequest request,Model model) throws IOException {
        String message = request.getParameter("message");
        Integer type = Integer.parseInt(request.getParameter("type"));
        String bgm = request.getParameter("bgm");
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("photo");
        MultipartFile file = files.get(0);
        if (!file.isEmpty()) {
            String path =environment.getProperty("aplication.location")+ "\\src\\main\\resources\\static\\upload";
            log.info("图片保存的路径为："+path);
            //获取图片后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = uploadMsg() + "." + suffix;
            File filePath = new File(path, uploadMsg() + "." + suffix);
            //判断路径是否存在，不存在则创建一个
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdir();
            }
            file.transferTo(new File(path+File.separator+fileName));
            //保存进数据库的图片url
            String photoUrl = "http://localhost:8086/upload/" + fileName;
            Photo photo = new Photo(fileName, photoUrl, type, message,bgm);
            boolean b = photoService.saveOrUpdate(photo);
            User user =(User) request.getSession().getAttribute("user");
            //得到用户ID和照片ID，存入用户照片关联表中
            Integer id = user.getId();
            QueryWrapper<Photo> queryWrapper = new QueryWrapper<Photo>().eq("photo_name", fileName);
            Photo photo1 = photoService.getOne(queryWrapper);
            Integer id1 = photo1.getId();
            UserPhoto userPhoto = new UserPhoto(id, id1);
            boolean b1 = userPhotoService.save(userPhoto);
            photo1.setPhotoBgm("http://localhost:8086/bgm/"+photo1.getPhotoBgm()+".mp3");
            if (b&&b1) {
                model.addAttribute("uploadMsg", "文件上传成功");
                //更新redis  签到
                switch (type) {
                    case 0:
                        List<Photo> pepList = JSONObject.parseArray(redisService.get(user.getUsername() + "_pepPhotos"), Photo.class);
                        pepList.add(photo1);
                        redisService.setObject(user.getUsername() + "_pepPhotos",pepList);
                        break;
                    case 1:
                        List<Photo> sceList = JSONObject.parseArray(redisService.get(user.getUsername() + "_scePhotos"), Photo.class);
                        sceList.add(photo1);
                        redisService.setObject(user.getUsername() + "_scePhotos",sceList);
                        break;
                    case 2:
                        List<Photo> buiList = JSONObject.parseArray(redisService.get(user.getUsername() + "_buiPhotos"), Photo.class);
                        buiList.add(photo1);
                        redisService.setObject(user.getUsername() + "_buiPhotos",buiList);
                        break;
                    case 3:
                        List<Photo> foodList = JSONObject.parseArray(redisService.get(user.getUsername() + "_foodPhotos"), Photo.class);
                        foodList.add(photo1);
                        redisService.setObject(user.getUsername() + "_foodPhotos",foodList);
                        break;
                }
                return "upload";
            }else{
                model.addAttribute("uploadMsg", "文件上传失败");
                return "upload";
            }
        }
        model.addAttribute("uploadMsg", "文件上传失败");
        return "upload";
    }

}
