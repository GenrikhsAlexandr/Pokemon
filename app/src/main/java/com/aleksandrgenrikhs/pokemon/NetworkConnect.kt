package com.aleksandrgenrikhs.pokemon

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.aleksandrgenrikhs.pokemon.domain.NetworkConnectionChecker

object NetworkConnected : NetworkConnectionChecker {

    override fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}