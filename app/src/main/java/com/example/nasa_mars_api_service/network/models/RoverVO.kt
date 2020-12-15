package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoverVO(

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "name")
    val name: String = "Curiosity"

): Parcelable
