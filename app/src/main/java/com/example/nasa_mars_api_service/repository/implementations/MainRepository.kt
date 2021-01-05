package com.example.nasa_mars_api_service.repository.implementations

import androidx.lifecycle.LiveData
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.dao.FavoritePhotoDao
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.network.api.MarsPhotosService
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import javax.inject.Inject

class MainRepository private constructor(
        private val localSourceMarsPhotos: MarsPhotoDao,
        private val localSourceFavoritePhotos: FavoritePhotoDao,
        private val remoteSource: MarsPhotosService,
        private val preferences: BaseApplicationPreferences
): BaseRepository {

    override suspend fun getLastMarsPhotos(): List<MarsPhoto> {
        TODO("Not yet implemented")
    }

    override suspend fun getPictureOfDay(): PictureOfDayPhoto {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavoritesPhotos(): List<FavouritePhoto> {
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


    companion object {

        @Volatile
        private var INSTANCE: BaseRepository? = null

        fun getInstance(localSourceMarsPhotos: MarsPhotoDao,
                        localSourceFavoritePhotos: FavoritePhotoDao,
                        remoteSource: MarsPhotosService,
                        preferences: BaseApplicationPreferences): BaseRepository {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = MainRepository(localSourceMarsPhotos, localSourceFavoritePhotos, remoteSource, preferences)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}