package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.MainListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 23:14
 * Project: NASA-Mars-API-Service
 */
class MainListAdapter: AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {

    init {
        delegatesManager
            .addDelegate(MainListDelegates.pictureOfDayDelegate())
            .addDelegate(MainListDelegates.favouritePhotosHorizontalListDelegate())
            .addDelegate(MainListDelegates.gridMarsPhotosListDelegate())
    }

}