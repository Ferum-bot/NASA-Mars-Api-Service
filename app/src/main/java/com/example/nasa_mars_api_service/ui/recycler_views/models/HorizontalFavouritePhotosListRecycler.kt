package com.example.nasa_mars_api_service.ui.recycler_views.models

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:02
 * Project: NASA-Mars-API-Service
 */
data class HorizontalFavouritePhotosListRecycler(
        val title: String = "Favourites Photos: ",
        val listOfItems: List<ListItem> = listOf()
): ListItem {
    override val idOfItem: Int
        get() = title.hashCode() - 2
}
