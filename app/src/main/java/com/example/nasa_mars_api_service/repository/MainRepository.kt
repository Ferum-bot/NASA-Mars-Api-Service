package com.example.nasa_mars_api_service.repository

import android.util.Log
import com.example.nasa_mars_api_service.core.add
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.toDeletedMarsPhotoDB
import com.example.nasa_mars_api_service.core.toMarsPhoto
import com.example.nasa_mars_api_service.core.toMarsPhotoDB
import com.example.nasa_mars_api_service.database.dao.DeletedMarsPhotoDao
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.database.entities.DeletedMarsPhotoDB
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

    private var deletedPhotosList: MutableList<DeletedMarsPhotoDB> = mutableListOf()


    suspend fun getPhotosFromNetwork(page: Int): List<MarsPhoto> {
        getAllDeletedPhotos()
        val result = remoteSource.getMarsPhotos(page).listPhotos
        val list = result
            .map {
                it.toMarsPhotoDB()
            }
            .filter {
                !deletedPhotosList.contains(it.toDeletedMarsPhotoDB())
            }

        localSource.insertPhotos(list)
        updatePreferences(page)

        return list.map { it.toMarsPhoto() }
    }

    suspend fun deleteMarsPhoto(photo: MarsPhoto) {
        val currentNumberOfPhotos = preferences.getNumberOfAvailablePhotos()
        preferences.updateNumberOfAvailablePhotos(currentNumberOfPhotos - 1)
        val deletedMarsPhotoDB = photo.toDeletedMarsPhotoDB()
        localSourceDeleted.insertPhoto(deletedMarsPhotoDB)
        localSource.deletePhoto(photo.toMarsPhotoDB())
        deletedPhotosList.add(deletedMarsPhotoDB)
    }

    suspend fun getAllDeletedPhotos() {
        val result = localSourceDeleted.getAllDeletedPhoto()
        deletedPhotosList = result.toMutableList()
    }

    suspend fun getAllPhotosFromDatabase(): List<MarsPhoto> {
        getAllDeletedPhotos()
        val result = localSource.getAllPhotos()
        Log.i("repo", "${result.size}")
        return result.map { it.toMarsPhoto() }
    }

    suspend fun getPhoto(id: Int): MarsPhoto {
        Log.i("repos", "$id")
        val result = localSource.getPhoto(id)
        return result.toMarsPhoto()
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