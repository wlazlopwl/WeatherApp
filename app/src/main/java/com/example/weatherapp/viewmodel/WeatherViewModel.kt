package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.current.WeatherResponse
import com.example.weatherapp.model.oneapi.oneApiResponse
import com.example.weatherapp.repository.WeatherRepository
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


const val API_KEY = "141d23084477611a21ef17a241c9c50f"

class WeatherViewModel : ViewModel() {
    private var mutableLiveData: MutableLiveData<WeatherResponse>? = null
    var oneApiMutableLiveData: MutableLiveData<oneApiResponse>? = null
    var weatherRepository: WeatherRepository? = null


    fun init(query: String, unit: String, lang: String) {

        var query = query
        var unit = unit
        var lang = lang
        weatherRepository = WeatherRepository.getInstance()

        mutableLiveData = weatherRepository?.getWeatherData(query, unit, lang, API_KEY)
    }

    fun initOneCall(latitude: String, longitude: String,unit:String, lang: String) {
        var latitude = latitude
        var longitude = longitude
        var unit = unit
        var lang=lang
        weatherRepository=WeatherRepository.getInstance()
        oneApiMutableLiveData = weatherRepository?.getOneCallWeather(latitude,longitude,unit,lang, API_KEY)

    }


    fun getWeatherRepository(): MutableLiveData<WeatherResponse>? {
        return mutableLiveData
    }

    fun getOneCallRepository(): MutableLiveData<oneApiResponse>? {

        return oneApiMutableLiveData

    }




}