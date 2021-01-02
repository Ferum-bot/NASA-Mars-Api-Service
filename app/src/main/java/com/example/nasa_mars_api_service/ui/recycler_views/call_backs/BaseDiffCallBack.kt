package com.example.nasa_mars_api_service.ui.recycler_views.call_backs

import androidx.recyclerview.widget.DiffUtil
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:17
 * Project: NASA-Mars-API-Service
 */
object BaseDiffCallBack: DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.idOfItem == newItem.idOfItem
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.equals(newItem)
    }
}