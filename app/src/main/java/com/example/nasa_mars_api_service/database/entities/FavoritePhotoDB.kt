package com.example.nasa_mars_api_service.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.database.converters.FavoritePhotoConverter

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 0:27
 * Project: NASA-Mars-API-Service
 */
@Entity(tableName = "favorite_photo_table")
@TypeConverters(value = [FavoritePhotoConverter::class])
data class FavoritePhotoDB(
    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = "type_of_photo")
    val typeOfPhoto: PhotoTypes = PhotoTypes.MARS_PHOTO,

    @ColumnInfo(name = "sol")
    val solDate: Int = 1000,

    @ColumnInfo(name = "earth_date")
    val earthDate: String = "2020-12-15",

    @ColumnInfo(name = "img_src")
    val imageSrc: String = "",

    @ColumnInfo(name = "camera_name")
    val cameraName: String = "FHAZ",

    @ColumnInfo(name = "rover_name")
    val roverName: String = "Curiosity",

    @ColumnInfo(name = "author")
    val author: String = "Matvey Popov",

    @ColumnInfo(name = "title")
    val title: String = "Title",

    @ColumnInfo(name = "description")
    val description: String = "Description",

)
