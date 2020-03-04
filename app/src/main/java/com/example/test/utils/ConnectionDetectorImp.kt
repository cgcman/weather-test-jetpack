package com.example.test.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData

class ConnectionDetectorImp : ConnectionDetector{
    val connection = MutableLiveData<Boolean>()

    override fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info)
                    if (i.state == NetworkInfo.State.CONNECTED) {
                        connection.value = true
                        return true
                    }
        }
        connection.value = false
        return false
    }

    override fun getConnectionStatus():MutableLiveData<Boolean>{
        return connection
    }
}