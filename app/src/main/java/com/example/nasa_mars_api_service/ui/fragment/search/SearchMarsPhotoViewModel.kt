package com.example.nasa_mars_api_service.ui.fragment.search

import androidx.lifecycle.ViewModel
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.models.MarsPhotoToSearch

/**
 * Created by Matvey Popov.
 * Date: 01.01.2021
 * Time: 19:05
 * Project: NASA-Mars-API-Service
 */
class SearchMarsPhotoViewModel: ViewModel() {

    var dateType: MarsDateTypes? = null
    var date: String? = null
    var rover: MarsRovers? = null
    var camera: MarsRoversCamera? = null

    fun getAvailableRoverCameras(): List<MarsRoversCamera> {
        return MarsRoversCamera.getAvailableCamerasFromRover(rover!!)
    }

    fun isDateCorrect(date: String): Boolean {
        return when(dateType) {
            MarsDateTypes.MARS_SOL -> {
                val marsDate = try {
                    date.toInt()
                }
                catch (ex: Exception) {
                    return false
                }
                marsDate in 0..1000

            }
            MarsDateTypes.EARTH_DATE -> {
                val regex = """((0?[1-9]|[12][0-9]|3[01]){1,2}[\.\\/-](0?[1-9]|1[012]){1,2}[\.\\/-](((19|20)\d\d)|(\d\d)))"""
                regex.toRegex().find(date) != null
            }
            else -> {
                false
            }
        }
    }

    fun setNewDate(date: String) {
        this.date = date
        this.dateType!!.date = date
    }

    fun getChosenRoverCameraShortName(): String {
        if (camera != null) {
            return camera!!.name
        }
        return "FHAZ"
    }

    fun searchParametersAreValid(): Boolean {
        return date != null && rover != null && camera != null && dateType != null
    }

    fun getSearchParameters(): MarsPhotoToSearch {
        return MarsPhotoToSearch(
                dateType!!,
                date!!,
                rover!!,
                camera!!
        )
    }
}