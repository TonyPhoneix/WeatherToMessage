package com.tony.weathertomessage.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.widget.Toast;

import com.tony.weathertomessage.utils.SharedPreferenceUtil;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Tony on 2017/10/16.
 */

public class AlarmWeatherService {

    private final Context context;
    private SharedPreferenceUtil preferenceUtil;
    public AlarmWeatherService(Context context) {
        this.context = context;
        preferenceUtil = SharedPreferenceUtil.getInstance();
    }

    public void setAlarmTime() {
        int timeInterval = preferenceUtil.getTimeInterval();
        int hour = preferenceUtil.getHour();
        int minute = preferenceUtil.getMinute();

        //        然后再设置时间，去唤醒Service
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(systemTime);
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 进行闹铃注册
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent clock = new Intent(context, WeatherService.class);
        PendingIntent sender = PendingIntent.getService(context, 1, clock, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                selectTime, timeInterval * 60 * 60 * 1000, sender);
        Toast.makeText(context, "服务开启成功! ", Toast.LENGTH_SHORT).show();
    }
}
