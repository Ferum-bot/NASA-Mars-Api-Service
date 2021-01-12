package com.example.nasa_mars_api_service.ui.recycler_views.models.loading

import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 12.01.2021
 * Time: 15:19
 * Project: NASA-Mars-API-Service
 */
data class MarsPhotoSearchListLoadingItem(
        val id: Int = 123
): ListItem {
    override val idOfItem: Int
        get() = id
}

