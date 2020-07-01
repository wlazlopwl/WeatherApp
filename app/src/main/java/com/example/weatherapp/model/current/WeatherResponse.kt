package com.example.weatherapp.model.current


data class WeatherResponse(
    val cod: String,
    val count: Int,
    val list: List<SimpleWeather>,
    val message: String
)