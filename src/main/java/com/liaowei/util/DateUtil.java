package com.liaowei.util;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zhan Xinjian
 * @date 2020/2/24 19:07
 * <p></p>
 */
public class DateUtil {
    public static String dateMsg() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return df.format(new Date());

    }
}
