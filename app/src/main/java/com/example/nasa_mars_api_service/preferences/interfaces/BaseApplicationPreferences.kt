package com.example.nasa_mars_api_service.preferences.interfaces

import android.content.SharedPreferences

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 20:31
 * Project: NASA-Mars-API-Service
 */
interface BaseApplicationPreferences {

    fun getNumberOfFavouritePhotos(): Int
    fun getNumberOfAvailableMarsPhotos(): Int
    fun getNumberOfAvailablePages(): Int
    fun getNumberOfAvailablePictureOfDayPhotos(): Int

    fun updateNumberOfFavouritePhotos(number: Int)
    fun updateNumberOfAvailableMarsPhotos(number: Int)
    fun updateNumberOfAvailablePages(number: Int)
    fun updateNumberOfAvailablePictureOfDayPhotos(number: Int)

}