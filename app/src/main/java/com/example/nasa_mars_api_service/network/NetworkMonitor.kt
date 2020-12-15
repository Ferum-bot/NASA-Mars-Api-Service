package com.example.nasa_mars_api_service.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.example.nasa_mars_api_service.core.Variables

class NetworkMonitor
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
constructor(private val application: Application) {

    fun startNetworkCallback() {
        val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()

        manager.registerNetworkCallback(
            builder.build(),
            object: ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Variables.isNetworkConnectionAvailable = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Variables.isNetworkConnectionAvailable = false
                }
            }
        )
    }

    fun stopNetworkCallback() {
        val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

}