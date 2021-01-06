package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListLoadingDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:14
 * Project: NASA-Mars-API-Service
 */
class MainListAdapter(
        favouritePhotoItemClickListener: (FavouritePhoto) -> Unit,
        pictureOfDayPhotoItemClickListener: (PictureOfDayPhoto) -> Unit,
        marsPhotoItemClickListener: (MarsPhoto) -> Unit,

        favouritePhotoItemLongClickListener: (FavouritePhoto) -> Unit,
        pictureOfDayPhotoItemLongClickListener: (PictureOfDayPhoto) -> Unit,
        marsPhotoItemLongClickListener: (MarsPhoto) -> Unit
): AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {

    init {
        delegatesManager
            .addDelegate(MainListDelegates.pictureOfDayDelegate(pictureOfDayPhotoItemClickListener, pictureOfDayPhotoItemLongClickListener))
            .addDelegate(MainListDelegates.favouritePhotosHorizontalListDelegate(favouritePhotoItemClickListener, favouritePhotoItemLongClickListener))
            .addDelegate(MainListDelegates.gridMarsPhotosListDelegate(marsPhotoItemClickListener, marsPhotoItemLongClickListener))
            .addDelegate(MainListLoadingDelegates.pictureOfDayLoadingDelegate())
            .addDelegate(MainListLoadingDelegates.favouritePhotosHorizontalListLoadingDelegate())
            .addDelegate(MainListLoadingDelegates.gridMarsPhotosListLoadingDelegate())
    }

}