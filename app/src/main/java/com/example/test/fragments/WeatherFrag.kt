package com.example.test.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.test.R
import com.example.test.databinding.FragmentWeatherBinding
import com.example.test.model.WeatherDataM
import com.example.test.utils.ConnectionDetector
import com.example.test.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnSuccessListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.android.viewmodel.ext.android.viewModel

class WeatherFrag : BaseFrag() {

    val weatherViewModel: WeatherViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var cd: ConnectionDetector
    private var lat : String = "-34.60"
    private var lon : String = "-58.38"
    private lateinit var dataBindin : FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBindin = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        return dataBindin.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.setTitle(getResources().getString(R.string.yourWeather));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        init()
        initObs()
    }

    private fun init(){
        cd = ConnectionDetector()
        cd.isConnectingToInternet(this.context!!)
    }

    fun getLocationPermission(){
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report?.let {
                        if(report.areAllPermissionsGranted()){
                            obtenerUbicacion()
                        } else{
                            fetch("remote")
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    fetch("remote")
                }
            }).check()
    }

    @SuppressLint("RestrictedApi")
    private fun obtenerUbicacion(){
        fusedLocationClient= FusedLocationProviderClient(this.context!!)
        activity?.let {
            fusedLocationClient.lastLocation?.addOnSuccessListener(it, object:
                OnSuccessListener<Location> {
                override fun onSuccess(location: Location?) {
                    if(location != null){
                        lat = location?.latitude.toString()
                        lon = location?.latitude.toString()
                        Log.e("LATLON",""+lat+"-"+lon)
                        fetch("remote")
                    } else{
                        fetch("remote")
                    }
                }
            })
        }
    }

    private fun fetch(type: String){
        if(type.equals("remote")){
            weatherViewModel.fetchFromRemote(lat, lon, context!!.getResources().getString(R.string.api_key))
        }else{
            weatherViewModel.fetchFromCache()
        }
    }

    private fun initObs(){

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer<WeatherDataM> { wea ->
            wea?.let {
                dataBindin.weatherData = it
            }
        })

        weatherViewModel.datafromLocalLoaded.observe(viewLifecycleOwner, Observer<Boolean> { dfl ->
            dfl?.let {
                if(weatherViewModel.checkDataExist().equals("")){
                    Toast.makeText(context!!, context!!.getResources().getString(R.string.weather_remote), Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(context!!, context!!.getResources().getString(R.string.weather_local), Toast.LENGTH_SHORT).show()
                }
            }
        })

        weatherViewModel.getLocationPermission.observe(viewLifecycleOwner, Observer<Boolean> { glp ->
            glp?.let {
                if(glp){
                    getLocationPermission()
                }
            }
        })

        weatherViewModel.noConnection.observe(viewLifecycleOwner, Observer<Boolean> { nc ->
            nc?.let {
                if(nc){
                    Toast.makeText(context!!, context!!.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                    weatherViewModel.fetchFromCache()
                }
            }
        })

        cd.connection.observe(viewLifecycleOwner, Observer<Boolean> { con ->
            con?.let {
                weatherViewModel.chekConnectivity(it)
            }
        })

        weatherViewModel.loading.observe(viewLifecycleOwner, Observer<Boolean> { pb ->
            pb?.let {
                if(pb){
                    progresBar.visibility= View.VISIBLE
                } else{
                    progresBar.visibility= View.GONE
                }
            }
        })

    }

}
