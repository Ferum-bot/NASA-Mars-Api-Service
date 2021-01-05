package com.example.nasa_mars_api_service.di.modules

import android.content.Context
import com.example.nasa_mars_api_service.core.models.AndroidResourceProvider
import com.example.nasa_mars_api_service.core.models.ResourceProvider
import com.example.nasa_mars_api_service.database.db.MainDatabase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 23:04
 * Project: NASA-Mars-API-Service
 */
@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindResourceProvider(provider: AndroidResourceProvider): ResourceProvider
}