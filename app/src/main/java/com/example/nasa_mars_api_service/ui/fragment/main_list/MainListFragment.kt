package com.example.nasa_mars_api_service.ui.fragment.main_list

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.Variables
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentMainListBinding
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.AppPreferences
import com.example.nasa_mars_api_service.repository.MainRepository
import com.example.nasa_mars_api_service.ui.fragment.main_list.recycler_view.ItemListAdapter

class MainListFragment: Fragment() {

    private lateinit var viewModel: MainListViewModel

    private lateinit var binding: FragmentMainListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        binding.lifecycleOwner = this

        val appContext = requireContext().applicationContext
        val database = MainDatabase.getInstance(appContext)
        val preferences = AppPreferences.getInstance(appContext)

        val repository = MainRepository(database.marsPhotoDao, database.deletedMarsPhotoDao,
            MarsPhotosApi.marsPhotosService, preferences)

        val factory = MainListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainListViewModel::class.java)

        val adapter = ItemListAdapter(viewModel)

        adapter.submitList(
            listOf(
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto(),
                MarsPhoto()
            )
        )

        binding.mainRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}