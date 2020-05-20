package com.chydee.atom360testapp.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.chydee.atom360testapp.di.AppComponent
import com.chydee.atom360testapp.di.DaggerAppComponent

@Suppress("DEPRECATION")
open class Atom360App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    companion object {
        fun hasNetwork(context: Context): Boolean {
            var isConnected = false // Initial Value
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }
    }

}