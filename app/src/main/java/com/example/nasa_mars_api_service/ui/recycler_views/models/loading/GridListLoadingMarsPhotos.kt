package com.example.nasa_mars_api_service.ui.recycler_views.models.loading

import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 19:52
 * Project: NASA-Mars-API-Service
 */
data class GridListLoadingMarsPhotos(
        val title: String = "Title Loading",

        val listOfPhotos: List<ListItem> = listOf()
): ListItem {
    override val idOfItem: Int
        get() = title.hashCode() * 2
}
