package com.example.test.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): BaseViewModel(application) {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    fun updateActionBarTitle(title: String) = _title.postValue(title)
}