package com.example.nasa_mars_api_service.application

import android.app.Application
import com.example.nasa_mars_api_service.di.DI
import com.example.nasa_mars_api_service.di.components.DaggerAppComponent
import com.example.nasa_mars_api_service.network.NetworkMonitor
import timber.log.Timber

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        Timber.plant(Timber.DebugTree())
        NetworkMonitor(this).startNetworkCallback()
    }

    private fun initDI() {
        DI.appComponent = DaggerAppComponent.builder()
            .appContext(this)
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}