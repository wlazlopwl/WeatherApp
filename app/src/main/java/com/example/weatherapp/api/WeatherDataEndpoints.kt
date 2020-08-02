package com.example.weatherapp.api

import com.example.weatherapp.model.current.WeatherResponse
import com.example.weatherapp.model.oneapi.oneApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query





interface WeatherDataEndpoints {
    @GET("data/2.5/weather?")
    fun getCurrentDataWeather(
        @Query("q") location: String,
        @Query("units") units:String,
        @Query("lang")lang:String,
        @Query("appid") appid:String
    ): Call<WeatherResponse>

    @GET("data/2.5/onecall?")
    fun getOneCallWeather(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("units") units:String,
        @Query("lang")lang:String,
        @Query("appid") appid:String
    )
    : Call <oneApiResponse>




  }