package com.example.nasa_mars_api_service.core.models

import androidx.annotation.AnimatorRes
import androidx.annotation.StringRes

/**
 * Created by Matvey Popov.
 * Date: 04.01.2021
 * Time: 23:00
 * Project: NASA-Mars-API-Service
 */
interface ResourceProvider {

    fun string(@StringRes id: Int): String

}