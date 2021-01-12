package com.example.nasa_mars_api_service.repository.interfaces

import androidx.lifecycle.LiveData
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 20:29
 * Project: NASA-Mars-API-Service
 */
interface BaseRepository {

    suspend fun getPictureOfDay(): PictureOfDayPhoto

    suspend fun getAllFavoritesPhotos(): List<FavouritePhoto>

    suspend fun getAllMarsPhotosFromCache(): List<MarsPhoto>
    suspend fun getMarsPhotoFromCache(id: Int): MarsPhoto

    suspend fun searchMarsPhotos(page: Int, date: MarsDateTypes, rover: MarsRovers, camera: MarsRoversCamera): List<MarsPhoto>
    suspend fun getLastMarsPhotos(page: Int = 1): List<MarsPhoto>

    suspend fun addPhotoToFavourite(marsPhoto: MarsPhoto)
    suspend fun addPhotoToFavourite(pictureOfDayPhoto: PictureOfDayPhoto)
    suspend fun addPhotoToFavourite(favouritePhoto: FavouritePhoto)

    suspend fun deleteFavouritePhoto(marsPhoto: MarsPhoto)
    suspend fun deleteFavouritePhoto(pictureOfDayPhoto: PictureOfDayPhoto)
    suspend fun deleteFavouritePhoto(favouritePhoto: FavouritePhoto)

    suspend fun getLastPictureOfDayFromCash(): PictureOfDayPhoto
    suspend fun getPictureOfDayFromCash(id: Int): PictureOfDayPhoto

    fun getNumberOfCashedMarsPhotos(): Int
    fun getNumberOfCashedFavouritesPhotos(): Int
    fun getNumberOfCashedPictureOfDayPhotos(): Int

    fun clearNumberOfAvailableSearchMarsPhotos()

    val numberOfAvailableMarsPhotosPages: Int
    val numberOfAvailableSearchMarsPhotosPages: Int
}