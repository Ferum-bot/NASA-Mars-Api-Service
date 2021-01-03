package com.example.nasa_mars_api_service.ui.fragment.search_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.databinding.FragmentSearchListBinding
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.SearchListAdapter

/**
 * Created by Matvey Popov.
 * Date: 31.12.2020
 * Time: 20:31
 * Project: NASA-Mars-API-Service
 */
class SearchListFragment: Fragment() {

    private lateinit var viewModel: SearchListViewModel

    private lateinit var binding: FragmentSearchListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val factory = SearchListViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(SearchListViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchListAdapter()
        adapter.items = listOf(
                MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(), MarsPhoto(),
        )
        binding.searchRecyclerView.adapter = adapter
    }
}