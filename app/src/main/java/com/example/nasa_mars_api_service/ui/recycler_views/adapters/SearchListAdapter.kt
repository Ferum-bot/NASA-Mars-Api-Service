package com.example.nasa_mars_api_service.ui.recycler_views.adapters

import com.example.nasa_mars_api_service.ui.recycler_views.call_backs.BaseDiffCallBack
import com.example.nasa_mars_api_service.ui.recycler_views.delegates.SearchListDelegates
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by Matvey Popov.
 * Date: 03.01.2021
 * Time: 23:49
 * Project: NASA-Mars-API-Service
 */
class SearchListAdapter: AsyncListDifferDelegationAdapter<ListItem>(BaseDiffCallBack) {

    init {
        delegatesManager
                .addDelegate(SearchListDelegates.marsPhotoListDelegate())
    }

}