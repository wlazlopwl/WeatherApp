package com.example.weatherapp.api

import com.example.weatherapp.model.current.WeatherResponse
import com.example.weatherapp.model.oneapi.oneApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//http://api.openweathermap.org/data/2.5/find?q=tryncza&units=metric&appid=141d23084477611a21ef17a241c9c50f
//https://api.openweathermap.org/data/2.5/onecall?lat=50.13&lon=22.58&units=metric&lang=pl&appid=141d23084477611a21ef17a241c9c50f


interface WeatherDataEndpoints {
    @GET("data/2.5/find?")
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