package com.example.nasa_mars_api_service.ui.fragment.mars_photo_description

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.enums.PhotoTypes
import com.example.nasa_mars_api_service.core.getBaseRequestOptions
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentMarsPhotoDescriptionBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

/**
 * Created by Matvey Popov.
 * Date: 31.12.2020
 * Time: 20:25
 * Project: NASA-Mars-API-Service
 */
class MarsPhotoDescriptionFragment: Fragment() {
    private lateinit var binding: FragmentMarsPhotoDescriptionBinding

    private lateinit var viewModel: MarsPhotoDescriptionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mars_photo_description, container, false)
        getViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllClickListeners()
        setAllObservers()
        startLoadingMarsPhoto()
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

        val factory = MarsPhotoDescriptionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MarsPhotoDescriptionViewModel::class.java)
    }

    private fun setAllClickListeners() {
        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.appBar.setOnMenuItemClickListener {
            when(viewModel.marsPhoto.value?.isFavourite) {
                true -> {
                    viewModel.removeMarsPhotoFromFavourites()
                    binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star)
                    true
                }
                false -> {
                    viewModel.addMarsPhotoToFavourites()
                    binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star__filled)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.marsImage.setOnClickListener {
            val args = MarsPhotoDescriptionFragmentArgs.fromBundle(requireArguments())
            val id = args.id
            findNavController().navigate(
                    MarsPhotoDescriptionFragmentDirections.actionMarsPhotoDescriptionFragmentToPhotoViewFragment(id, PhotoTypes.MARS_PHOTO)
            )
        }
    }

    private fun setAllObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { newMessage ->
            if (newMessage != null) {
                showErrorMessage(newMessage)
            }
        })

        viewModel.marsPhoto.observe(viewLifecycleOwner, Observer { marsPhoto ->
            loadImage(marsPhoto)
            setPhotoDescription(marsPhoto)
        })
    }

    private fun loadImage(marsPhoto: MarsPhoto) {
        val options = getBaseRequestOptions()
        val src = marsPhoto.imageSrc
        Glide.with(binding.marsImage)
                .applyDefaultRequestOptions(options)
                .load(src)
                .transition(withCrossFade())
                .into(binding.marsImage)
    }

    private fun setPhotoDescription(marsPhoto: MarsPhoto) {
        binding.solTextView.text = marsPhoto.solDate.toString()
        binding.earthDateTextView.text = marsPhoto.earthDate
        binding.roverTextView.text = marsPhoto.roverName
        binding.cameraTextView.text = marsPhoto.cameraName
        binding.landingDateTextView.text = marsPhoto.landingDate
        binding.launchDateTextView.text = marsPhoto.launchDate
        binding.statusTextView.text = marsPhoto.status
        when(marsPhoto.isFavourite) {
            true -> binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star__filled)
            false -> binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star)
        }
    }

    private fun startLoadingMarsPhoto() {
        val args = MarsPhotoDescriptionFragmentArgs.fromBundle(requireArguments())
        val id = args.id
        viewModel.getMarsPhoto(id)
    }

    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}