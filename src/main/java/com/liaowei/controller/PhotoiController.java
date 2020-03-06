package com.liaowei.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liaowei.model.Effects;
import com.liaowei.model.Photo;
import com.liaowei.model.User;
import com.liaowei.model.UserPhoto;
import com.liaowei.service.EffectsService;
import com.liaowei.service.PhotoService;
import com.liaowei.service.UserPhotoService;
import com.liaowei.service.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    EffectsService effectsService;

    @RequestMapping(value = "uploadPhoto", method = RequestMethod.POST)
    public String uploadPhoto(HttpServletRequest request, Model model, HttpSession session) throws IOException {
        String message = request.getParameter("message");
        Integer type = Integer.parseInt(request.getParameter("type"));
        String bgm = request.getParameter("bgm");
        Integer effect = Integer.parseInt(request.getParameter("effect"));
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("photo");
        MultipartFile file = files.get(0);
        if (!file.isEmpty()) {
            String path = environment.getProperty("aplication.location") + "\\src\\main\\resources\\static\\upload";
            log.info("图片保存的路径为：" + path);
            //获取图片后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = uploadMsg() + "." + suffix;
            File filePath = new File(path, uploadMsg() + "." + suffix);
            //判断路径是否存在，不存在则创建一个
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdir();
            }
            file.transferTo(new File(path + File.separator + fileName));
            //保存进数据库的图片url
            String photoUrl = environment.getProperty("photo.url") + fileName;
            Photo photo = new Photo(fileName, photoUrl, type, message, bgm);
            boolean b = photoService.saveOrUpdate(photo);
            User user = (User) request.getSession().getAttribute("user");
            //得到用户ID和照片ID，存入用户照片关联表中
            Integer id = user.getId();
            QueryWrapper<Photo> queryWrapper = new QueryWrapper<Photo>().eq("photo_name", fileName);
            Photo photo1 = photoService.getOne(queryWrapper);
            Integer id1 = photo1.getId();
            UserPhoto userPhoto = new UserPhoto(id, id1);
            boolean b1 = userPhotoService.save(userPhoto);
            photo1.setPhotoBgm("http://localhost:8086/bgm/" + photo1.getPhotoBgm() + ".mp3");
            Effects effects = new Effects(effect, photo1.getId());
            boolean b2 = effectsService.save(effects);
            if (b && b1 && b2) {
                model.addAttribute("uploadMsg", "文件上传成功");
                ArrayList<Photo> photos = (ArrayList<Photo>) session.getAttribute("photos");
                if (null== photos) {
                    photos = new ArrayList<>();
                }
                photos.add(photo1);
                redisService.setObject(user.getUsername() + "_photos", photos);
                session.setAttribute("photos", photos);
                //更新redis  签到
                switch (type) {
                    case 0:
                        List<Photo> pepList = JSONObject.parseArray(redisService.get(user.getUsername() + "_pepPhotos"), Photo.class);
                        pepList.add(photo1);
                        redisService.setObject(user.getUsername() + "_pepPhotos", pepList);
                        session.setAttribute("pepPhotos", pepList);
                        break;
                    case 1:
                        List<Photo> sceList = JSONObject.parseArray(redisService.get(user.getUsername() + "_scePhotos"), Photo.class);
                        sceList.add(photo1);
                        redisService.setObject(user.getUsername() + "_scePhotos", sceList);
                        session.setAttribute("scePhotos", sceList);
                        break;
                    case 2:
                        List<Photo> buiList = JSONObject.parseArray(redisService.get(user.getUsername() + "_buiPhotos"), Photo.class);
                        buiList.add(photo1);
                        redisService.setObject(user.getUsername() + "_buiPhotos", buiList);
                        session.setAttribute("buiPhotos", buiList);
                        break;
                    case 3:
                        List<Photo> foodList = JSONObject.parseArray(redisService.get(user.getUsername() + "_foodPhotos"), Photo.class);
                        foodList.add(photo1);
                        redisService.setObject(user.getUsername() + "_foodPhotos", foodList);
                        session.setAttribute("foodPhotos", foodList);
                        break;
                }
                return "upload";
            } else {
                model.addAttribute("uploadMsg", "文件上传失败");
                return "upload";
            }
        }
        model.addAttribute("uploadMsg", "文件上传失败");
        return "upload";
    }


    @GetMapping("/effect/{photoId}")
    public String effect(@PathVariable(name = "photoId") Integer photoId, Model model) {
        Photo photo = photoService.getById(photoId);
        QueryWrapper<Effects> wrapper = new QueryWrapper<Effects>().eq("photo_id", photoId);
        Effects effect = effectsService.getOne(wrapper);
        Integer effect1 = effect.getEffect();
        model.addAttribute("photo", photo);
        if (1 == effect1) {
            return "photos-effects1";
        } else if (2 == effect1) {
            return "photos-effects2";
        } else {
            return "photos-effects3";
        }

    }

    @GetMapping("/delPhoto/{photoId}")
    public String delPhoto(@PathVariable(name = "photoId") Integer photoId, Model model,HttpSession session) {
        Photo photo = photoService.getById(photoId);
        ArrayList<Photo> photos = (ArrayList<Photo>) session.getAttribute("photos");
        ArrayList<Photo> photos1 = new ArrayList<>();
        for (Photo photo1 : photos) {
            if (photo1.getId().equals(photo.getId())) {
                continue;
            }
            photos1.add(photo);
        }
        session.setAttribute("photos",photos1);
        if (photos.size() - photos1.size() == 1) {
            model.addAttribute("delMsg", "删除成功！");
            return "/photos-organize";
        } else {
            model.addAttribute("delMsg", "删除失败!");
            return "/photos-organize";
        }
    }



}
