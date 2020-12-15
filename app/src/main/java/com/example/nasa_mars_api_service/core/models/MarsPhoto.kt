package com.example.nasa_mars_api_service.core.models

data class MarsPhoto(
    val id: Int = 0,

    val solDate: Int = 1000,

    val earthDate: String = "2020-12-15",

    val imageSrc: String = "",

    val cameraName: String = "FHAZ",

    val roverName: String = "Curiosity"
)
