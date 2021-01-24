package com.example.nasa_mars_api_service.ui.fragment.main_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.add
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.recycler_views.models.*
import com.example.nasa_mars_api_service.ui.recycler_views.models.loading.*
import kotlinx.coroutines.*
import timber.log.Timber

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

    private val _messageDelete: MutableLiveData<String?> = MutableLiveData(null)
    val messageDelete: LiveData<String?>
    get() = _messageDelete

    private val numberOfCashedPictureOfDayPhotos: Int
    get() = repository.getNumberOfCashedPictureOfDayPhotos()

    private val _isNewPhotoLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNewPhotoLoading: LiveData<Boolean>
    get() = _isNewPhotoLoading

    var isFirstLaunching: Boolean = true

    private fun postPictureOfDay(photo: PictureOfDayPhoto?) {
        if (photo == null) {
            val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
            currentResult.removeAt(0)
            _resultListForMainListAdapter.postValue(currentResult)
            return
        }
        val currentItem = PictureOfDayItem(
                picture = photo
        )
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[0] = currentItem
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun postFavouritesPhotos(photos: List<FavouritePhoto>) {
        val currentItem = if (photos.isEmpty()) {
            HorizontalFavouritePhotosListRecycler(
                    title = "No Favourites! Add One!",
                    listOf()
            )
        }
        else {
            HorizontalFavouritePhotosListRecycler(
                    listOfItems = photos
            )
        }
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[1] = currentItem
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun postNewMarsPhotos(photos: List<MarsPhoto>) {
        val currentItem = if (photos.isEmpty()) {
            GridListMarsPhotos(
                    title = "Can't find new Mars photos!",
                    listOf()
            )
        }
        else {
            GridListMarsPhotos(
                    listOfPhotos = photos
            )
        }
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        currentResult[currentResult.size - 1] = currentItem
        val loadMoreButton = LoadMoreMarsPhotos()
        currentResult.add(loadMoreButton)
        _resultListForMainListAdapter.postValue(currentResult)
    }

    private fun addNewMarsPhotosToList(photos: List<MarsPhoto>) {
        if (photos.isEmpty()) {
            return
        }
        val currentResult = _resultListForMainListAdapter.value!!.toMutableList()
        val currentItem = currentResult[currentResult.size - 2] as GridListMarsPhotos
        val currentList = currentItem.listOfPhotos.toMutableList()
        currentList.add(photos)
        currentResult[currentResult.size - 2] = GridListMarsPhotos(
                listOfPhotos = currentList.toList()
        )
        _resultListForMainListAdapter.postValue(currentResult)
        _isNewPhotoLoading.postValue(false)
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

                val result = if (numberOfCashedPictureOfDayPhotos != 0) {
                    repository.getLastPictureOfDayFromCash()
                }
                else {
                    null
                }
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
        buildLoadingList()
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

    fun addPhotoToFavourites(photo: MarsPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPhotoToFavourite(photo)
                withContext(Dispatchers.Main) {
                    getFavoritesPhoto()
                }
            }
            catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun addPhotoToFavourites(photo: PictureOfDayPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPhotoToFavourite(photo)
                withContext(Dispatchers.Main) {
                    getFavoritesPhoto()
                }
            }
            catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun deletePhotoFromFavourites(photo: MarsPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(photo)
                withContext(Dispatchers.Main) {
                    getFavoritesPhoto()
                }
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _messageDelete.postValue(ex.message)
            }
        }
    }

    fun deletePhotoFromFavourites(photo: PictureOfDayPhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(photo)
                withContext(Dispatchers.Main) {
                    getFavoritesPhoto()
                }
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _messageDelete.postValue(ex.message)
            }
        }
    }

    fun deletePhotoFromFavourites(photo: FavouritePhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(photo)
                withContext(Dispatchers.Main) {
                    getFavoritesPhoto()
                }
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _messageDelete.postValue(ex.message)
            }
        }
    }

    fun downloadNewMarsPhotos() {
        _isNewPhotoLoading.postValue(true)
        val downloadedPages = repository.numberOfAvailableMarsPhotosPages
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _statusNewMarsPhotos.postValue(MarsApiStatus.LOADING)
                val result = repository.getLastMarsPhotos(downloadedPages + 1)
                _statusNewMarsPhotos.postValue(MarsApiStatus.DONE)
                addNewMarsPhotosToList(result)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _statusNewMarsPhotos.postValue(MarsApiStatus.ERROR)
                _messageNewMarsPhotos.postValue(ex.message)
                _isNewPhotoLoading.postValue(false)
            }
        }
    }

    fun messageNewMarsPhotosWasShown() {
        _messageNewMarsPhotos.value = null
    }

    fun messageDeleteWasShown() {
        _messageDelete.value = null
    }

    fun messageFavouritesPhotosWasShown() {
        _messageFavouritesPhotos.value = null
    }

    fun messagePictureOfDayWasShown() {
        _messagePictureOfDay.value = null
    }

    companion object {
        private const val TAG = "MainListViewModel"
    }
}