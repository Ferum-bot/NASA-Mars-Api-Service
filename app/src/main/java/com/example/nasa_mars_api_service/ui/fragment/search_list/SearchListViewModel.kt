package com.example.nasa_mars_api_service.ui.fragment.search_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.MarsPhotoToSearch
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.example.nasa_mars_api_service.ui.recycler_views.models.LoadMoreMarsPhotos
import com.example.nasa_mars_api_service.ui.recycler_views.models.loading.MarsPhotoSearchListLoadingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Matvey Popov.
 * Date: 03.01.2021
 * Time: 23:58
 * Project: NASA-Mars-API-Service
 */
class SearchListViewModel(
        private val repository: BaseRepository
): ViewModel() {
    private val _resultListForRecyclerView: MutableLiveData<List<ListItem>> = MutableLiveData(listOf())
    val resultListForRecyclerView: LiveData<List<ListItem>>
    get() = _resultListForRecyclerView

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?>
    get() = _errorMessage

    private val _statusForLoadingNewPhotos: MutableLiveData<MarsApiStatus> = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
    val statusForLoadingNewPhotos: LiveData<MarsApiStatus>
    get() = _statusForLoadingNewPhotos

    var isFirstLaunching: Boolean = true

    private fun buildLoadingList() {
        _resultListForRecyclerView.postValue(listOf(
                MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(),
                MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(),
                MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(), MarsPhotoSearchListLoadingItem(),
        ))
    }

    private fun postMarsPhotos(marsPhotos: List<MarsPhoto>) {
        var currentResult: MutableList<ListItem> = marsPhotos.toMutableList()
        currentResult.add(LoadMoreMarsPhotos())
        _resultListForRecyclerView.postValue(currentResult)
    }

    private fun postEmptyListOfPhotos() {
        _resultListForRecyclerView.postValue(emptyList())
    }

    private fun addMarsPhotos(marsPhotos: List<MarsPhoto>) {
        var currentResult = _resultListForRecyclerView.value!!.toMutableList()
        currentResult.removeAt(currentResult.size - 1)
        marsPhotos.forEach { currentResult.add(it) }
        currentResult.add(LoadMoreMarsPhotos())
        _resultListForRecyclerView.postValue(currentResult)
    }

    private fun searchMarsPhotosFromNetwork(photoToSearch: MarsPhotoToSearch) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val camera = photoToSearch.camera
                val date = photoToSearch.dateType
                val rover = photoToSearch.rover
                val result = repository.searchMarsPhotos(1, date, rover, camera)
                if (result.isEmpty()) {
                    _errorMessage.postValue("Nothing to show!")
                    postEmptyListOfPhotos()
                    return@launch
                }
                postMarsPhotos(result)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
                postEmptyListOfPhotos()
            }
        }
    }

    fun searchMarsPhotos(photoToSearch: MarsPhotoToSearch) {
        buildLoadingList()
        searchMarsPhotosFromNetwork(photoToSearch)
    }

    fun refreshSearch(photoToSearch: MarsPhotoToSearch) {
        searchMarsPhotosFromNetwork(photoToSearch)
    }

    fun downloadMoreMarsPhotos(photoToSearch: MarsPhotoToSearch) {
        viewModelScope.launch(Dispatchers.IO) {
            val downloadedPages = repository.numberOfAvailableSearchMarsPhotosPages
            try {
                _statusForLoadingNewPhotos.postValue(MarsApiStatus.LOADING)
                val camera = photoToSearch.camera
                val date = photoToSearch.dateType
                val rover = photoToSearch.rover
                val result = repository.searchMarsPhotos(downloadedPages + 1, date, rover, camera)
                if (result.isEmpty()) {
                    _errorMessage.postValue("That's all mars Photos!")
                    _statusForLoadingNewPhotos.postValue(MarsApiStatus.DONE)
                    return@launch
                }
                addMarsPhotos(result)
                _statusForLoadingNewPhotos.postValue(MarsApiStatus.DONE)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
                _statusForLoadingNewPhotos.postValue(MarsApiStatus.ERROR)
            }
        }
    }

    fun addPhotoToFavourites(photoToAdd: MarsPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPhotoToFavourite(photoToAdd)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }

    fun removePhotoFromFavourites(photoToRemove: MarsPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(photoToRemove)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }

    fun errorMessageWasShown() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()

        repository.clearNumberOfAvailableSearchMarsPhotos()
    }
}