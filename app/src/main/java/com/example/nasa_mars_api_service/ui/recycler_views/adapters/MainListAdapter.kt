package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.loading.MainListLoadingDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:14
 * Project: NASA-Mars-API-Service
 */
class MainListAdapter(
        // Item click listeners
        favouritePhotoItemClickListener: (FavouritePhoto) -> Unit,
        pictureOfDayPhotoItemClickListener: (PictureOfDayPhoto) -> Unit,
        marsPhotoItemClickListener: (MarsPhoto) -> Unit,

        // Item long click listeners
        favouritePhotoItemLongClickListener: (FavouritePhoto) -> Unit,
        pictureOfDayPhotoItemLongClickListener: (PictureOfDayPhoto) -> Unit,
        marsPhotoItemLongClickListener: (MarsPhoto) -> Unit,

        // Add to favourite click listeners
        pictureOfDayPhotoItemAddToFavouritesClickListener: (PictureOfDayPhoto) -> Boolean,
        marsPhotoItemAddToFavouritesClickListener: (MarsPhoto) -> Boolean,

        // Delete from favourites click listener
        favouritePhotoItemDeleteFromFavouritesClickListener: (FavouritePhoto) -> Boolean,

        // Load more button click listener
        loadMoreMarsPhotosButtonClickListener: () -> Unit,

        // Live Data for getting result
        downloadingStatus: LiveData<MarsApiStatus>,

        // Lifecycle owner owner for live data
        lifecycleOwner: LifecycleOwner
): AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {

    init {
        delegatesManager
            .addDelegate(MainListDelegates.pictureOfDayDelegate(pictureOfDayPhotoItemClickListener, pictureOfDayPhotoItemLongClickListener, pictureOfDayPhotoItemAddToFavouritesClickListener))
            .addDelegate(MainListDelegates.favouritePhotosHorizontalListDelegate(favouritePhotoItemClickListener, favouritePhotoItemLongClickListener, favouritePhotoItemDeleteFromFavouritesClickListener))
            .addDelegate(MainListDelegates.gridMarsPhotosListDelegate(marsPhotoItemClickListener, marsPhotoItemLongClickListener, marsPhotoItemAddToFavouritesClickListener))
            .addDelegate(MainListDelegates.loadMoreMarsPhotosDelegate(loadMoreMarsPhotosButtonClickListener, downloadingStatus, lifecycleOwner))
            .addDelegate(MainListLoadingDelegates.pictureOfDayLoadingDelegate())
            .addDelegate(MainListLoadingDelegates.favouritePhotosHorizontalListLoadingDelegate())
            .addDelegate(MainListLoadingDelegates.gridMarsPhotosListLoadingDelegate())
    }

}