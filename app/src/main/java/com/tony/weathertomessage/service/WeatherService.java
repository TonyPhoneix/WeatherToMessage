package com.tony.weathertomessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tony.weathertomessage.api.domain.Weather;
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
        wm.getWeatherData(city, new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                //这里去解析
                Logger.i(response.body().toString());
                sendSms(response.body());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Logger.e(t.getMessage());
//              这里需要弹窗，获取天气失败
                Toast.makeText(WeatherService.this, "获取天气失败", Toast.LENGTH_SHORT).show();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 用来发送信息
     *
     * @param weather
     */
    private void sendSms(Weather weather) {
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
    private String splicingContent(Weather weather) {
        String city = weather.getCity();
        Weather.DataBean data = weather.getData();
        List<Weather.DataBean.ForecastBean> forecast = data.getForecast();
        String head = String.format("尊敬的袁先生早上好，接下来由您帅气的儿子为您播报%s今日天气和未来4天的天气：", city);
        String content = "";
        for (Weather.DataBean.ForecastBean bean :
                forecast) {
            String date = bean.getDate();
            String high = bean.getHigh();
            String low = bean.getLow();
            String type = bean.getType();
            content += String.format("%s，%s，%s，%s。 ", date, type, high, low);
        }
        String front = "天气播报结束，爸爸妈妈要注意身体哦，爱你们的儿子~";
        return head + content + front;
    }
}
