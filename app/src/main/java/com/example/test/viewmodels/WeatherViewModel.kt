package com.example.test.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.test.api.WeatherApiService
import com.example.test.model.WeatherDataM
import com.example.test.utils.SharePreferencesHelper
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherViewModel (application: Application): BaseViewModel(application){
    private val weatherService = WeatherApiService()
    private val disposable = CompositeDisposable()
    private val prefHelper = SharePreferencesHelper(application)
    private val gson = Gson()

    val weatherLiveData = MutableLiveData<WeatherDataM>()
    val loadError = MutableLiveData<Boolean>()
    val datafromLocalLoaded = MutableLiveData<Boolean>()
    val getLocationPermission = MutableLiveData<Boolean>()
    val noConnection = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun chekConnectivity(cnx: Boolean): Boolean{
        if(cnx){
            refreshData()
            return true
        }else{
            noConnection.value = true
            loading.value = false
            return false
        }
    }

    fun refreshData(){
        if(prefHelper.getWeatherData().equals("") || prefHelper.getWeatherData() == null){
            getLocationPermission.value = true
        }else{
            fetchFromCache()
        }
    }

    fun fetchFromRemote(lat: String, lon: String, key: String){
        loading.value = true
        disposable.add(
            weatherService.getWeatherData(lat, lon, key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherDataM>(){
                    override fun onSuccess(weatherData : WeatherDataM) {
                        weatherLiveData.value = weatherData
                        val json = gson.toJson(weatherData)
                        prefHelper.saveWeatherData(json)
                        loading.value = false
                        datafromLocalLoaded.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    fun fetchFromCache(){
        val obj = gson.fromJson(prefHelper.getWeatherData(), WeatherDataM::class.java)
        weatherLiveData.value = obj
        datafromLocalLoaded.value = true
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}