package com.example.nasa_mars_api_service.ui.fragment.main_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository

class MainListViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainListViewModel::class.java)) {
            return MainListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unexpected modeClass: ${modelClass.toString()}")
    }

}