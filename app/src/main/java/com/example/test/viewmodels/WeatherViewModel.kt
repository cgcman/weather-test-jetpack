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
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeatherViewModel (application: Application): BaseViewModel(application), KoinComponent {
    val weatherService by inject<WeatherApiService>()
    val prefHelper by inject<SharePreferencesHelper>()

    private val disposable = CompositeDisposable()
    private val gson = Gson()

    val weatherLiveData = MutableLiveData<WeatherDataM>()
    val loadError = MutableLiveData<Boolean>()
    val datafromLocalLoaded = MutableLiveData<Boolean>()
    val getLocationPermission = MutableLiveData<Boolean>()
    val noConnection = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun chekConnectivity(cnx: Boolean): Boolean{
        if(cnx){
            noConnection.value = false
            refreshData()
            return true
        }else{
            noConnection.value = true
            return false
        }
    }

    fun checkDataExist(): String? {
        val dataExist = prefHelper.getWeatherData()
        if(dataExist.equals("") || dataExist == null){
            return ""
        }else{
            return dataExist
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
                        datafromLocalLoaded.value = true
                        loading.value = false
                        weatherLiveData.value = weatherData
                        val json = gson.toJson(weatherData)
                        prefHelper.saveWeatherData(json)
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
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}