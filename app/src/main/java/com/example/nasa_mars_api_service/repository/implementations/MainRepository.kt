package com.example.nasa_mars_api_service.repository.implementations

import androidx.lifecycle.LiveData
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.network.api.MarsPhotosService
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

class MainRepository(
        private val localSource: MarsPhotoDao,
        private val remoteSource: MarsPhotosService,
        private val preferences: AppPreferences
): BaseRepository {

    override suspend fun getPictureOfDay(): PictureOfDayPhoto {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavoritesPhotos(): LiveData<List<FavouritePhoto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMarsPhotosFromCache(): List<MarsPhoto> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMarsPhotos(date: MarsDateTypes, rover: MarsRovers, camera: MarsRoversCamera): List<MarsPhoto> {
        TODO("Not yet implemented")
    }

    override suspend fun addPhotoToFavourite(marsPhoto: MarsPhoto) {
        TODO("Not yet implemented")
    }

    override suspend fun addPhotoToFavourite(pictureOfDayPhoto: PictureOfDayPhoto) {
        TODO("Not yet implemented")
    }

    override suspend fun addPhotoToFavourite(favouritePhoto: FavouritePhoto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavouritePhoto(favouritePhoto: FavouritePhoto) {
        TODO("Not yet implemented")
    }


}