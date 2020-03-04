package com.example.test.utils

interface SharePreferencesHelper {
    fun saveUserName(name: String)
    fun getUserName():String?
    fun getWeatherData():String?
    fun saveWeatherData(wd: String)
}