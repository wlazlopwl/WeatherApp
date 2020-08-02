package com.example.weatherapp.model.current

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)