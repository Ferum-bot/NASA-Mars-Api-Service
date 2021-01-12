package com.example.nasa_mars_api_service.core

import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.entities.FavoritePhotoDB
import com.example.nasa_mars_api_service.database.entities.MarsPhotoDB
import com.example.nasa_mars_api_service.database.entities.PictureOfDayPhotoDB
import com.example.nasa_mars_api_service.network.models.MarsPhotoVO
import com.example.nasa_mars_api_service.network.models.PictureOfDayVO

fun MarsPhotoVO.toMarsPhoto(): MarsPhoto {
    return MarsPhoto(
        id,
        solDate,
        earthDate.toDefaultDateFormat(),
        imageSrc,
        cameraVO.name,
        rover.name,
        false,
        rover.landingDate,
        rover.launchDate,
        rover.status
    )
}

fun MarsPhotoVO.toMarsPhotoDB(): MarsPhotoDB {
    return MarsPhotoDB(
        id,
        solDate,
        earthDate.toDefaultDateFormat(),
        imageSrc,
        cameraVO.name,
        rover.name,
        rover.landingDate,
        rover.launchDate,
        rover.status
    )
}

fun MarsPhotoDB.toMarsPhoto(): MarsPhoto {
    return MarsPhoto(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraName,
        roverName,
        false,
        landingDate,
        launchDate,
        status
    )
}

fun MarsPhoto.toMarsPhotoDB(): MarsPhotoDB {
    return MarsPhotoDB(
        id,
        solDate,
        earthDate,
        imageSrc,
        cameraName,
        roverName,
        landingDate,
        launchDate,
        status
    )
}

fun PictureOfDayVO.toPictureOfDayPhotoDB(): PictureOfDayPhotoDB {
    return PictureOfDayPhotoDB(
            description.hashCode(),
            author,
            title,
            description,
            date.toDefaultDateFormat(),
            false,
            imageSrc
    )
}

fun PictureOfDayPhotoDB.toPictureOfDayPhoto(): PictureOfDayPhoto {
    return PictureOfDayPhoto(
            id,
            author,
            title,
            description,
            date,
            isFavourite,
            imageSrc
    )
}

fun PictureOfDayVO.toPictureOfDayPhoto(): PictureOfDayPhoto {
    return this.toPictureOfDayPhotoDB().toPictureOfDayPhoto()
}

fun FavoritePhotoDB.toFavouritePhoto(): FavouritePhoto {
    return when(this.typeOfPhoto) {
        PhotoTypes.PICTURE_OF_DAY -> {
            FavouritePhoto(
                    id,
                    typeOfPhoto,
                    PictureOfDayPhoto(
                            id,
                            author,
                            title,
                            description,
                            this.earthDate,
                            false,
                            imageSrc
                    )
            )
        }
        PhotoTypes.MARS_PHOTO -> {
            FavouritePhoto(
                    id,
                    typeOfPhoto,
                    MarsPhoto(
                            id,
                            solDate,
                            earthDate,
                            imageSrc,
                            cameraName,
                            roverName
                    )
            )
        }
    }
}

fun MarsPhoto.toFavouritePhoto(): FavouritePhoto {
    return FavouritePhoto(
            id,
            PhotoTypes.MARS_PHOTO,
            photo = this
    )
}

fun FavouritePhoto.toFavouritePhotoDB(): FavoritePhotoDB {
    return when(this.typeOfPhoto) {
        PhotoTypes.MARS_PHOTO -> {
            val marsPhoto = this.photo as MarsPhoto
            FavoritePhotoDB(
                    id,
                    typeOfPhoto,
                    marsPhoto.solDate,
                    marsPhoto.earthDate,
                    marsPhoto.imageSrc,
                    marsPhoto.cameraName,
                    marsPhoto.roverName
            )
        }
        PhotoTypes.PICTURE_OF_DAY -> {
            val pictureOfDay = this.photo as PictureOfDayPhoto
            FavoritePhotoDB(
                    id,
                    typeOfPhoto,
                    author = pictureOfDay.author,
                    title = pictureOfDay.title,
                    description = pictureOfDay.description,
                    imageSrc = pictureOfDay.imageSrc,
                    earthDate = pictureOfDay.date
            )
        }
    }
}

fun MarsPhoto.toFavouritePhotoDB(): FavoritePhotoDB {
    return this.toFavouritePhoto().toFavouritePhotoDB()
}

fun PictureOfDayPhoto.toFavouritePhoto(): FavouritePhoto {
    return FavouritePhoto(
            id,
            PhotoTypes.PICTURE_OF_DAY,
            photo = this
    )
}

fun PictureOfDayPhoto.toFavouritePhotoDB(): FavoritePhotoDB {
    return this.toFavouritePhoto().toFavouritePhotoDB()
}

fun PictureOfDayPhoto.toPictureOfDayPhotoDB(): PictureOfDayPhotoDB {
    return PictureOfDayPhotoDB(
            id,
            author,
            title,
            description,
            date,
            isFavourite,
            imageSrc
    )
}

fun MarsPhoto.isNotFavourite(): MarsPhoto {
    return MarsPhoto(
            id,
            solDate,
            earthDate,
            imageSrc,
            cameraName,
            roverName,
            false,
            landingDate,
            launchDate,
            status
    )
}

fun MarsPhoto.isFavourite(): MarsPhoto {
    return MarsPhoto(
            id,
            solDate,
            earthDate,
            imageSrc,
            cameraName,
            roverName,
            true,
            landingDate,
            launchDate,
            status
    )
}

fun PictureOfDayPhoto.isNotFavourite(): PictureOfDayPhoto {
    return PictureOfDayPhoto(
            id,
            author,
            title,
            description,
            date,
            false,
            imageSrc
    )
}

fun PictureOfDayPhoto.isFavourite(): PictureOfDayPhoto {
    return PictureOfDayPhoto(
            id,
            author,
            title,
            description,
            date,
            true,
            imageSrc
    )
}