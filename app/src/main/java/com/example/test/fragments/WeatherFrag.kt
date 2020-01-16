package com.example.test.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.test.R
import com.example.test.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFrag : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat : String = "-34.60"
    private var lon : String = "-58.38"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        getActivity()?.title= getResources().getString(R.string.yourWeather);
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData(this.activity!!)
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.weatherLiveData.observe(this, Observer { wea ->
            wea?.let {
                weather.text = "Wheter: "+it.weather.get(0).description
                temp.text = "Temperature: "+it.main.temp
                visibility.text = "Visibility: "+it.visibility
                wind.text = "Wind Speed: "+it.wind.speed
                progresBar.visibility= View.GONE
            }
        })
    }

}
