package com.example.nasa_mars_api_service.repository.implementations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasa_mars_api_service.core.*
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.dao.FavoritePhotoDao
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.database.dao.PictureOfDayDao
import com.example.nasa_mars_api_service.network.api.MarsPhotosService
import com.example.nasa_mars_api_service.network.models.MarsPhotoVO
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import javax.inject.Inject

class MainRepository private constructor(
        private val localSourceMarsPhotos: MarsPhotoDao,
        private val localSourceFavoritePhotos: FavoritePhotoDao,
        private val localSourcePictureOfDayDao: PictureOfDayDao,
        private val remoteSource: MarsPhotosService,
        private val preferences: BaseApplicationPreferences
): BaseRepository {


    override fun getNumberOfCashedMarsPhotos(): Int {
        return preferences.getNumberOfAvailableMarsPhotos()
    }

    override fun getNumberOfCashedPictureOfDayPhotos(): Int {
       return preferences.getNumberOfAvailablePictureOfDayPhotos()
    }

    override fun getNumberOfCashedFavouritesPhotos(): Int {
        return preferences.getNumberOfFavouritePhotos()
    }

    override suspend fun getLastPictureOfDayFromCash(): PictureOfDayPhoto {
        TODO("Not yet implemented")
    }

    override suspend fun getLastMarsPhotos(): List<MarsPhoto> {
        TODO("Not yet implemented")
    }

    override suspend fun getPictureOfDay(): PictureOfDayPhoto {
        val result = remoteSource.getPictureOfDayPhoto()
        localSourcePictureOfDayDao.insertPicture(result.toPictureOfDayPhotoDB())
        return result.toPictureOfDayPhoto()
    }

    override suspend fun getAllFavoritesPhotos(): List<FavouritePhoto> {
        val result = localSourceFavoritePhotos.getAllPhotos()
        return result.map { it.toFavouritePhoto() }
    }

    override suspend fun getAllMarsPhotosFromCache(): List<MarsPhoto> {
        val result = localSourceMarsPhotos.getAllPhotos()
        return result.map { it.toMarsPhoto() }
    }

    override suspend fun searchMarsPhotos(page: Int, date: MarsDateTypes, rover: MarsRovers, camera: MarsRoversCamera): List<MarsPhoto> {
        val result: List<MarsPhotoVO> = when(rover) {
            MarsRovers.CURIOSITY -> {
                when(date) {
                    MarsDateTypes.EARTH_DATE -> remoteSource.getCuriosityMarsPhotosFromEarthDate(page, date.date, camera.name)
                    MarsDateTypes.MARS_SOL -> remoteSource.getCuriosityMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name)
                }
            }
            MarsRovers.OPPORTUNITY -> {
                when(date) {
                    MarsDateTypes.EARTH_DATE -> remoteSource.getOpportunityMarsPhotosFromEarthDate(page, date.date, camera.name)
                    MarsDateTypes.MARS_SOL -> remoteSource.getOpportunityMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name)
                }
            }
            MarsRovers.SPIRIT -> {
                when(date) {
                    MarsDateTypes.EARTH_DATE -> remoteSource.getSpiritMarsPhotosFromEarthDate(page, date.date, camera.name)
                    MarsDateTypes.MARS_SOL -> remoteSource.getSpiritMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name)
                }
            }
        }
        localSourceMarsPhotos.insertPhotos(result.map { it.toMarsPhotoDB() })
        return result.map { it.toMarsPhoto() }
    }

    override suspend fun addPhotoToFavourite(marsPhoto: MarsPhoto) {
        localSourceFavoritePhotos.insertPhoto(marsPhoto.toFavouritePhotoDB())
    }

    override suspend fun addPhotoToFavourite(pictureOfDayPhoto: PictureOfDayPhoto) {
        localSourceFavoritePhotos.insertPhoto(pictureOfDayPhoto.toFavouritePhoto().toFavouritePhotoDB())
    }

    override suspend fun addPhotoToFavourite(favouritePhoto: FavouritePhoto) {
        localSourceFavoritePhotos.insertPhoto(favouritePhoto.toFavouritePhotoDB())
    }

    override suspend fun deleteFavouritePhoto(favouritePhoto: FavouritePhoto) {
        localSourceFavoritePhotos.deletePhoto(favouritePhoto.toFavouritePhotoDB())
    }


    companion object {

        @Volatile
        private var INSTANCE: BaseRepository? = null

        fun getInstance(localSourceMarsPhotos: MarsPhotoDao,
                        localSourceFavoritePhotos: FavoritePhotoDao,
                        localSourcePictureOfDayDao: PictureOfDayDao,
                        remoteSource: MarsPhotosService,
                        preferences: BaseApplicationPreferences): BaseRepository {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = MainRepository(localSourceMarsPhotos, localSourceFavoritePhotos, localSourcePictureOfDayDao, remoteSource, preferences)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}