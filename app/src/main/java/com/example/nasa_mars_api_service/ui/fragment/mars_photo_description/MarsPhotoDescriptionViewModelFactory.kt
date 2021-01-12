package com.example.nasa_mars_api_service.ui.fragment.mars_photo_description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

/**
 * Created by Matvey Popov.
 * Date: 11.01.2021
 * Time: 22:14
 * Project: NASA-Mars-API-Service
 */
class MarsPhotoDescriptionViewModelFactory(
        private val repository: BaseRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarsPhotoDescriptionViewModel::class.java)) {
            return MarsPhotoDescriptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unexpected view model to create: $modelClass")
    }
}