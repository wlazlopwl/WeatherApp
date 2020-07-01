package com.example.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.HourForecastAdapter
import com.example.weatherapp.R
import com.example.weatherapp.SharedPreferences
import com.example.weatherapp.model.oneapi.Hourly
import com.example.weatherapp.model.oneapi.oneApiResponse
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var hourForecastRV: RecyclerView
    private lateinit var hourForecastAdapter: HourForecastAdapter
    lateinit var hourForecastLayoutManager:RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = SharedPreferences(applicationContext)
        actualWeatherSearchView.isIconifiedByDefault = false

        updateLang()


    }

    override fun onResume() {
        super.onResume()
        hourForecastLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        hourForecastAdapter = HourForecastAdapter()
        hourForecastRV = findViewById<RecyclerView>(R.id.hour_forecast_rv).apply {
            setHasFixedSize(true)
            layoutManager = hourForecastLayoutManager
            adapter = hourForecastAdapter

        }


        if (sharedPreferences.locationExist()) {
            getWeatherForCity(sharedPreferences.getLatestLocation())
            actualWeatherSearchView.queryHint = sharedPreferences.getLatestLocation()
        } else {
            actualWeatherSearchView.queryHint = "Enter city"
        }
        actualWeatherSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getWeatherForCity(query)
                    sharedPreferences.setCurrentLocation(query)
                    actualWeatherSearchView.queryHint = sharedPreferences.getLatestLocation()

                }
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })







    }


    fun getWeatherForCity(query: String) {

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.init(query, sharedPreferences.getUnit(), sharedPreferences.currentLang())
        weatherViewModel.getWeatherRepository()
            ?.observe(this, Observer {
                test.text = it.list[0].main.temp.roundToInt().toString() + getActualUnit()
                actualWeatherDesc.text = it.list[0].weather[0].description
//                weather_time.text=java.time.format.DateTimeFormatter.ISO_INSTANT
//                    .format(java.time.Instant.ofEpochSecond(it.list[0].dt.toLong())).toString()
                weather_time.text = convertUnixTime(it.list[0].dt, 7200)
                val imgType: String = it.list[0].weather[0].icon
                Picasso.with(applicationContext).load("http://openweathermap.org/img/wn/" + imgType + "@2x.png")
                    .into(actualWeatherImageView, object : Callback {
                        override fun onSuccess() {
                            Log.d("t", "test")
                            val lat = it.list[0].coord.lat.toString()
                            val lon = it.list[0].coord.lon.toString()
                            getOneCallWeather(lat,lon)
                        }

                        override fun onError() {
                            Log.d("t", "E R R O R MAIN")

                        }

                    })

            })

    }

    fun getOneCallWeather(lat:String,lon:String) {
        weatherViewModel.initOneCall(lat,lon,sharedPreferences.getUnit(),sharedPreferences.currentLang())
        weatherViewModel.getOneCallRepository()?.observe(this, Observer {
            hourForecastAdapter.setHourForecast(it.hourly)
            hourForecastAdapter.notifyDataSetChanged()

        })

    }

    private fun getActualUnit(): String {
        return when (sharedPreferences.getUnit()) {
            "metric" -> 0x00B0.toChar() + "C"
            "imperial" -> 0x00B0.toChar() + "F"

            else -> "K"
        }
    }

    private fun checkDeviceLang(): String {
        return Locale.getDefault().language

    }

    //compare lang in SharedPreferences with actual Lang
    private fun compareLang(): Boolean {
        val deviceLang = checkDeviceLang()
        val currentLang = sharedPreferences.currentLang()
        return deviceLang == currentLang

    }

    private fun updateLang() {
        compareLang()
        if (!compareLang()) {
            sharedPreferences.setLang(checkDeviceLang())

        }

    }


    fun convertUnixTime(unixDate: Int, timeZone: Int): String {
        val date = Date((unixDate) * 1000L)
        val format: DateFormat = SimpleDateFormat("dd-MM, HH:mm")
        format.timeZone = TimeZone.getTimeZone("Etc/UTC")
        return format.format(date)
    }

}

