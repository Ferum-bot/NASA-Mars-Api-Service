package com.example.nasa_mars_api_service.ui.fragment.search_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.Variables
import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentSearchListBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
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

    private lateinit var searchListAdapter: SearchListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false)

        getViewModel()
        setBaseVisibilityForViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setAllClickListeners()
        setAllObservers()
        requireMarsPhotosOrIssue()
    }

    private fun getViewModel() {
        val context = requireContext()
        val preferences: BaseApplicationPreferences = AppPreferences.getInstance(context)
        val dataBase = MainDatabase.getInstance(context)
        val remoteSource = MarsPhotosApi.marsPhotosService
        val repository: BaseRepository = MainRepository.getInstance(
                dataBase.marsPhotoDao,
                dataBase.favoritePhotoDao,
                dataBase.pictureOfDayDao,
                remoteSource,
                preferences
        )

        val factory = SearchListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SearchListViewModel::class.java)
    }

    private fun requireMarsPhotosOrIssue() {
        if (!viewModel.isFirstLaunching) {
            return
        }
        viewModel.isFirstLaunching = false
        if (!Variables.isNetworkConnectionAvailable) {
            showErrorMessage(getString(R.string.no_internet_connection))
            showErrorImage()
            return
        }
        val args = SearchListFragmentArgs.fromBundle(requireArguments())
        val marsPhotoToSearch = args.MarsPhotoToSearch
        viewModel.searchMarsPhotos(marsPhotoToSearch)
    }

    private fun setUpAdapter() {
        searchListAdapter = SearchListAdapter(
            // Mars photo Item Click Listener
            fun (marsPhoto: MarsPhoto) {
                findNavController().navigate(SearchListFragmentDirections.actionSearchListFragmentToMarsPhotoDescriptionFragment(marsPhoto.id))
            },

            // Image long Click listener for mars photo item
            fun (marsPhoto: MarsPhoto) {
                findNavController().navigate(SearchListFragmentDirections.actionSearchListFragmentToPhotoViewFragment(marsPhoto.id, PhotoTypes.MARS_PHOTO))
            },

            // Add to favourites click Listener
            fun (marsPhoto: MarsPhoto) {
                when(marsPhoto.isFavourite) {
                    true -> viewModel.removePhotoFromFavourites(marsPhoto)
                    false -> viewModel.addPhotoToFavourites(marsPhoto)
                }
            },

            // Click listener for load more button
            fun () {
                val args = SearchListFragmentArgs.fromBundle(requireArguments())
                val photoType = args.MarsPhotoToSearch
                viewModel.downloadMoreMarsPhotos(photoType)
            },

            // Live data to observe status
            viewModel.statusForLoadingNewPhotos,

            // Lifecycle for Observer
            viewLifecycleOwner
        )

        binding.searchRecyclerView.adapter = searchListAdapter
    }

    private fun setAllObservers() {
        viewModel.resultListForRecyclerView.observe(viewLifecycleOwner, Observer { newList->
            if (newList.isNotEmpty()) {
                showRecyclerView()
                searchListAdapter.items = newList
            }
            else {
                showErrorImage()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { newMessage ->
            if (newMessage != null) {
                showErrorMessage("Something went wrong: $newMessage")
                viewModel.errorMessageWasShown()
            }
        })
    }

    private fun setAllClickListeners() {
        binding.appBar.setOnMenuItemClickListener {
            if (!Variables.isNetworkConnectionAvailable) {
                showErrorMessage(getString(R.string.no_internet_connection))
                return@setOnMenuItemClickListener false
            }
            val args = SearchListFragmentArgs.fromBundle(requireArguments())
            val photoToSearch = args.MarsPhotoToSearch
            viewModel.refreshSearch(photoToSearch)
            true
        }

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorImage() {
        binding.errorImage.setImageResource(R.drawable.connection_error_image)
        binding.errorImage.visibility = View.VISIBLE
        binding.searchRecyclerView.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.errorImage.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

    private fun setBaseVisibilityForViews() {
        binding.errorImage.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

}