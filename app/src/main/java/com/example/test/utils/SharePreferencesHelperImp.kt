package com.example.test.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharePreferencesHelperImp: SharePreferencesHelper{

    companion object{
        private const val USER_NAME = "UserName"
        private const val WEATHER_DATA = "WeatherData"
        private var prefs: SharedPreferences? = null
        @Volatile private var instance: SharePreferencesHelperImp? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharePreferencesHelperImp = instance ?: synchronized(LOCK){
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context: Context):SharePreferencesHelperImp{
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharePreferencesHelperImp()
        }
    }

    override fun saveUserName(name: String){
        prefs?.edit(commit = true){
            putString(USER_NAME, name)
        }
    }

    override fun getUserName() = prefs?.getString(USER_NAME, "")


    override fun saveWeatherData(wd: String){
        prefs?.edit(commit = true){
            putString(WEATHER_DATA, wd)
        }
    }

    override fun getWeatherData() = prefs?.getString(WEATHER_DATA, "")
}