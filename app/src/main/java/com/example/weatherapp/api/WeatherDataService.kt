package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherDataService {

            private val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            fun <T> buildService(service: Class<T>): T {
                return retrofit.create(service)

            }



    }
