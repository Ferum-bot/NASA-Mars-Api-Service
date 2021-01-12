package com.example.nasa_mars_api_service.ui.fragment.photo_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.getBaseRequestOptions
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentLoadMoreItemsBinding
import com.example.nasa_mars_api_service.databinding.FragmentPhotoShowBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

/**
 * Created by Matvey Popov.
 * Date: 31.12.2020
 * Time: 20:27
 * Project: NASA-Mars-API-Service
 */
class PhotoViewFragment: Fragment() {
    private lateinit var binding: FragmentPhotoShowBinding

    private lateinit var viewModel: PhotoViewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_show, container, false)
        getViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllObservers()
        startLoadingImage()
    }

    private fun getViewModel() {
        val context = requireContext()
        val preferences: BaseApplicationPreferences = AppPreferences.getInstance(context)
        val database = MainDatabase.getInstance(context)
        val remoteSource = MarsPhotosApi.marsPhotosService
        val repository: BaseRepository = MainRepository.getInstance(
                database.marsPhotoDao,
                database.favoritePhotoDao,
                database.pictureOfDayDao,
                remoteSource,
                preferences
        )

        val factory = PhotoViewViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PhotoViewViewModel::class.java)
    }

    private fun setAllObservers() {
        viewModel.imageSrc.observe(viewLifecycleOwner, Observer { newImageSrc ->
            if (newImageSrc != null) {
                val options = getBaseRequestOptions()
                Glide.with(binding.photoView)
                        .applyDefaultRequestOptions(options)
                        .load(newImageSrc)
                        .transition(withCrossFade())
                        .into(binding.photoView)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { newMessage ->
            if (newMessage != null) {
                showErrorMessage(newMessage)
            }
        })
    }

    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, "Something went wrong: $message", Toast.LENGTH_SHORT).show()
    }

    private fun startLoadingImage() {
        val args = PhotoViewFragmentArgs.fromBundle(requireArguments())
        val id = args.id
        val photoType = args.PhotoType
        viewModel.getPhoto(id, photoType)
    }

}