package com.example.nasa_mars_api_service.core

import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.database.entities.MarsPhotoDB
import com.example.nasa_mars_api_service.network.models.MarsPhotoVO

fun MarsPhotoVO.toMarsPhoto(): MarsPhoto {
    return MarsPhoto(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraVO.name,
        rover.name
    )
}

fun MarsPhotoVO.toMarsPhotoDB(): MarsPhotoDB {
    return MarsPhotoDB(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraVO.name,
        rover.name
    )
}

fun MarsPhotoDB.toMarsPhoto(): MarsPhoto {
    return MarsPhoto(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraName,
        roverName
    )
}

fun MarsPhoto.toMarsPhotoDB(): MarsPhotoDB {
    return MarsPhotoDB(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraName,
        roverName
    )
}

fun MarsPhoto.toDeletedMarsPhotoDB(): DeletedMarsPhotoDB {
    return DeletedMarsPhotoDB(
            id
    )
}

fun MarsPhotoDB.toDeletedMarsPhotoDB(): DeletedMarsPhotoDB {
    return DeletedMarsPhotoDB(
        id
    )
}