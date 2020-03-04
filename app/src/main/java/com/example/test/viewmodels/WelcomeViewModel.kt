package com.example.test.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.test.utils.SharePreferencesHelper
import com.example.test.utils.SharePreferencesHelperImp
import org.koin.core.KoinComponent
import org.koin.core.inject

class WelcomeViewModel(application: Application): BaseViewModel(application), KoinComponent {

    val prefHelper by inject<SharePreferencesHelper>()
    val userLiveData = MutableLiveData<String>()

    fun checkUserExist(): String? {
        val userExist = prefHelper.getUserName()
        userLiveData.value = userExist
        if(userExist.equals("") || userExist == null){
            return ""
        }else{
            return userExist
        }
    }

    fun saveUserName(user: String){
        userLiveData.value = user
        prefHelper.saveUserName(user)
    }

    fun getButtonIsShow(): Boolean {
        if (userLiveData.value.equals("") || userLiveData.value == null) {
            return false
        } else {
            return true
        }
    }
}