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
        val numberOfAvailablePhotos = viewModel.listOfPhotos.value?.size ?: 0
        if (numberOfAvailablePhotos <= position) {
            holder.bind(MarsPhoto())
            return
        }

        val photo = viewModel.listOfPhotos.value!![position]
        holder.bind(photo)

        if (numberOfAvailablePhotos - position == 5) {
            val numberOfAvailablePages = viewModel.numberOfAvailablePages
            viewModel.getPhotosFromNetwork(numberOfAvailablePages + 1)
        }
    }

}