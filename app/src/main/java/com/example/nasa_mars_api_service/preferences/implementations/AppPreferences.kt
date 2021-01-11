package com.example.nasa_mars_api_service.preferences.implementations

import android.content.Context
import android.content.SharedPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences

class AppPreferences private constructor(context: Context): BaseApplicationPreferences{

    private val storage: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getNumberOfAvailableSearchMarsPhotoPages(): Int {
        return storage.getInt(NUMBER_OF_AVAILABLE_SEARCH_MARS_PHOTOS_PAGES_NAME, 0)
    }

    override fun updateNumberOfAvailableSearchMarsPhotoPages(number: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_AVAILABLE_SEARCH_MARS_PHOTOS_PAGES_NAME, number)
        editor.apply()
    }

    override fun getNumberOfAvailableMarsPhotoPages(): Int {
        return storage.getInt(NUMBER_OF_AVAILABLE_MARS_PHOTOS_PAGES_NAME, 0)
    }

    override fun getNumberOfAvailablePictureOfDayPhotos(): Int {
        return storage.getInt(NUMBER_OF_PICTURE_OF_DAY_PHOTOS_NAME, 0)
    }

    override fun updateNumberOfFavouritePhotos(number: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_FAVORITE_PHOTOS_NAME, number)
        editor.apply()
    }

    override fun updateNumberOfAvailableMarsPhotoPages(number: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_AVAILABLE_MARS_PHOTOS_PAGES_NAME, number)
        editor.apply()
    }

    override fun updateNumberOfAvailablePictureOfDayPhotos(number: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_PICTURE_OF_DAY_PHOTOS_NAME, number)
        editor.apply()
    }

    override fun getNumberOfFavouritePhotos(): Int {
        return storage.getInt(NUMBER_OF_FAVORITE_PHOTOS_NAME, 0)
    }

    override fun getNumberOfAvailableMarsPhotos(): Int {
        return storage.getInt(NUMBER_OF_AVAILABLE_PHOTOS_NAME, 0)
    }

    override fun updateNumberOfAvailableMarsPhotos(number: Int) {
        val editor = storage.edit()
        editor.putInt(NUMBER_OF_AVAILABLE_PHOTOS_NAME, number)
        editor.apply()
    }

    companion object {

        @Volatile
        private var INSTANCE: AppPreferences? = null

        private const val PREFERENCES_NAME = "APPLICATION_SHARED_PREFERENCES"
        private const val NUMBER_OF_AVAILABLE_MARS_PHOTOS_PAGES_NAME = "number_of_available_mars_photos_pages"
        private const val NUMBER_OF_AVAILABLE_PHOTOS_NAME = "number_of_available_photos"
        private const val NUMBER_OF_FAVORITE_PHOTOS_NAME = "number_of_favourite_photos"
        private const val NUMBER_OF_PICTURE_OF_DAY_PHOTOS_NAME = "number_of_picture_of_day_photos"
        private const val NUMBER_OF_AVAILABLE_SEARCH_MARS_PHOTOS_PAGES_NAME = "number_of_available_search_mars_photos_pages"

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