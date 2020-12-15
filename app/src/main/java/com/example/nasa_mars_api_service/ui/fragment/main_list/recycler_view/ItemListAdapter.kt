package com.example.nasa_mars_api_service.ui.fragment.main_list.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.ui.fragment.main_list.MainListViewModel

class ItemListAdapter(
    private val viewModel: MainListViewModel
): ListAdapter<MarsPhoto, ItemViewHolder>(ItemListCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.getViewHolder(parent, viewModel)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val photo = viewModel.listOfPhotos.value!![position]
        holder.bind(photo)
    }

}