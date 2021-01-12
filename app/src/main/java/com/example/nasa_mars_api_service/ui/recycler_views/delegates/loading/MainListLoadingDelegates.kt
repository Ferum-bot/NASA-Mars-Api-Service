package com.example.nasa_mars_api_service.ui.recycler_views.delegates.loading

import android.graphics.Color
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.databinding.*
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.loading.FavouritePhotosHorizontalLoadingListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.loading.GridMarsPhotoLoadingListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.example.nasa_mars_api_service.ui.recycler_views.models.loading.*
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 20:01
 * Project: NASA-Mars-API-Service
 */
object MainListLoadingDelegates {

    fun favouritePhotosHorizontalListLoadingDelegate() =
        adapterDelegateViewBinding<HorizontalFavouritePhotoListLoadingRecycler, ListItem, FragmentHorizontalListBinding>(
                { inflater, container -> FragmentHorizontalListBinding.inflate(inflater, container, false) }
        ) {

            // OnCreateViewHolder
            val adapter = FavouritePhotosHorizontalLoadingListAdapter()
            binding.horizontalRecyclerView.adapter = adapter
            binding.titleLabel.setBackgroundColor(Color.LTGRAY)

            // OnBindViewHolder
            bind {
                adapter.apply {
                    items = item.listOfItems
                }
            }
        }

    fun pictureOfDayLoadingDelegate() =
            adapterDelegateViewBinding<PictureOfDayLoadingItem, ListItem, FragmentImageOfDayLoadingBinding>(
                    { inflater, container -> FragmentImageOfDayLoadingBinding.inflate(inflater, container, false) }
            ) {
        }

    fun gridMarsPhotosListLoadingDelegate() =
            adapterDelegateViewBinding<GridListLoadingMarsPhotos, ListItem, FragmentGridMarsPhotoListBinding>(
                    { inflater, container -> FragmentGridMarsPhotoListBinding.inflate(inflater, container, false) }
            ) {

                val adapter = GridMarsPhotoLoadingListAdapter()
                binding.titleLabel.text = getString(R.string.new_mars_photos)
                binding.gridMarsPhotoRecyclerView.adapter = adapter

                bind {
                    adapter.apply {
                        items = item.listOfPhotos
                    }
                }
            }


    fun gridMarsPhotosListItemLoadingDelegate() =
            adapterDelegateViewBinding<MarsPhotoLoading, ListItem, FragmentGridMarsPhotoItemBinding>(
                    { inflater, container -> FragmentGridMarsPhotoItemBinding.inflate(inflater, container, false) }
            ) {
                binding.marsPhotoImageView.setBackgroundColor(Color.LTGRAY)
            }


    fun favouritePhotosHorizontalListItemLoadingDelegate() =
            adapterDelegateViewBinding<FavouritePhotoLoadingListItem, ListItem, FragmentHorizontalListItemLoadingBinding>(
                    { inflater, container -> FragmentHorizontalListItemLoadingBinding.inflate(inflater, container, false) }
            ) {
            }
}