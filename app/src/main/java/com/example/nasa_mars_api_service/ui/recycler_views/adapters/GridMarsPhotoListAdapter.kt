package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:53
 * Project: NASA-Mars-API-Service
 */
class GridMarsPhotoListAdapter(
        marsPhotoItemClickListener: (MarsPhoto) -> Unit,
        marsPhotoItemLongClickListener: (MarsPhoto) -> Unit,
        marsPhotoItemAddToFavouritesClickListener: (MarsPhoto) -> Boolean
): AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {
    init {
        delegatesManager
            .addDelegate(MainListDelegates.gridMarsPhotosListItemDelegate(marsPhotoItemClickListener, marsPhotoItemLongClickListener, marsPhotoItemAddToFavouritesClickListener))
    }
}