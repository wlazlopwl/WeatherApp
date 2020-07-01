package com.example.weatherapp.model.oneapi

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val rain: Rain,
    val temp: Double,
    val weather: List<WeatherXX>,
    val wind_deg: Int,
    val wind_speed: Double
)