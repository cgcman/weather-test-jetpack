package com.example.test.api

import com.example.test.model.WeatherDataM
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiService {
    private var base_url = "https://api.openweathermap.org/"
    private var api = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(weatherApi::class.java)

    fun getWeatherData(lat: String, lon: String, APPID: String): Single<WeatherDataM> {
        return  api.getData(lat, lon, APPID)
    }
}
