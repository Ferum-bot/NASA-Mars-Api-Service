package com.example.nasa_mars_api_service.ui.recycler_views.models

import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 22:57
 * Project: NASA-Mars-API-Service
 */
data class PictureOfDayItem(
        val title: String = "Picture of Day:",
        val picture: PictureOfDayPhoto = PictureOfDayPhoto()
): ListItem {
    override val idOfItem: Int
        get() = title.hashCode()
}