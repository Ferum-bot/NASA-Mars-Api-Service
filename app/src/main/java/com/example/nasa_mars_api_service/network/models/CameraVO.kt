package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraVO(

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "name")
    val name: String = "FHAZ",

    @Json(name = "full_name")
    val fullName: String = "Front Hazard Avoidance Camera"

): Parcelable