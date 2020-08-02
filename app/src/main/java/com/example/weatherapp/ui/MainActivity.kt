package com.example.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*
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
    lateinit var dailyForecastRV: RecyclerView
    private lateinit var hourForecastAdapter: HourForecastAdapter
    private lateinit var dailyForecastAdapter: DailyForecastAdapter
    lateinit var hourForecastLayoutManager: RecyclerView.LayoutManager
    lateinit var dailyForecastLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = SharedPreferences(applicationContext)
        actualWeatherSearchView.isIconifiedByDefault = false

        updateLang()


    }

    override fun onResume() {
        super.onResume()
        hourForecastLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dailyForecastLayoutManager = LinearLayoutManager(this)
        hourForecastAdapter = HourForecastAdapter()
        dailyForecastAdapter= DailyForecastAdapter()
        hourForecastRV = findViewById<RecyclerView>(R.id.hour_forecast_rv).apply {
            setHasFixedSize(true)
            layoutManager = hourForecastLayoutManager
            adapter = hourForecastAdapter }

        dailyForecastRV=findViewById<RecyclerView>(R.id.daily_forecast_rv).apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled=false
            layoutManager=dailyForecastLayoutManager
            adapter=dailyForecastAdapter
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        if (sharedPreferences.locationExist()) {
            getWeatherForCity(sharedPreferences.getLatestLocation())
            val latestLocation = sharedPreferences.getLatestLocation()
            actualWeatherSearchView.queryHint = latestLocation
            getWeatherForCity(latestLocation)
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

                actualTemp.text = it.main.temp.roundToInt().toString() + setActualUnit()
                actualWeatherDesc.text = it.weather[0].description
                actualFeelsLike.text = "Feels like " + it.main.feels_like.roundToInt() + setActualUnit()
                weather_time.text =convertUnixTime(it.dt, it.timezone)

                val imgType: String = it.weather[0].icon
                Picasso.with(applicationContext).load("http://openweathermap.org/img/wn/" + imgType + "@2x.png")
                    .into(actualWeatherImageView, object : Callback {
                        override fun onSuccess() {

                            val lat = it.coord.lat.toString()
                            val lon = it.coord.lon.toString()
                            getOneCallWeather(lat, lon)
                        }

                        override fun onError() {
                            Log.d("t", "E R R O R MAIN")

                        }

                    })
            }


            )

    }

    fun getOneCallWeather(lat: String, lon: String) {
        weatherViewModel.initOneCall(lat, lon, sharedPreferences.getUnit(), sharedPreferences.currentLang())
        weatherViewModel.getOneCallRepository()?.observe(this, Observer {
            current_humidity.text=it.current.humidity.toString()+ "%"
            current_pressure.text=it.current.pressure.toString()+ " hPa"
            current_visibility.text=it.current.visibility.toString() +" m"
            current_dev_point.text=it.current.dew_point.toString() + setActualUnit()
            current_UV_index.text=it.current.uvi.toString()
            hourForecastAdapter.setHourForecast(it.hourly)
            hourForecastAdapter.notifyDataSetChanged()
            dailyForecastAdapter.setDailyForecast(it.daily)
            dailyForecastAdapter.notifyDataSetChanged()



        })

    }

     private fun setActualUnit(): String {
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
    private fun convertUnixTime(unixDate: Int, timeZone: Int): String {
        val date = Date((unixDate + (timeZone)) * 1000L)
        val format: DateFormat = SimpleDateFormat("dd-MM, HH:mm")
        return format.format(date)
    }








}

