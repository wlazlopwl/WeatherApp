package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.model.current.Main
import com.example.weatherapp.model.oneapi.Hourly
import com.example.weatherapp.ui.MainActivity
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourForecastAdapter() : RecyclerView.Adapter<HourForecastAdapter.ForecastHolder>() {

     private var hourForecast = ArrayList<Hourly>()



        class ForecastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temp = itemView.findViewById<TextView>(R.id.hour_rv_temp)
        val time = itemView.findViewById<TextView>(R.id.hour_rv_time)
        val icon = itemView.findViewById<ImageView>(R.id.hour_rv_icon)

            fun bind(item: Hourly){
                temp.text=item.temp.toString()

                val date = Date((item.dt) * 1000L)
                val format: DateFormat = SimpleDateFormat("HH:mm")
//                format.timeZone = TimeZone.getTimeZone("Etc/UTC")
                var convertedHour = format.format(date)


                time.text=convertedHour
                Picasso.with(itemView.context).load("http://openweathermap.org/img/wn/" + item.weather[0].icon + "@2x.png")
                    .into(icon)

            }



    }


    override fun  onCreateViewHolder(parent: ViewGroup, viewType: Int): HourForecastAdapter.ForecastHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_rv_layout,parent,false)
        return ForecastHolder(view)

    }

    override fun getItemCount(): Int {
        return hourForecast.size
    }

    override fun onBindViewHolder(holder: HourForecastAdapter.ForecastHolder, position: Int) {
        holder.bind(hourForecast[position])



    }

    fun setHourForecast(item: List<Hourly>) {

        this.hourForecast= item as ArrayList<Hourly>
        
    }

}
