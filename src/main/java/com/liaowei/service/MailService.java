package com.liaowei.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.liaowei.util.EmailCode.isEmail;

/**
 * @author Zhan Xinjian
 * @date 2020/2/24 17:16
 * <p></p>
 */
@Service
public class MailService {
    private static  final Logger log = LoggerFactory.getLogger(MailService.class);
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    Environment environment;

    public void sendMail(String email,int code) {
        boolean b = isEmail(email);
        if(!b){
            try {
                throw new Exception("邮件格式错误！");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String text=environment.getProperty("mail.content")+code;
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(environment.getProperty("mail.send.from"));
            simpleMailMessage.setSubject(environment.getProperty("mail.subject"));
            simpleMailMessage.setTo(email);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
            log.info("邮件发送成功！,邮件为："+text);
        } catch (Exception e) {
            log.error("邮件发送异常",e.fillInStackTrace());
        }

    }
    public void sendResetMail(String email,int code) {
        boolean b = isEmail(email);
        if(!b){
            try {
                throw new Exception("邮件格式错误！");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String text=environment.getProperty("mail.reset.content")+code;
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(environment.getProperty("mail.send.from"));
            simpleMailMessage.setSubject(environment.getProperty("mail.reset.subject"));
            simpleMailMessage.setTo(email);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
            log.info("邮件发送成功！,邮件为："+text);
        } catch (Exception e) {
            log.error("邮件发送异常",e.fillInStackTrace());
        }

    }
}
