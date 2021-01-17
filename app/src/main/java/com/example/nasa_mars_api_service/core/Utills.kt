package com.example.nasa_mars_api_service.core

import android.net.Uri
import androidx.core.net.toUri
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.models.MarsPhoto

@JvmName("addMarsPhoto")
fun MutableList<MarsPhoto>.add(list: List<MarsPhoto>) {
    val size = list.size
    for (i in 0 until size) {
        this.add(list[i])
    }
}

fun buildUTIFromURL(url: String): Uri {
    return url.toUri().buildUpon().scheme("https").build()
}

fun getBaseRequestOptions(): RequestOptions {
    return RequestOptions()
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.connection_error_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
}

fun String.toSearchDateFormat(): String {
    val listOfDates = this.split(".")
    return listOfDates[2] + "-" + listOfDates[1] + "-" + listOfDates[0]
}

fun String.toDefaultDateFormat(): String {
    val listOfDates = this.split("-")
    return listOfDates[2] + "." + listOfDates[1] + "." + listOfDates[0]
}

fun getSearchDateFormatFrom(dayOfMonth: String, month: String, year: String): String {
    val resultDate = StringBuilder()
    resultDate.append(
            when(dayOfMonth.length) {
                1 -> "0$dayOfMonth"
                2 -> dayOfMonth
                else ->
                    throw IllegalArgumentException("Invalid day of month: $dayOfMonth")
            }
    )
    resultDate.append(".")
    resultDate.append(
            when(month.length) {
                1 -> "0$month"
                2 -> month
                else ->
                    throw IllegalArgumentException("Invalid month: $month")
            }
    )
    resultDate.append(".")
    resultDate.append(year)
    return resultDate.toString()
}

fun getSearchDateFormatFrom(dayOfMonth: Int, month: Int, year: Int): String {
    return getSearchDateFormatFrom(dayOfMonth.toString(), (month + 1).toString(), year.toString())
}