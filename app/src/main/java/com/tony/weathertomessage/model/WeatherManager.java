package com.tony.weathertomessage.model;

import com.tony.weathertomessage.api.domain.Weather;
import com.tony.weathertomessage.api.inter.WeatherInter;
import com.tony.weathertomessage.utils.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tony on 2017/10/16.
 */

public class WeatherManager {
    private Retrofit retrofit;
    private static WeatherManager manager;

    private WeatherManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.sojson.com/open/api/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 单例，只可以获取一个实例
     * @return
     */
    public static WeatherManager getInstance() {
        if (manager == null) {
            synchronized (WeatherManager.class) {
                if (manager == null) {
                    return new WeatherManager();
                }
            }
        }
        return manager;
    }

    /**
     * 获取天气数据
     * @param city
     * @param callback
     */
    public  void getWeatherData(String city, Callback<Weather> callback) {
        if (StringUtils.isEmpty(city)) {
            return;
        }
        WeatherInter weatherInter = retrofit.create(WeatherInter.class);
        Call<Weather> weatherData = weatherInter.getWeatherData(city);
        weatherData.enqueue(callback);
    }
}
