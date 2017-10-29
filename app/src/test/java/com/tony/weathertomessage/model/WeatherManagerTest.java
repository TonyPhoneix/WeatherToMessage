package com.tony.weathertomessage.model;

import com.tony.weathertomessage.api.domain.WeatherInfo;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tony on 2017/10/16.
 */
public class WeatherManagerTest {

    WeatherManager manager = WeatherManager.getInstance();

    @Test
    public void getInstance() throws Exception {
//        System.out.println(manager);
    }

    @Test
    public void getWeatherData() throws Exception {
        manager.getWeatherData("北京", new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {

            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {

            }
        });

    }

}