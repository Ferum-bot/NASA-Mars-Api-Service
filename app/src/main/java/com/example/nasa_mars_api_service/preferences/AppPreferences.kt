package com.example.nasa_mars_api_service.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(context: Context){

    private val storage: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)


    fun getNumberOfAvailablePages(): Int {
        return storage.getInt(NUMBER_OF_AVAILABLE_PAGES_NAME, 0)
    }

    fun updateNumberOfAvailablePages(value: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_AVAILABLE_PAGES_NAME, value)
        editor.apply()
    }

    fun getNumberOfAvailablePhotos(): Int {
        return storage.getInt(NUMBER_OF_AVAILABLE_PHOTOS_NAME, 0)
    }

    fun updateNumberOfAvailablePhotos(value: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_AVAILABLE_PHOTOS_NAME, value)
        editor.apply()
    }

    companion object {

        private var INSTANCE: AppPreferences? = null

        private const val PREFERENCES_NAME = "APPLICATION_SHARED_PREFERENCES"
        private const val NUMBER_OF_AVAILABLE_PAGES_NAME = "number_of_available_pages"
        private const val NUMBER_OF_AVAILABLE_PHOTOS_NAME = "number_of_available_photos"

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