package com.example.nasa_mars_api_service.core.models

import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

data class MarsPhoto(
    val id: Int = 0,

    val solDate: Int = 1000,

    val earthDate: String = "2020-12-15",

    val imageSrc: String = "",

    val cameraName: String = "FHAZ",

    val roverName: String = "Curiosity",

    val isFavourite: Boolean = false,

    val landingDate: String = "11.02.2002",

    val launchDate: String = "11.02.2002",

    val status: String = "Active"

): ListItem {

    override val idOfItem: Int
        get() = id
}
