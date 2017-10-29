package com.tony.weathertomessage.api.inter;

import com.tony.weathertomessage.api.domain.WeatherInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tony on 2017/10/16.
 */

public interface WeatherInter {

    @GET("now")
    Call<WeatherInfo> getWeatherData(@Query("cityid") String city);
}
