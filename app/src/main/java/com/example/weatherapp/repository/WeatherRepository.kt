package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.api.WeatherDataEndpoints
import com.example.weatherapp.api.WeatherDataService
import com.example.weatherapp.model.current.WeatherResponse
import com.example.weatherapp.model.oneapi.oneApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {
    companion object {
        private var weatherRepository: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (weatherRepository == null) {
                weatherRepository = WeatherRepository()
            }
            return weatherRepository as WeatherRepository
        }
    }


    constructor()


    fun getWeatherData(location: String, units: String, lang: String, appid: String): MutableLiveData<WeatherResponse> {
        val weatherData: MutableLiveData<WeatherResponse> = MutableLiveData()
        WeatherDataService.buildService(WeatherDataEndpoints::class.java)
            .getCurrentDataWeather(location, units, lang, appid)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d("test", t.toString())

                }

                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful) {


                            weatherData.value = response.body()



                    }
                }

            })
        return weatherData

    }

    fun getOneCallWeather(
        latitude: String,
        longitude: String,
        units: String,
        lang: String,
        appid: String
    ): MutableLiveData<oneApiResponse> {
        val oneApiData: MutableLiveData<oneApiResponse> = MutableLiveData()

        WeatherDataService.buildService(WeatherDataEndpoints::class.java)
            .getOneCallWeather(latitude, longitude, units, lang, appid)
            .enqueue(object : Callback<oneApiResponse> {
                override fun onFailure(call: Call<oneApiResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<oneApiResponse>, response: Response<oneApiResponse>) {
                    oneApiData.value=response.body()
                }

            })

        return oneApiData

    }

}


