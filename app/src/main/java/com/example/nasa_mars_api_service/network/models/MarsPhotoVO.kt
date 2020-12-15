package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarsPhotoVO(

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "sol")
    val solDate: Int = 1000,

    @Json(name = "camera")
    val cameraVO: CameraVO = CameraVO(),

    @Json(name = "img_src")
    val imageSrc: String = "",

    @Json(name = "earth_date")
    val earthDate: String = "2020-12-15",

    @Json(name = "rover")
    val rover: RoverVO

): Parcelable
