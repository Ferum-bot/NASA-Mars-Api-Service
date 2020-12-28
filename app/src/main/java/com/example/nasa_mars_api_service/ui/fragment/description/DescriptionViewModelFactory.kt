package com.example.nasa_mars_api_service.ui.fragment.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.example.nasa_mars_api_service.repository.MainRepository

class DescriptionViewModelFactory(
    private val id: Int,
    private val repository: MainRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DescriptionViewModel::class.java)) {
            return DescriptionViewModel(id, repository) as T
        }
        throw IllegalArgumentException("Unexpected View Model: ${modelClass.toString()}")
    }
}