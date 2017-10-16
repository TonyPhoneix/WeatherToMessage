package com.tony.weathertomessage.utils;


/**
 * Created by Tony on 2017/10/16.
 */

public class StringUtils {
    public static boolean isEmpty(String text) {
        if (text == null || "".equals(text)) {
            return true;
        }
        return false;
    }
}
