package com.example.nasa_mars_api_service.core.models

import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:26
 * Project: NASA-Mars-API-Service
 */
data class FavouritePhoto(
    val id: Int = 0,

    val typeOfPhoto: PhotoTypes = PhotoTypes.MARS_PHOTO,

    val photo: ListItem
): ListItem {
    override val idOfItem: Int
        get() = id
}
