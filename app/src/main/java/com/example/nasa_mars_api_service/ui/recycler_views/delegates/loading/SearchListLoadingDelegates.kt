package com.example.nasa_mars_api_service.ui.recycler_views.delegates.loading

import com.example.nasa_mars_api_service.databinding.FragmentSearchListItemLoadingBinding
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.example.nasa_mars_api_service.ui.recycler_views.models.loading.MarsPhotoSearchListLoadingItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Created by Matvey Popov.
 * Date: 12.01.2021
 * Time: 15:10
 * Project: NASA-Mars-API-Service
 */
object SearchListLoadingDelegates {

    fun marsPhotoListLoadingDelegate() =
        adapterDelegateViewBinding<MarsPhotoSearchListLoadingItem, ListItem, FragmentSearchListItemLoadingBinding>(
            { inflater, container -> FragmentSearchListItemLoadingBinding.inflate(inflater, container, false) }
        ) {

        }

}