package com.example.nasa_mars_api_service.ui.fragment.image_of_day_description

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa_mars_api_service.core.isFavourite
import com.example.nasa_mars_api_service.core.isNotFavourite
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Matvey Popov.
 * Date: 12.01.2021
 * Time: 14:07
 * Project: NASA-Mars-API-Service
 */
class ImageOfDayViewModel(
        private val repository: BaseRepository
): ViewModel() {
    private val _imageOfDay: MutableLiveData<PictureOfDayPhoto> = MutableLiveData()
    val imageOfDay: LiveData<PictureOfDayPhoto>
    get() = _imageOfDay

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?>
    get() = _errorMessage


    fun getImageOfDay(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getPictureOfDayFromCash(id)
                _imageOfDay.postValue(result)
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }

    fun removeImageOfDayFromFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteFavouritePhoto(imageOfDay.value!!)
                _imageOfDay.postValue(imageOfDay.value!!.isNotFavourite())
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }

    fun addImageOfDayToFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPhotoToFavourite(imageOfDay.value!!)
                _imageOfDay.postValue(imageOfDay.value!!.isFavourite())
            }
            catch (ex: Exception) {
                Timber.e(ex)
                _errorMessage.postValue(ex.message)
            }
        }
    }
}