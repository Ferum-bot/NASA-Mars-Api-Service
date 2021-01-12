package com.example.nasa_mars_api_service.ui.fragment.image_of_day_description

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
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentImageOfDayDescriptionBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

/**
 * Created by Matvey Popov.
 * Date: 31.12.2020
 * Time: 20:24
 * Project: NASA-Mars-API-Service
 */
class ImageOfDayFragment: Fragment() {
    private lateinit var binding: FragmentImageOfDayDescriptionBinding

    private lateinit var viewModel: ImageOfDayViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_of_day_description, container, false)

        getViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllClickListeners()
        setAllObservers()
        getImageOfDay()
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

        val factory = ImageOfDayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ImageOfDayViewModel::class.java)
    }

    private fun setAllClickListeners() {
        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageView.setOnClickListener {
            val args = ImageOfDayFragmentArgs.fromBundle(requireArguments())
            val id = args.id
            findNavController().navigate(ImageOfDayFragmentDirections.actionImageOfDayFragmentToPhotoViewFragment(id, PhotoTypes.PICTURE_OF_DAY))
        }

        binding.appBar.setOnMenuItemClickListener {
            when(viewModel.imageOfDay.value?.isFavourite) {
                true -> {
                    viewModel.removeImageOfDayFromFavourites()
                    true
                }
                false -> {
                    viewModel.addImageOfDayToFavourites()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setAllObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { newMessage ->
            if (newMessage != null) {
                showErrorMessage(newMessage)
            }
        })

        viewModel.imageOfDay.observe(viewLifecycleOwner, Observer { image ->
            startLoadingImage(image)
            setAllFields(image)
        })
    }

    private fun startLoadingImage(image: PictureOfDayPhoto) {
        val src = image.imageSrc
        val options = getBaseRequestOptions()
        Glide.with(binding.imageView)
                .applyDefaultRequestOptions(options)
                .load(src)
                .transition(withCrossFade())
                .into(binding.imageView)
    }

    private fun setAllFields(image: PictureOfDayPhoto) {
        binding.authorTextView.text = image.author
        binding.dateTextView.text = image.date
        binding.titleTextView.text = image.title
        binding.descriptionTextView.text = image.description
        when(image.isFavourite) {
            true -> binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star__filled)
            false -> binding.appBar.menu.getItem(0).setIcon(R.drawable.ic_star)
        }
    }

    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, "Something went wrong: $message", Toast.LENGTH_SHORT).show()
    }

    private fun getImageOfDay() {
        val args = ImageOfDayFragmentArgs.fromBundle(requireArguments())
        val id = args.id
        viewModel.getImageOfDay(id)
    }
}