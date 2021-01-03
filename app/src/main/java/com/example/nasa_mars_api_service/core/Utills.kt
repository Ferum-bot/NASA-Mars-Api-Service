package com.example.nasa_mars_api_service.core

import com.example.nasa_mars_api_service.core.models.MarsPhoto

@JvmName("addDeletedMarsPhoto")
fun MutableList<DeletedMarsPhotoDB>.add(list: List<DeletedMarsPhotoDB>) {
    val size = list.size
    for (i in 0 until size) {
        this.add(list[i])
    }
}

@JvmName("addMarsPhoto")
fun MutableList<MarsPhoto>.add(list: List<MarsPhoto>) {
    val size = list.size
    for (i in 0 until size) {
        this.add(list[i])
    }
}