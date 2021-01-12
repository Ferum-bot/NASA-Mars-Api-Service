package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.SearchListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.loading.SearchListLoadingDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 03.01.2021
 * Time: 23:49
 * Project: NASA-Mars-API-Service
 */
class SearchListAdapter(
    // Mars photo item click listeners
    marsPhotoItemClickListener: (MarsPhoto) -> Unit,
    marsPhotoImageLongClickListener: (MarsPhoto) -> Unit,

    // Add to favourites click listener
    marsPhotoAddToFavouritesClickListener: (MarsPhoto) -> Unit,

    // Load more button click listeners and lifeCycles
    loadMoreMarsPhotosClickListener: () -> Unit,
    loadingStatus: LiveData<MarsApiStatus>,
    lifecycleOwnerForStatusBar: LifecycleOwner
): AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {

    init {
        delegatesManager
                .addDelegate(SearchListDelegates.marsPhotoListDelegate(marsPhotoItemClickListener, marsPhotoImageLongClickListener, marsPhotoAddToFavouritesClickListener))
                .addDelegate(MainListDelegates.loadMoreMarsPhotosDelegate(loadMoreMarsPhotosClickListener, loadingStatus, lifecycleOwnerForStatusBar))
                .addDelegate(SearchListLoadingDelegates.marsPhotoListLoadingDelegate())
    }

}