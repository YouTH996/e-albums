package com.liaowei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liaowei.model.User;
import com.liaowei.service.MailService;
import com.liaowei.service.UserService;
import com.liaowei.service.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 跳转到登录页
     *
     * @return
     */
    @RequestMapping(value = {"/","login"})
    public String toLogin() {
        return "login";
    }

    /**
     * 跳转到注册页
     *
     * @return
     */
    @RequestMapping("toRegister")
    public String toRegister() {
        return "register";
    }

    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping("toIndex")
    public String toIndex() {
        return "index";
    }

    /**
     * 跳转到重置密码页
     *
     * @return
     */
    @RequestMapping("toReset")
    public String toReset() {
        return "reset";
    }

    /**
     * 跳转到works页
     *
     * @return
     */
    @RequestMapping("toWorks")
    public String toWorks() {
        return "works";
    }

    /**
     * 跳转到gallery页
     *
     * @return
     */
    @RequestMapping("toGallery")
    public String toGallery() {
        return "gallery";
    }

    /**
     * 跳转到blog页
     *
     * @return
     */
    @RequestMapping("toBlog")
    public String toBlog() {
        return "blog";
    }

    /**
     * 跳转到upload页
     *
     * @return
     */
    @RequestMapping("toUpload")
    public String toUpload() {
        return "upload";
    }

    @RequestMapping("loginMethod")
    public String loginMethod(HttpServletRequest request, ModelMap map) {
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", name).eq("password", pass);
        User user = userService.getOne(queryWrapper);
        if (null == user) {
            map.addAttribute("msg", "登录失败，用户名或密码错误！");
            return "login";
        } else {
            return "index";
        }
    }

    @RequestMapping("sendCode")
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

    @RequestMapping("sendResetCode")
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
//            select("username", "password", "sex", "email")
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
        boolean b = userService.update(user,queryWrapper);
        if(!b){
            return "修改密码失败";
        }
        return "";
    }

}