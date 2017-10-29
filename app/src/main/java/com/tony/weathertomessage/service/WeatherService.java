package com.tony.weathertomessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tony.weathertomessage.api.domain.WeatherInfo;
import com.tony.weathertomessage.model.SmsSender;
import com.tony.weathertomessage.model.WeatherManager;
import com.tony.weathertomessage.utils.SharedPreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService extends Service {
    private static WeatherManager wm;
    private static SharedPreferenceUtil preferenceUtil;
    private static SmsSender smsSender;

    public WeatherService() {
        wm = WeatherManager.getInstance();
        preferenceUtil = SharedPreferenceUtil.getInstance();
        smsSender = SmsSender.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//      在这里去获取天气，发送短信
        Logger.i("服务开启成功");
        String city = preferenceUtil.getCityName();
        wm.getWeatherData(city, new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                //这里去解析
                Logger.i(response.body().toString());
                sendSms(response.body());
            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Logger.e(t.getMessage());
//              这里需要弹窗，获取天气失败
                Toast.makeText(WeatherService.this, "获取天气失败", Toast.LENGTH_SHORT).show();
            }
        });
        return super.onStartCommand(intent,flags,startId);
    }

    /**
     * 用来发送信息
     *
     * @param weather
     */
    private void sendSms(WeatherInfo weather) {
        String phone = preferenceUtil.getPhoneNumber();
        String content = splicingContent(weather);
        smsSender.sendSMS(phone, content);
//        Toast.makeText(WeatherService.this, "短信发送成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 组装天气信息
     *
     * @param weather
     * @return
     */
    private String splicingContent(WeatherInfo weather) {
        WeatherInfo.WeatherBean weatherBean = weather.getWeather().get(0);
        String city = weatherBean.getCity_name();
        List<WeatherInfo.WeatherBean.FutureBean> future = weatherBean.getFuture();
        String content = "";
        for (WeatherInfo.WeatherBean.FutureBean bean :
                future) {
            String text = bean.getText().split("/")[0];
            String date = bean.getDate().split("-")[2];
//            20号周五多云
            content += date + "号" + text + " ";
        }
        Logger.i(content);
        return city + "：" + content;
    }
}
