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
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFrag : BaseFrag() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.setTitle(getResources().getString(R.string.yourWeather));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData(this.activity!!)
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.weatherLiveData.observe(this, Observer { wea ->
            wea?.let {
                weather.text = ""+getResources().getString(R.string.weather)+" "+it.weather.get(0).description
                temp.text = ""+getResources().getString(R.string.temperature)+" "+it.main.temp
                visibility.text = ""+getResources().getString(R.string.visibility)+" "+it.visibility
                wind.text = ""+getResources().getString(R.string.wind)+" "+it.wind.speed
                progresBar.visibility= View.GONE
            }
        })
    }

}
