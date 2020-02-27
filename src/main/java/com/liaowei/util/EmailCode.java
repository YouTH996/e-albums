package com.liaowei.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhan Xinjian
 * @date 2020/2/24 17:01
 * <p>
 *     生成6位随机验证码
 * </p>
 */
public class EmailCode {
    public static int  generteCode() {
        int code = new Random().nextInt(1000000);
        return code;
    }

    public static String handleEmail(String email) {
        if(email.startsWith("www.")){
            int i = email.indexOf(".");
            String substring = email.substring(i + 1);
            return substring;
        }
        return email;
    }

    //判断Email合法性
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(rule);
        matcher = pattern.matcher(email);
        if (matcher.matches())
            return true;
        else {
            return false;
        }
    }

}
