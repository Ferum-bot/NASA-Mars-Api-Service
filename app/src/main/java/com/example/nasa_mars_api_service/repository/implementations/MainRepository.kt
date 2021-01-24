package com.example.nasa_mars_api_service.repository.implementations

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
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

class MainRepository private constructor(
        private val localSourceMarsPhotos: MarsPhotoDao,
        private val localSourceFavoritePhotos: FavoritePhotoDao,
        private val localSourcePictureOfDayDao: PictureOfDayDao,
        private val remoteSource: MarsPhotosService,
        private val preferences: BaseApplicationPreferences
): BaseRepository {

    override val numberOfAvailableMarsPhotosPages: Int
        get() = preferences.getNumberOfAvailableMarsPhotoPages()
    override val numberOfAvailableSearchMarsPhotosPages: Int
        get() = preferences.getNumberOfAvailableSearchMarsPhotoPages()

    override suspend fun getMarsPhotoFromCache(id: Int): MarsPhoto {
        val result = localSourceMarsPhotos.getPhoto(id)
        return result.toMarsPhoto()
    }

    override suspend fun getPictureOfDayFromCash(id: Int): PictureOfDayPhoto {
        val result = localSourcePictureOfDayDao.getPictureOfDay(id)
        return result.toPictureOfDayPhoto()
    }

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
        val result = localSourcePictureOfDayDao.getLastPicture()
        return result.toPictureOfDayPhoto()
    }

    override suspend fun getLastMarsPhotos(page: Int): List<MarsPhoto> {
        val result = remoteSource.getLastMarsPhotos(page).listPhotos
        updateNumberOfPhotosInPreferences(result)
        updateNumberOfMarsPhotoPagesInPreferences(page)
        localSourceMarsPhotos.insertPhotos(result.map { it.toMarsPhotoDB() })
        return result.map { it.toMarsPhoto() }
    }

    override suspend fun getPictureOfDay(): PictureOfDayPhoto {
        val result = remoteSource.getPictureOfDayPhoto()[0]
        if (!localSourcePictureOfDayDao.isPictureExists(result.description)) {
            val number = preferences.getNumberOfAvailablePictureOfDayPhotos()
            preferences.updateNumberOfAvailablePictureOfDayPhotos(number + 1)
        }
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
                    MarsDateTypes.EARTH_DATE -> remoteSource.getCuriosityMarsPhotosFromEarthDate(page, date.date.toSearchDateFormat(), camera.name).listPhotos
                    MarsDateTypes.MARS_SOL -> remoteSource.getCuriosityMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name).listPhotos
                }
            }
            MarsRovers.OPPORTUNITY -> {
                when(date) {
                    MarsDateTypes.EARTH_DATE -> remoteSource.getOpportunityMarsPhotosFromEarthDate(page, date.date.toSearchDateFormat(), camera.name).listPhotos
                    MarsDateTypes.MARS_SOL -> remoteSource.getOpportunityMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name).listPhotos
                }
            }
            MarsRovers.SPIRIT -> {
                when(date) {
                    MarsDateTypes.EARTH_DATE -> remoteSource.getSpiritMarsPhotosFromEarthDate(page, date.date.toSearchDateFormat(), camera.name).listPhotos
                    MarsDateTypes.MARS_SOL -> remoteSource.getSpiritMarsPhotosFromMarsSol(page, date.date.toInt(), camera.name).listPhotos
                }
            }
        }
        updateNumberOfPhotosInPreferences(result)
        updateNumberOfSearchMarsPhotosPagesInPreferences(page)
        localSourceMarsPhotos.insertPhotos(result.map { it.toMarsPhotoDB() })
        return result.map { it.toMarsPhoto() }
    }

    override suspend fun addPhotoToFavourite(marsPhoto: MarsPhoto) {
        if (!localSourceFavoritePhotos.isPhotoExists(marsPhoto.id)) {
            val number = preferences.getNumberOfFavouritePhotos()
            preferences.updateNumberOfFavouritePhotos(number + 1)
        }
        localSourceFavoritePhotos.insertPhoto(marsPhoto.toFavouritePhotoDB())
    }

    override suspend fun addPhotoToFavourite(pictureOfDayPhoto: PictureOfDayPhoto) {
        if (localSourceFavoritePhotos.isPhotoExists(pictureOfDayPhoto.id)) {
            val number = preferences.getNumberOfFavouritePhotos()
            preferences.updateNumberOfFavouritePhotos(number + 1)
        }
        localSourcePictureOfDayDao.insertPicture(pictureOfDayPhoto.toPictureOfDayPhotoDB())
        localSourceFavoritePhotos.insertPhoto(pictureOfDayPhoto.toFavouritePhoto().toFavouritePhotoDB())
    }

    override suspend fun addPhotoToFavourite(favouritePhoto: FavouritePhoto) {
        if (localSourceFavoritePhotos.isPhotoExists(favouritePhoto.id)) {
            val number = preferences.getNumberOfFavouritePhotos()
            preferences.updateNumberOfFavouritePhotos(number + 1)
        }
        localSourceFavoritePhotos.insertPhoto(favouritePhoto.toFavouritePhotoDB())
    }

    override suspend fun deleteFavouritePhoto(favouritePhoto: FavouritePhoto) {
        deleteFavouritePhotoFromPreferences()
        localSourceFavoritePhotos.deletePhoto(favouritePhoto.toFavouritePhotoDB())
    }

    override suspend fun deleteFavouritePhoto(marsPhoto: MarsPhoto) {
        deleteFavouritePhotoFromPreferences()
        localSourceFavoritePhotos.deletePhoto(marsPhoto.toFavouritePhotoDB())
    }

    override suspend fun deleteFavouritePhoto(pictureOfDayPhoto: PictureOfDayPhoto) {
        deleteFavouritePhotoFromPreferences()
        localSourceFavoritePhotos.deletePhoto(pictureOfDayPhoto.toFavouritePhotoDB())
    }

    override fun clearNumberOfAvailableSearchMarsPhotos() {
        preferences.updateNumberOfAvailableSearchMarsPhotoPages(0)
    }

    private fun deleteFavouritePhotoFromPreferences() {
        val number = preferences.getNumberOfFavouritePhotos()
        preferences.updateNumberOfFavouritePhotos(number - 1)
    }

    private suspend fun updateNumberOfPhotosInPreferences(photos: List<MarsPhotoVO>) {
        for (photo in photos) {
            if (!localSourceMarsPhotos.isPhotoExists(photo.id)) {
                val number = preferences.getNumberOfAvailableMarsPhotos()
                preferences.updateNumberOfAvailableMarsPhotos(number + 1)
            }
        }
    }

    private fun updateNumberOfMarsPhotoPagesInPreferences(page: Int){
        val currentNumberOfPages = preferences.getNumberOfAvailableMarsPhotoPages()
        if (currentNumberOfPages < page) {
            preferences.updateNumberOfAvailableMarsPhotoPages(page)
        }
    }

    private fun updateNumberOfSearchMarsPhotosPagesInPreferences(page: Int) {
        val currentNumberOfPages = preferences.getNumberOfAvailableSearchMarsPhotoPages()
        if (currentNumberOfPages < page) {
            preferences.updateNumberOfAvailableSearchMarsPhotoPages(page)
        }
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