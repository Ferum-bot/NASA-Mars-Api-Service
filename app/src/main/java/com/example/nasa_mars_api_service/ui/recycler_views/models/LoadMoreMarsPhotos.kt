package com.example.nasa_mars_api_service.ui.recycler_views.models

/**
 * Created by Matvey Popov.
 * Date: 11.01.2021
 * Time: 15:31
 * Project: NASA-Mars-API-Service
 */
data class LoadMoreMarsPhotos(
        val id: Int = -19
): ListItem {
    override val idOfItem: Int
        get() = id
}
