package com.example.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.oneapi.Daily
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class DailyForecastAdapter : RecyclerView.Adapter<DailyForecastAdapter.DailyHolder>() {
    private var dailyForecast = ArrayList<Daily>()

    class DailyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val forecastDate = itemView.findViewById<TextView>(R.id.daily_rv_time)
        private val description = itemView.findViewById<TextView>(R.id.daily_description)
        private val max_temp = itemView.findViewById<TextView>(R.id.daily_day_rv_temp)
        private val min_temp = itemView.findViewById<TextView>(R.id.daily_night_rv_temp)
        private val icon = itemView.findViewById<ImageView>(R.id.daily_rv_icon)
        fun bind(item: Daily) {

            val date = Date((item.dt) * 1000L)
            val format: DateFormat = SimpleDateFormat("E, dd MMM")
            var convertedHour = format.format(date)


            forecastDate.text = convertedHour


            description.text = item.weather[0].description
            max_temp.text = item.temp.max.roundToInt().toString() + 0x00B0.toChar()
            min_temp.text = item.temp.min.roundToInt().toString() + 0x00B0.toChar()
            Picasso.with(itemView.context)
                .load("http://openweathermap.org/img/wn/" + item.weather[0].icon + "@2x.png")
                .into(icon)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_rv_layout, parent, false)
        return DailyHolder(view)

    }

    override fun getItemCount(): Int {
        return dailyForecast.size
    }

    override fun onBindViewHolder(holder: DailyHolder, position: Int) {
        holder.bind(dailyForecast[position])
    }

    fun setDailyForecast(item: List<Daily>) {
        this.dailyForecast = item as ArrayList<Daily>
    }


}