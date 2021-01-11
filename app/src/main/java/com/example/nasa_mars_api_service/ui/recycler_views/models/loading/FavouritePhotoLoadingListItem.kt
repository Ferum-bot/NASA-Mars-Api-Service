package com.example.nasa_mars_api_service.ui.recycler_views.models.loading

import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 19:56
 * Project: NASA-Mars-API-Service
 */
data class FavouritePhotoLoadingListItem(
        val id: Int = 0,

        val typeOfPhoto: PhotoTypes = PhotoTypes.MARS_PHOTO,
): ListItem {
    override val idOfItem: Int
        get() = -30
}
