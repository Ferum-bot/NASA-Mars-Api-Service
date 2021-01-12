package com.example.nasa_mars_api_service.ui.fragment.image_of_day_description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.fragment.ViewModelFactory

/**
 * Created by Matvey Popov.
 * Date: 12.01.2021
 * Time: 14:07
 * Project: NASA-Mars-API-Service
 */
class ImageOfDayViewModelFactory(
        private val repository: BaseRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageOfDayViewModel::class.java)) {
            return ImageOfDayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unexpected View Model to create: $modelClass")
    }

}