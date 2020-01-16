package com.example.test.api

import com.example.test.model.WeatherDataM
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query;

interface weatherApi {
    @GET("data/2.5/weather?")
    fun getData(@Query("lat") lat: String,
                @Query("lon") lon: String,
                @Query("APPID") APPID: String): Single<WeatherDataM>
}