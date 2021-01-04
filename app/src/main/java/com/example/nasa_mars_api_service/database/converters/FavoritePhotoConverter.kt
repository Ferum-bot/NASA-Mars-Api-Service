package com.example.nasa_mars_api_service.database.converters

import androidx.room.TypeConverter
import com.example.nasa_mars_api_service.core.enums.PhotoTypes

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 15:00
 * Project: NASA-Mars-API-Service
 */
class FavoritePhotoConverter {

    @TypeConverter
    fun fromTypeOfPhoto(typeOfPhoto: PhotoTypes): String {
        return typeOfPhoto.name
    }

    @TypeConverter
    fun toTypeOfPhoto(typeOfPhoto: String): PhotoTypes {
        return when(typeOfPhoto) {
            "MARS_PHOTO" -> PhotoTypes.MARS_PHOTO
            "PICTURE_OF_DAY" -> PhotoTypes.PICTURE_OF_DAY
            else ->
                throw IllegalArgumentException("Invalid type of photo to parse: $typeOfPhoto")
        }
    }

}