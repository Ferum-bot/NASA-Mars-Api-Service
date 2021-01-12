package com.example.nasa_mars_api_service.ui.recycler_views.adapters.loading

import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.loading.MainListLoadingDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 20:54
 * Project: NASA-Mars-API-Service
 */
class FavouritePhotosHorizontalLoadingListAdapter: AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {
    init {
        delegatesManager
                .addDelegate(MainListLoadingDelegates.favouritePhotosHorizontalListItemLoadingDelegate())
    }
}