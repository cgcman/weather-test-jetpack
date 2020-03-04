package com.example.test

import com.example.test.api.WeatherApiService
import com.example.test.api.WeatherApiServiceImp
import com.example.test.utils.ConnectionDetector
import com.example.test.utils.ConnectionDetectorImp
import com.example.test.utils.SharePreferencesHelper
import com.example.test.utils.SharePreferencesHelperImp
import com.example.test.viewmodels.WeatherViewModel
import com.example.test.viewmodels.WelcomeViewModel
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val appModule = module {
    // MyViewModel ViewModel
    single { WeatherApiServiceImp() as WeatherApiService }
    single { SharePreferencesHelperImp() as SharePreferencesHelper }
    single { ConnectionDetectorImp() as ConnectionDetector }
    viewModel { WelcomeViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
}