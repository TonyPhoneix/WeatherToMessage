package com.tony.weathertomessage.api.inter;

import com.tony.weathertomessage.api.domain.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tony on 2017/10/16.
 */

public interface WeatherInter {

    @GET("json.shtml")
    Call<Weather> getWeatherData(@Query("city") String city);
}
