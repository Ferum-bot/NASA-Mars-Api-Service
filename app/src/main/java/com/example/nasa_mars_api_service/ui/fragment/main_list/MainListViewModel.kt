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

    // Result list for main screen
    private val _resultListForMainListAdapter: MutableLiveData<List<ListItem>> = MutableLiveData(listOf())
    val resultListForMainListAdapter: LiveData<List<ListItem>>
    get() = _resultListForMainListAdapter

    // Statuses for different types
    private val _statusFavouritesPhotos: MutableLiveData<MarsApiStatus> = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
    val statusFavouritesPhotos: LiveData<MarsApiStatus>
    get() = _statusFavouritesPhotos

    private val _statusPictureOfDay: MutableLiveData<MarsApiStatus> = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
    val statusPictureOfDay: LiveData<MarsApiStatus>
    get() = _statusPictureOfDay

    private val _statusNewMarsPhotos: MutableLiveData<MarsApiStatus> = MutableLiveData(MarsApiStatus.NOT_ACTIVE)
    val statusNewMarsPhotos: LiveData<MarsApiStatus>
    get() = _statusNewMarsPhotos

    // Error messages for different types
    private val _messageFavouritesPhotos: MutableLiveData<String?> = MutableLiveData(null)
    val messageFavouritesPhotos: LiveData<String?>
    get() = _messageFavouritesPhotos

    private val _messagePictureOfDay: MutableLiveData<String?> = MutableLiveData(null)
    val messagePictureOfDay: LiveData<String?>
    get() = _messagePictureOfDay

    private val _messageNewMarsPhotos: MutableLiveData<String?> = MutableLiveData(null)
    val messageNewMarsPhotos: LiveData<String?>
    get() = _messageNewMarsPhotos

    private fun postPictureOfDay(photo: PictureOfDayPhoto) {
        val currentItem: PictureOfDayItem = PictureOfDayItem(
                picture = photo
        )
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[0] = currentItem
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun postFavouritesPhotos(photos: List<FavouritePhoto>) {
        val currentItem: HorizontalFavouritePhotosListRecycler = HorizontalFavouritePhotosListRecycler(
                listOfItems = photos
        )
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[1] = currentItem
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun postNewMarsPhotos(photos: List<MarsPhoto>) {
        val currentItem: GridListMarsPhotos = GridListMarsPhotos(
                listOfPhotos = photos
        )
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[2] = currentItem
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun getPictureOfDay() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _statusPictureOfDay.postValue(MarsApiStatus.LOADING)
                val result = repository.getPictureOfDay()
                _statusPictureOfDay.postValue(MarsApiStatus.DONE)
                postPictureOfDay(result)
            }
            catch (ex: Exception) {
                _statusPictureOfDay.postValue(MarsApiStatus.ERROR)
                _messagePictureOfDay.postValue(ex.message)

                val result = repository.getLastPictureOfDayFromCash()
                postPictureOfDay(result)
            }
        }
    }

    private fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _statusFavouritesPhotos.postValue(MarsApiStatus.LOADING)
                val result = repository.getAllFavoritesPhotos()
                postFavouritesPhotos(result)
                _statusFavouritesPhotos.postValue(MarsApiStatus.DONE)
            }
            catch (ex: Exception) {
                _statusFavouritesPhotos.postValue(MarsApiStatus.ERROR)
                _messageFavouritesPhotos.postValue(ex.message)
            }
        }
    }

    private fun getNewMarsPhotosFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _statusNewMarsPhotos.postValue(MarsApiStatus.LOADING)
                val result = repository.getLastMarsPhotos()
                postNewMarsPhotos(result)
                _statusNewMarsPhotos.postValue(MarsApiStatus.DONE)
            } catch (ex: Exception) {
                _statusNewMarsPhotos.postValue(MarsApiStatus.ERROR)
                _messageNewMarsPhotos.postValue(ex.message)

                val result = repository.getAllMarsPhotosFromCache()
                postNewMarsPhotos(result)
            }
        }
    }

    private fun buildLoadingFavoritesPhotos(): HorizontalFavouritePhotoListLoadingRecycler {
        return HorizontalFavouritePhotoListLoadingRecycler(
                listOfItems = listOf(
                        FavouritePhotoLoadingListItem(), FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem(), FavouritePhotoLoadingListItem(),
                        FavouritePhotoLoadingListItem(), FavouritePhotoLoadingListItem()
                )
        )
    }

    private fun buildLoadingGridMarsPhotos(): GridListLoadingMarsPhotos {
        return GridListLoadingMarsPhotos(
                listOfPhotos = listOf(
                        MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(),
                        MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(),
                        MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(),
                        MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(), MarsPhotoLoading(),
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
        getFavoritesPhoto()
        getPictureOfDay()
        getNewMarsPhotosFromNetwork()
    }

    fun refreshAllPhotos() {
        if (nothingIsAvailable()) {
            buildLoadingList()
        }
        getFavoritesPhoto()
        getPictureOfDay()
        getNewMarsPhotosFromNetwork()
    }

    fun nothingIsAvailable(): Boolean {
        val numberOfFavourites = repository.getNumberOfCashedFavouritesPhotos()
        val numberOfMarsPhotos = repository.getNumberOfCashedMarsPhotos()
        val numberOfDayPhoto = repository.getNumberOfCashedPictureOfDayPhotos()
        return numberOfFavourites == 0 && numberOfMarsPhotos == 0 && numberOfDayPhoto == 0
    }
}