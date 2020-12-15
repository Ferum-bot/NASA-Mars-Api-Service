package com.example.nasa_mars_api_service.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(context: Context){

    private val storage: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)


    companion object {

        private var INSTANCE: AppPreferences? = null

        private const val PREFERENCES_NAME = "APPLICATION_SHARED_PREFERENCES"

        fun getInstance(context: Context): AppPreferences {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AppPreferences(context)
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}