package com.example.nasa_mars_api_service.ui.recycler_views.models.loading

import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 19:32
 * Project: NASA-Mars-API-Service
 */
data class PictureOfDayLoadingItem(
        val title: String =  "Picture Of Day:"
): ListItem {

    override val idOfItem: Int
        get() = 2 * title.hashCode()
}
