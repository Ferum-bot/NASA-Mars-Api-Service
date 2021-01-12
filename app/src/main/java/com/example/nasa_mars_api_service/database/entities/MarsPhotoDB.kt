package com.example.nasa_mars_api_service.database.entities

import androidx.annotation.ColorLong
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mars_photo_table")
data class MarsPhotoDB(
    @PrimaryKey
    val id: Int = 0,

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

    @ColumnInfo(name = "landing_date")
    val landingDate: String = "11.02.2002",

    @ColumnInfo(name = "launch_date")
    val launchDate: String = "11.02.2002",

    @ColumnInfo(name = "status")
    val status: String = "Active"

)
