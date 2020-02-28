package com.example.test

import com.example.test.viewmodels.WeatherViewModel
import com.example.test.viewmodels.WelcomeViewModel
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val appModule = module {
    // MyViewModel ViewModel
    viewModel { WelcomeViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
}