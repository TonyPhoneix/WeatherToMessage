package com.tony.weathertomessage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tony.weathertomessage.base.BaseApplication;

/**
 * Created by hugo on 2016/2/19 0019.
 *
 * 设置相关 包括 sp 的写入
 */
public class SharedPreferenceUtil {

    private static final String CITY_NAME = "城市";//选择城市
    public static final String ALARM_HOUR  = "hour";
    public static final String ALARM_MINUTE= "minute";
    public static final String PHONE_NUMBER= "phone";
    public static final String SEND_INTERVAL= "interval";


    private SharedPreferences mPrefs;

    public static SharedPreferenceUtil getInstance() {
        return SPHolder.sInstance;
    }

    private static class SPHolder {
        private static final SharedPreferenceUtil sInstance = new SharedPreferenceUtil();
    }

    private SharedPreferenceUtil() {
        mPrefs = BaseApplication.getmAppContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public SharedPreferenceUtil putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SharedPreferenceUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public SharedPreferenceUtil putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    //时间
    public void setHour(int hour) {
        mPrefs.edit().putInt(ALARM_HOUR, hour).apply();
    }

    public int getHour() {
        return mPrefs.getInt(ALARM_HOUR, 7);
    }

    //当前城市
    public void setMinute(int minute) {
        mPrefs.edit().putInt(ALARM_MINUTE, minute).apply();
    }

    public int getMinute() {
        return mPrefs.getInt(ALARM_MINUTE, 0);
    }

    public void setCityName(String cityName) {
        mPrefs.edit().putString(CITY_NAME, cityName).apply();
    }

//    青浦
    public String getCityName() {
        return mPrefs.getString(CITY_NAME, "WTW1HNSVUSCG");
    }

    public void setPhoneNumber(String phoneNumber) {
        mPrefs.edit().putString(PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return mPrefs.getString(PHONE_NUMBER, "13093687239");
    }

    public void setTimeInterval(int interval) {
        mPrefs.edit().putInt(SEND_INTERVAL, interval).apply();
    }

    public int getTimeInterval() {
        return mPrefs.getInt(SEND_INTERVAL, 24);
    }
}
