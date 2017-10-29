package com.tony.weathertomessage.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tony.weathertomessage.service.AlarmWeatherService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private AlarmWeatherService service;
    @Override
    public void onReceive(Context context, Intent intent) {
        service = new AlarmWeatherService(context);
        if (intent.getAction().equals(ACTION)) {
            service.setAlarmTime();
        }
    }
}
