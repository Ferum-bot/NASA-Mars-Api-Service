package com.example.nasa_mars_api_service.repository

import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.toMarsPhoto
import com.example.nasa_mars_api_service.core.toMarsPhotoDB
import com.example.nasa_mars_api_service.database.dao.DeletedMarsPhotoDao
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.network.api.MarsPhotosService
import com.example.nasa_mars_api_service.preferences.AppPreferences

class MainRepository(
    private val localSource: MarsPhotoDao,
    private val localSourceDeleted: DeletedMarsPhotoDao,
    private val remoteSource: MarsPhotosService,
    private val preferences: AppPreferences
) {

    val numberOfAvailablePages: Int
    get() = preferences.getNumberOfAvailablePages()

    val numberOfAvailablePhotos: Int
    get() = preferences.getNumberOfAvailablePhotos()

    suspend fun getPhotosFromNetwork(page: Int): List<MarsPhoto> {
        val result = remoteSource.getMarsPhotos(page)
        val list = result.map { it.toMarsPhotoDB() }

        localSource.updatePhotos(list)
        updatePreferences(page)

        return list.map { it.toMarsPhoto() }
    }

    suspend fun getAllPhotosFromDatabase(): List<MarsPhoto> {
        val result = localSource.getAllPhotos()
        return result.map { it.toMarsPhoto() }
    }

    suspend fun updatePhotos(page: Int) {
        val result = remoteSource.getMarsPhotos(page)
        val list = result.map { it.toMarsPhotoDB() }

        localSource.updatePhotos(list)
        updatePreferences(page)
    }

    private fun updatePreferences(page: Int) {
        val currentNumberOfPages = preferences.getNumberOfAvailablePages()
        if (page > currentNumberOfPages) {
            preferences.updateNumberOfAvailablePages(page)
            val currentNumberOfPhotos = preferences.getNumberOfAvailablePhotos()
            preferences.updateNumberOfAvailablePhotos(currentNumberOfPhotos + 25)
        }
    }

}