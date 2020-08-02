package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences (private val context: Context) {
    private val PREFS_NAME="weather"
    private var LOCATION = "location"
    private var UNITS="units"
    private var LANG="language"

    private val sharedPreferences: SharedPreferences =context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun locationExist(): Boolean {
        return LOCATION.isNotEmpty()
        }



    fun getLatestLocation(): String {
        return sharedPreferences.getString(LOCATION,"Warszawa").toString()

    }

    fun setCurrentLocation( city:String) {
        editor.putString(LOCATION,city)
        editor.apply()

    }
    fun setUnit(unit:String) {
        editor.putString(UNITS,unit)
        editor.apply()

    }
    fun getUnit():String {
        return sharedPreferences.getString(UNITS,"metric").toString()

    }

    fun currentLang():String{
        return sharedPreferences.getString(LANG,"en").toString()
    }
    fun setLang(lang: String) {
        editor.putString(LANG, lang)
        editor.apply()

    }

}