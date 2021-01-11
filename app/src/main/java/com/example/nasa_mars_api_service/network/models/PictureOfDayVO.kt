package com.example.nasa_mars_api_service.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 14:36
 * Project: NASA-Mars-API-Service
 */
@Parcelize
data class PictureOfDayVO(
        @Json(name = "copyright")
        val author: String = "Matvey Popov",

        @Json(name = "date")
        val date: String = "11.02.2002",

        @Json(name = "explanation")
        val description: String = "Description",

        @Json(name = "media_type")
        val mediaType: String = "image",

        @Json(name = "title")
        val title: String = " Title",

        @Json(name = "url")
        val imageSrc: String = ""

): Parcelable
