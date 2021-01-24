package com.example.nasa_mars_api_service.ui.fragment.main_list

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
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentMainListBinding
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.MainListAdapter

class MainListFragment: Fragment() {

    private lateinit var viewModel: MainListViewModel

    private lateinit var binding: FragmentMainListBinding

    private lateinit var mainListAdapter: MainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        binding.lifecycleOwner = this

        getViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllClickListeners()
        setAllObservers()
        setAdapters()
        requirePhotosOrIssue()
    }

    private fun getViewModel() {
        val context = requireContext().applicationContext
        val database = MainDatabase.getInstance(context)
        val preferences: BaseApplicationPreferences = AppPreferences.getInstance(context)
        val remoteSource = MarsPhotosApi.marsPhotosService

        val repository: BaseRepository = MainRepository.getInstance(database.marsPhotoDao, database.favoritePhotoDao, database.pictureOfDayDao, remoteSource, preferences)
        val factory = MainListViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(MainListViewModel::class.java)
    }

    private fun setAllClickListeners() {

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.title) {
                getString(R.string.refresh) -> {
                    menuItem.setIcon(R.drawable.refreshing_animation)
                    if (!Variables.isNetworkConnectionAvailable) {
                        showErrorImage()
                        showErrorMessage(getString(R.string.no_internet_connection))
                        menuItem.setIcon(R.drawable.ic_refresh)
                        return@setOnMenuItemClickListener true
                    }
                    viewModel.refreshAllPhotos()
                    true
                }
                getString(R.string.search) -> {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToSearchMarsPhotoFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setAdapters() {
        mainListAdapter = MainListAdapter(

                // Click Listeners for each type
                fun (favouritePhotoItem: FavouritePhoto) {
                    when(favouritePhotoItem.typeOfPhoto) {
                        PhotoTypes.PICTURE_OF_DAY -> {
                            val photo = favouritePhotoItem.photo as PictureOfDayPhoto
                            findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToImageOfDayFragment(photo.id))
                        }
                        PhotoTypes.MARS_PHOTO -> {
                            val photo = favouritePhotoItem.photo as MarsPhoto
                            findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToMarsPhotoDescriptionFragment(photo.id))
                        }
                    }
                },

                fun (pictureOfDayPhotoItem: PictureOfDayPhoto) {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToImageOfDayFragment(pictureOfDayPhotoItem.id))
                },

                fun (marsPhotoItem: MarsPhoto) {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToMarsPhotoDescriptionFragment(marsPhotoItem.id))
                },

                // Long Click Listeners for each type
                fun (favouritePhotoItem: FavouritePhoto) {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToPhotoViewFragment(favouritePhotoItem.id, favouritePhotoItem.typeOfPhoto))
                },

                fun (pictureOfDayPhotoItem: PictureOfDayPhoto) {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToPhotoViewFragment(pictureOfDayPhotoItem.id, PhotoTypes.PICTURE_OF_DAY))
                },

                fun (marsPhotoItem: MarsPhoto) {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToPhotoViewFragment(marsPhotoItem.id, PhotoTypes.MARS_PHOTO))
                },

                // Add to favourites click listeners
                fun (pictureOfDayItemAddToFavourites: PictureOfDayPhoto): Boolean {
                    if (pictureOfDayItemAddToFavourites.isFavourite) {
                        viewModel.deletePhotoFromFavourites(pictureOfDayItemAddToFavourites)
                    }
                    else {
                        viewModel.addPhotoToFavourites(pictureOfDayItemAddToFavourites)
                    }
                   return true
                },

                fun (marsPhotoItemAddToFavourites: MarsPhoto): Boolean {
                    if (marsPhotoItemAddToFavourites.isFavourite) {
                        viewModel.deletePhotoFromFavourites(marsPhotoItemAddToFavourites)
                    }
                    else {
                        viewModel.addPhotoToFavourites(marsPhotoItemAddToFavourites)
                    }
                    return true
                },

                // Delete photo from favourites
                fun (favouritePhotoItem: FavouritePhoto): Boolean {
                    viewModel.deletePhotoFromFavourites(favouritePhotoItem)
                    return true
                },

                // On click listener for load more button
                fun() {
                    viewModel.downloadNewMarsPhotos()
                },

                // Live data to observe status in load more button
                viewModel.statusNewMarsPhotos,

                // Lifecycle owner for live data
                this.viewLifecycleOwner
        )

        binding.mainRecyclerView.adapter = mainListAdapter
    }

    private fun setAllObservers() {
        viewModel.resultListForMainListAdapter.observe(viewLifecycleOwner, Observer { newList ->
            mainListAdapter.items = newList
        })

        viewModel.messageFavouritesPhotos.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                if (!Variables.isNetworkConnectionAvailable) {
                    showErrorMessage("Can't get Favourites photos: " + getString(R.string.no_internet_connection))
                }
                else {
                    showErrorMessage("Can't get Favourites photos: $message")
                }
                viewModel.messageFavouritesPhotosWasShown()
            }
        })

        viewModel.messageNewMarsPhotos.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                if (!Variables.isNetworkConnectionAvailable) {
                    showErrorMessage("Can't get New Mars photos: " + getString(R.string.no_internet_connection))
                }
                else {
                    showErrorMessage("Can't get New Mars photos: $message")
                }
                viewModel.messageNewMarsPhotosWasShown()
            }
        })

        viewModel.messagePictureOfDay.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                if (!Variables.isNetworkConnectionAvailable) {
                    showErrorMessage("Can't get Picture of day: " + getString(R.string.no_internet_connection))
                }
                else {
                    showErrorMessage("Can't get Picture of day: $message")
                }
                viewModel.messagePictureOfDayWasShown()
            }
        })

        viewModel.messageDelete.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                showErrorMessage("Can't delete photo: $message")
                viewModel.messageDeleteWasShown()
            }
        }

        viewModel.statusPictureOfDay.observe(viewLifecycleOwner, Observer { newStatus->
            when(newStatus) {
                MarsApiStatus.NOT_ACTIVE -> {
                    binding.appBar.menu.getItem(1).setIcon(R.drawable.ic_refresh)
                }
                MarsApiStatus.ERROR -> {
                    binding.appBar.menu.getItem(1).setIcon(R.drawable.ic_refresh)
                }
                MarsApiStatus.DONE -> {
                    binding.appBar.menu.getItem(1).setIcon(R.drawable.ic_refresh)
                }
                MarsApiStatus.LOADING -> {
                    binding.appBar.menu.getItem(1).setIcon(R.drawable.refreshing_animation)
                }
            }
        })
    }

    private fun requirePhotosOrIssue() {
        if (!viewModel.isFirstLaunching) {
            return
        }
        viewModel.isFirstLaunching = false
        if (!Variables.isNetworkConnectionAvailable && viewModel.nothingIsAvailable()) {
            showErrorImage()
            showErrorMessage(getString(R.string.no_internet_connection))
            return
        }
        viewModel.getResultList()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorImage() {
        binding.mainRecyclerView.visibility = View.GONE
        binding.statusImage.visibility = View.VISIBLE
        binding.statusImage.setImageResource(R.drawable.connection_error_image)
    }

}