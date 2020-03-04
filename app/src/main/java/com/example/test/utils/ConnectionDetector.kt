package com.example.test.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface ConnectionDetector {
    fun isConnectingToInternet(context: Context): Boolean
    fun getConnectionStatus(): MutableLiveData<Boolean>
}