package com.example.nasa_mars_api_service.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_mars_photos_table")
data class DeletedMarsPhotoDB(
    @PrimaryKey
    val id: Int = 0
)