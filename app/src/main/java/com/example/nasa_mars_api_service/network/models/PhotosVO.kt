package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotosVO(
        @Json(name = "photos")
        val listPhotos: List<MarsPhotoVO>

):Parcelable