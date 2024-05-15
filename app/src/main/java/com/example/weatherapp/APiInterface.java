package com.example.weatherapp;

import com.example.weatherapp.model.WeatherItemList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APiInterface {
    @GET("weather")
    Call<WeatherItemList>getWeatherData(@Query("q") String city,
                                        @Query("appId") String appid,
                                        @Query("units") String units
    );
}
