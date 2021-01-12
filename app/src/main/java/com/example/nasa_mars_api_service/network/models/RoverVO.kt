package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoverVO(

    @Json(name = "id")
    val id: Int = 0,

    @Json(name = "name")
    val name: String = "Curiosity",

    @Json(name = "landing_date")
    val landingDate: String = "11.02.2002",

    @Json(name = "launch_date")
    val launchDate: String = "11.02.2002",

    @Json(name = "status")
    val status: String = "Active"

): Parcelable
