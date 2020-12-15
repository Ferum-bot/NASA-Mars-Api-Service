package com.example.nasa_mars_api_service.ui.fragment.main_list.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.example.nasa_mars_api_service.core.models.MarsPhoto

class ItemListCallBack: DiffUtil.ItemCallback<MarsPhoto>() {
    override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
        return oldItem == newItem
    }

}