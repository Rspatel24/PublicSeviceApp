package com.example.contr.publicservice.Services

import android.app.Activity
import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import android.support.v7.app.AlertDialog

class checkconnection {


    fun check(context: Context) : Boolean
    {
        val connectivityManager =
            context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
         var isConnected = activeNetwork?.isConnectedOrConnecting == true


        return isConnected
    }
}