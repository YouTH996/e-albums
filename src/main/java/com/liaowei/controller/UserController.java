package com.liaowei.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liaowei.model.Photo;
import com.liaowei.model.User;
import com.liaowei.model.UserPhoto;
import com.liaowei.service.MailService;
import com.liaowei.service.PhotoService;
import com.liaowei.service.UserPhotoService;
import com.liaowei.service.UserService;
import com.liaowei.service.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.liaowei.util.DateUtil.dateMsg;
import static com.liaowei.util.EmailCode.generteCode;
import static com.liaowei.util.EmailCode.handleEmail;

/**
 * @author Zhan Xinjian
 * @date 2020/2/25 21:23
 * <p></p>
 */
@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    RedisServiceImpl redisService;
    @Autowired
    UserPhotoService userPhotoService;
    @Autowired
    PhotoService photoService;

    /**
     * 跳转到登录页
     *
     * @return
     */
    @RequestMapping(value = {"/"})
    public String toLogin() {
        return "login";
    }

    /**
     * 跳转到注册页
     *
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("/toIndex")
    public String toIndex() {
        return "index";
    }

    /**
     * 跳转到重置密码页
     *
     * @return
     */
    @RequestMapping("/toReset")
    public String toReset() {
        return "reset";
    }

    /**
     * 跳转到所有照片页
     *
     * @return
     */
    @RequestMapping("/toAllPhotos")
    public String toWorks(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "all-photos";
    }

    /**
     * 跳转到照片特效页
     *
     * @return
     */
    @RequestMapping("/toPhotosEffects1")
    public String toPhotosEffects(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "photos-effects1";
    }

    /**
     * 跳转到照片特效页4
     *
     * @return
     */
    @RequestMapping("/toPhotosOrganize")
    public String toPhotosEffects4(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "photos-organize";
    }

    /**
     * 跳转到照片特效页2
     *
     * @return
     */
    @RequestMapping("/toPhotosEffects2")
    public String toPhotosEffects2(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "photos-effects2";
    }

    /**
     * 跳转到照片特效页3
     *
     * @return
     */
    @RequestMapping("/toPhotosEffects3")
    public String toPhotosEffects3(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "photos-effects3";
    }

    /**
     * 跳转到upload页
     *
     * @return
     */
    @RequestMapping("/toUpload")
    public String toUpload(HttpSession session) {
        if (null == session.getAttribute("user")) {
            return "login";
        }
        return "upload";
    }

    /**
     * 退出功能
     * @param session
     * @return
     */
    @RequestMapping("/toExit")
    public String toExit(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", name).eq("password", pass);
        User user = userService.getOne(queryWrapper);
        if (null == user) {
            return "登录失败，用户名或密码错误！";
        } else {
            ArrayList<Photo> pepList = new ArrayList<>();
            ArrayList<Photo> sceList = new ArrayList<>();
            ArrayList<Photo> buiList = new ArrayList<>();
            ArrayList<Photo> foodList = new ArrayList<>();
            User user1 = JSONObject.toJavaObject(JSONObject.parseObject(redisService.get("user_" + user.getUsername())), User.class);
            if (null != user1) {
                List<Photo> photos = JSONObject.parseArray(redisService.get(name + "_photos"), Photo.class);
                List<Photo> pepList1 = JSONObject.parseArray(redisService.get(name + "_pepPhotos"), Photo.class);
                List<Photo> sceList1 = JSONObject.parseArray(redisService.get(name + "_scePhotos"), Photo.class);
                List<Photo> buiList1 = JSONObject.parseArray(redisService.get(name + "_buiPhotos"), Photo.class);
                List<Photo> foodList1 = JSONObject.parseArray(redisService.get(name + "_foodPhotos"), Photo.class);
                session.setAttribute("pepPhotos", pepList1);
                session.setAttribute("scePhotos", sceList1);
                session.setAttribute("buiPhotos", buiList1);
                session.setAttribute("foodPhotos", foodList1);
                session.setAttribute("photos", photos);
                request.getSession().setAttribute("user", user1);
                return "index";
            } else {
                //得到用户的ID
                Integer id = user.getId();
                //根据用于ID，得到用户照片关联表中的照片ID，用于获取该用户账户下的照片
                QueryWrapper<UserPhoto> wrapper = new QueryWrapper<UserPhoto>().eq("user_id", id);
                List<UserPhoto> userPhotoList = userPhotoService.list(wrapper);
                ArrayList<Photo> photos = new ArrayList<>();
                for (UserPhoto userPhoto : userPhotoList) {
                    Integer photoId = userPhoto.getPhotoId();
                    Photo photo = photoService.getById(photoId);
                    photos.add(photo);
                    HashMap map = getPhoto(photoId);
                    if (map.containsKey("pepPhoto")) {
                        Photo pepPhoto = (Photo) map.get("pepPhoto");
                        pepList.add(pepPhoto);
                    } else if (map.containsKey("scePhoto")) {
                        Photo scePhoto = (Photo) map.get("scePhoto");
                        sceList.add(scePhoto);
                    } else if (map.containsKey("buiPhoto")) {
                        Photo buiPhoto = (Photo) map.get("buiPhoto");
                        buiList.add(buiPhoto);
                    } else if (map.containsKey("foodPhoto")) {
                        Photo foodPhoto = (Photo) map.get("foodPhoto");
                        foodList.add(foodPhoto);
                    }
                }
                session.setAttribute("photos", photos);
                JudgeListSize(request, session, user, pepList, sceList, buiList, foodList);
                //将user存入redis
                redisService.setObject("user_" + name, user);
                redisService.setObject(user.getUsername() + "_pepPhotos", pepList);
                redisService.setObject(user.getUsername() + "_scePhotos", sceList);
                redisService.setObject(user.getUsername() + "_buiPhotos", buiList);
                redisService.setObject(user.getUsername() + "_foodPhotos", foodList);
                return "index";
            }

        }
    }

    private void JudgeListSize(HttpServletRequest request, HttpSession session, User user, ArrayList<Photo> pepList, ArrayList<Photo> sceList, ArrayList<Photo> buiList, ArrayList<Photo> foodList) {
        if (pepList.size() != 0) {
            session.setAttribute("pepPhotos", pepList);
        }
        if (sceList.size() != 0) {
            session.setAttribute("scePhotos", sceList);
        }
        if (buiList.size() != 0) {
            session.setAttribute("buiPhotos", buiList);
        }
        if (foodList.size() != 0) {
            session.setAttribute("foodPhotos", foodList);
        }
        request.getSession().setAttribute("user", user);
    }

    @RequestMapping("/sendCode")
    @ResponseBody
    public String sendCode(HttpServletRequest request) {
        String email1 = request.getParameter("email");
        String username = request.getParameter("username");
        String email = handleEmail(email1);
        if (redisService.hasKey("register" + "email" + "_" + email)) {
            return "请在1分钟之后重试！";
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("email", email);
        int i = userService.count(queryWrapper);
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>().eq("username", username);
        int j = userService.count(queryWrapper1);
        if (0 != j) {
            return "已存在的用户名";
        } else {
            if (0 != i) {
                return "该邮箱已被注册";
            } else {
                int code = generteCode();
                mailService.sendMail(email, code);
                //获取当前时间串作为redis的key,验证码作为value
                String date = dateMsg();
                //防止一分钟之内重复注册
                redisService.setex("register" + "email" + "_" + email, email, 60);
                //十分钟没有注册，key失效
                redisService.setex("register" + email + "_" + date, code, 60 * 10);
                log.info("redis设置成功");

            }
        }
        return "";
    }

    @RequestMapping("/sendResetCode")
    @ResponseBody
    public String sendResetCode(HttpServletRequest request) {
        String email1 = request.getParameter("email");
        String email = handleEmail(email1);
        if (redisService.hasKey("reset" + "email" + "_" + email)) {
            return "请在1分钟之后重试！";
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("email", email);
        int i = userService.count(queryWrapper);

        if (0 == i) {
            //防止一分钟之内重复提交
            redisService.setex("reset" + "email" + "_" + email, email, 60);
            return "不存在该邮箱";
        } else {
            int code = generteCode();
            mailService.sendResetMail(email, code);
            //获取当前时间串作为redis的key,验证码作为value
            String date = dateMsg();
            //十分钟没有重置密码，key失效
            redisService.setex("reset" + email + "_" + date, code, 60 * 10);
            log.info("redis设置成功");

        }
        return "";
    }

    /**
     * 注册功能
     *
     * @param request
     * @return
     */
    @RequestMapping("/registeUser")
    @ResponseBody
    public String registeUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd2");
        String gender = request.getParameter("gender");
        int sex = ("male".equals(gender)) ? 0 : 1;
        String email1 = request.getParameter("email");
        String email = handleEmail(email1);
        String verifyCode = request.getParameter("verifyCode");
        User user = new User(username, pwd, sex, email);
        String code = redisService.get("register" + email + "_" + dateMsg());
        if (!verifyCode.equals(code)) {
            return "验证码错误";
        } else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                    .eq("username", username)
                    .eq("password", pwd).eq("sex", sex).eq("email", email);
            boolean b = userService.saveOrUpdate(user, queryWrapper);
            if (!b) {
                return "对不起，注册失败！";
            }
        }
        return "恭喜你，注册成功！";
    }

    @ResponseBody
    @RequestMapping("confirmResetCode")
    public String confirmResetCode(HttpServletRequest request) {
        String email1 = request.getParameter("email");
        String email = handleEmail(email1);
        String verifyCode = request.getParameter("verifyCode");
        String code = redisService.get("reset" + email + "_" + dateMsg());
        if (!verifyCode.equals(code)) {
            return "验证码错误";
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("resetUser")
    public String resetUser(HttpServletRequest request) {
        String email1 = request.getParameter("email");
        String pwd = request.getParameter("pwd2");
        String email = handleEmail(email1);
        User user = new User(pwd);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("email", email);
        boolean b = userService.update(user, queryWrapper);
        if (!b) {
            return "修改密码失败";
        }
        return "";
    }

    public HashMap getPhoto(int id) {
        HashMap<String, Photo> map = new HashMap<String, Photo>(128);
        Photo photo = photoService.getById(id);
        Integer photoType = photo.getPhotoType();
        switch (photoType) {
            //人物照片
            case 0:
                map.put("pepPhoto", photo);
                break;
            //风景照片
            case 1:
                map.put("scePhoto", photo);
                break;
            //建筑照片
            case 2:
                map.put("buiPhoto", photo);
                break;
            //美食照片
            case 3:
                map.put("foodPhoto", photo);
                break;

        }

        return map;
    }

}