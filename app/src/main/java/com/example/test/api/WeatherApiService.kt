package com.example.test.api

import com.example.test.model.WeatherDataM
import io.reactivex.Single

interface WeatherApiService {
    fun getWeatherData(lat: String, lon: String, APPID: String): Single<WeatherDataM>
}