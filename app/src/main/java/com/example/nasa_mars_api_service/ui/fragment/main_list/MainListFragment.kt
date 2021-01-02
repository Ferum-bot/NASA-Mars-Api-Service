package com.example.nasa_mars_api_service.ui.fragment.main_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentMainListBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.MainListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.models.GridListMarsPhotos
import com.example.nasa_mars_api_service.ui.recycler_views.models.HorizontalFavouritePhotosListRecycler
import com.example.nasa_mars_api_service.ui.recycler_views.models.PictureOfDayItem

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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainListAdapter().apply {
            items = listOf(
                PictureOfDayItem(),
                HorizontalFavouritePhotosListRecycler(listOfItems = listOf(
                    FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()),
                    FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()), FavouritePhoto(photo = MarsPhoto()),
                )),
                GridListMarsPhotos(listOfPhotos = listOf(
                    MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(), MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),
                    MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(), MarsPhoto(), MarsPhoto(),  MarsPhoto(),  MarsPhoto(),
                    MarsPhoto(),  MarsPhoto(), MarsPhoto(), MarsPhoto(),  MarsPhoto(),  MarsPhoto(), MarsPhoto(), MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),  MarsPhoto(),
                ))
            )
        }
        binding.mainRecyclerView.adapter = adapter

    }

}