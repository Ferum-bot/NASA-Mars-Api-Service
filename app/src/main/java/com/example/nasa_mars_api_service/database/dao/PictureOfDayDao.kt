package com.example.nasa_mars_api_service.database.dao

import androidx.room.*
import androidx.room.RoomMasterTable.TABLE_NAME
import com.example.nasa_mars_api_service.database.entities.PictureOfDayPhotoDB

/**
 * Created by Matvey Popov.
 * Date: 05.01.2021
 * Time: 22:49
 * Project: NASA-Mars-API-Service
 */
@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureOfDayPhotoDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictures(pictures: List<PictureOfDayPhotoDB>)

    @Delete
    suspend fun deletePicture(picture: PictureOfDayPhotoDB)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAllPictures()

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllPictures(): List<PictureOfDayPhotoDB>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id DESC LIMIT 1")
    suspend fun getLastPicture(): PictureOfDayPhotoDB

    @Query("SELECT EXISTS (SELECT description FROM $TABLE_NAME WHERE description = :description)")
    suspend fun isPictureExists(description: String): Boolean

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getPictureOfDay(id: Int): PictureOfDayPhotoDB

    companion object {
        private const val TABLE_NAME = "picture_of_day_photo_table"
    }

}