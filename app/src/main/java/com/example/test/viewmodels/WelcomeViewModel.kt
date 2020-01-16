package com.example.test.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.test.utils.SharePreferencesHelper

class WelcomeViewModel(application: Application): BaseViewModel(application) {

    private val prefHelper = SharePreferencesHelper(application)
    val UserLiveData = MutableLiveData<String>()

    fun checkUserExist(): String? {
        val userExist = prefHelper.getUserName()
        UserLiveData.value = userExist

        if(userExist.equals("")){
            return ""
        }else{
            return userExist
        }
    }

    fun saveUserName(user: String){
        UserLiveData.value = user
        prefHelper.saveUserName(user)
    }
}