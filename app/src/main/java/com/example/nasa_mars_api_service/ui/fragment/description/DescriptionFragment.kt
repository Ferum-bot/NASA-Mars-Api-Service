package com.example.nasa_mars_api_service.ui.fragment.description

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentDescriptionBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.AppPreferences
import com.example.nasa_mars_api_service.repository.MainRepository

class DescriptionFragment: Fragment() {

    private lateinit var viewModel: DescriptionViewModel
    private lateinit var binding: FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_description, container, false)

        val args = DescriptionFragmentArgs.fromBundle(requireArguments())

        val appContext = requireContext().applicationContext
        val database = MainDatabase.getInstance(appContext)
        val preferences = AppPreferences.getInstance(appContext)

        val repository = MainRepository(database.marsPhotoDao, database.deletedMarsPhotoDao,
            MarsPhotosApi.marsPhotosService, preferences)

        val factory = DescriptionViewModelFactory(args.idPhoto, repository)
        viewModel = ViewModelProvider(this, factory).get(DescriptionViewModel::class.java)

        viewModel.getImageUri()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoView = binding.photoView

        viewModel.imageUri.observe(viewLifecycleOwner, Observer { uri ->
            if (uri != null && !viewModel.imageWasShown) {
                showImage(uri)
                viewModel.imageWasShown = true
            }
        })
    }

    private fun showImage(uri: Uri) {
        val options = RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.connection_error_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        val photoView = binding.photoView
        Glide.with(photoView.context)
            .applyDefaultRequestOptions(options)
            .load(uri)
            .into(photoView)
    }
}