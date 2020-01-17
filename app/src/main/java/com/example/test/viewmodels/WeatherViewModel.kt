package com.example.test.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.location.Location
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.test.R
import com.example.test.api.WeatherApiService
import com.example.test.model.WeatherDataM
import com.example.test.utils.SharePreferencesHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherViewModel (application: Application): BaseViewModel(application){
    private val weatherService = WeatherApiService()
    private val disposable = CompositeDisposable()
    private val prefHelper = SharePreferencesHelper(application)
    private val gson = Gson()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat : String = "-34.60"
    private var lon : String = "-58.38"

    val weatherLiveData = MutableLiveData<WeatherDataM>()
    val loadError = MutableLiveData<Boolean>()

    fun getLocationPermission(activity: Activity){

        Dexter.withActivity(activity)
        .withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                report?.let {
                    if(report.areAllPermissionsGranted()){
                        obtenerUbicacion(activity)
                    } else{
                        fetchFromRemote(lat, lon, activity)
                    }
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?,
                token: PermissionToken?
            ) {
                fetchFromRemote(lat, lon, activity)
            }
        }).check()
    }

    @SuppressLint("RestrictedApi")
    private fun obtenerUbicacion(activity: Activity){
        fusedLocationClient= FusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation?.addOnSuccessListener(activity, object:
            OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                if(location != null){
                    lat = location?.latitude.toString()
                    lon = location?.latitude.toString()
                    fetchFromRemote(lat, lon, activity)
                } else{
                    fetchFromCache(activity)
                }
            }
        })
    }

    fun refreshData(activity: Activity){
        if(prefHelper.getWeatherData().equals("")){
            getLocationPermission(activity)
        }else{
            fetchFromCache(activity)
        }
    }

    private fun fetchFromRemote(lat: String, lon: String, activity: Activity){
        disposable.add(
            weatherService.getWeatherData(lat, lon, activity.getResources().getString(R.string.api_key))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherDataM>(){
                    override fun onSuccess(weatherData : WeatherDataM) {
                        weatherLiveData.value = weatherData
                        val json = gson.toJson(weatherData)
                        prefHelper.saveWeatherData(json)
                        Toast.makeText(getApplication(), activity.getResources().getString(R.string.weather_remote), Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun fetchFromCache(activity: Activity){
        val obj = gson.fromJson(prefHelper.getWeatherData(), WeatherDataM::class.java)
        weatherLiveData.value = obj
        Toast.makeText(getApplication(), activity.getResources().getString(R.string.weather_local), Toast.LENGTH_SHORT).show()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}