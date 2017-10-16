package com.tony.weathertomessage.model;

import com.tony.weathertomessage.api.domain.Weather;

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
        manager.getWeatherData("北京", new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather beijing = response.body();
                System.out.println(beijing);
                Weather.DataBean data = beijing.getData();
                System.out.println(data.getAqi());
                System.out.println(data.getCity());
                System.out.println(data.getForecast());
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });

    }

}