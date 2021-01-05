package com.example.nasa_mars_api_service.ui.fragment.main_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.recycler_views.models.*
import com.example.nasa_mars_api_service.ui.recycler_views.models.loading.*
import kotlinx.coroutines.*

class MainListViewModel(
    private val repository: BaseRepository
): ViewModel() {

    private val _resultListForMainListAdapter: MutableLiveData<List<ListItem>> = MutableLiveData(listOf())
    val resultListForMainListAdapter: LiveData<List<ListItem>>
    get() = _resultListForMainListAdapter

    private val _pictureOfDay: MutableLiveData<PictureOfDayPhoto?> = MutableLiveData(null)
    val pictureOfDayPhoto: LiveData<PictureOfDayPhoto?>
    get() = _pictureOfDay

    private val _favoritePhotos: MutableLiveData<List<FavouritePhoto>> = MutableLiveData(listOf())
    val favoritePhotos: LiveData<List<FavouritePhoto>>
    get() = _favoritePhotos

    private val _newMarsPhotos: MutableLiveData<List<MarsPhoto>> = MutableLiveData(listOf())
    val newMarsPhotos: LiveData<List<MarsPhoto>>
    get() = _newMarsPhotos

    private val _status: MutableLiveData<MarsApiStatus> = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
    val status: LiveData<MarsApiStatus>
    get() = _status

    private val _message: MutableLiveData<String?> = MutableLiveData(null)
    val message: LiveData<String?>
    get() = _message

    private suspend fun getPictureOfDay() {
        val result = repository.getPictureOfDay()
        _pictureOfDay.postValue(result)
    }

    private suspend fun getFavoritesPhoto() {
        val result = repository.getAllFavoritesPhotos()
    }

    private suspend fun getNewMarsPhotosFromNetwork() {
        val result = repository.getLastMarsPhotos()
    }

    private fun buildLoadingFavoritesPhotos(): HorizontalFavouritePhotoListLoadingRecycler {
        return HorizontalFavouritePhotoListLoadingRecycler(
                listOfItems = listOf(
                        FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem()
                )
        )
    }

    private fun buildLoadingGridMarsPhotos(): GridListLoadingMarsPhotos {
        return GridListLoadingMarsPhotos(
                listOfPhotos = listOf(
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                        MarsPhotoLoading(),
                )
        )
    }

    private fun buildLoadingList() {
        val result = listOf<ListItem>(
                PictureOfDayLoadingItem(),
                buildLoadingFavoritesPhotos(),
                buildLoadingGridMarsPhotos()
        )
        _resultListForMainListAdapter.value = result
    }

    fun getResultList() {
        buildLoadingList()

//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                getPictureOfDay()
//            }
//            catch (ex: Exception) {
//
//            }
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                getFavoritesPhoto()
//            }
//            catch (ex: Exception) {
//
//            }
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                getNewMarsPhotosFromNetwork()
//            }
//            catch (ex: Exception) {
//
//            }
//        }
    }

    fun refreshAllPhotos() {

    }

}