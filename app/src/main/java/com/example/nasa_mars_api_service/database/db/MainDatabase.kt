package com.example.nasa_mars_api_service.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nasa_mars_api_service.database.dao.DeletedMarsPhotoDao
import com.example.nasa_mars_api_service.database.dao.MarsPhotoDao
import com.example.nasa_mars_api_service.database.db.MainDatabase.Companion.DATABASE_VERSION
import com.example.nasa_mars_api_service.database.entities.DeletedMarsPhotoDB
import com.example.nasa_mars_api_service.database.entities.MarsPhotoDB

@Database(
    entities = [MarsPhotoDB::class, DeletedMarsPhotoDB::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class MainDatabase: RoomDatabase() {

    abstract val marsPhotoDao: MarsPhotoDao
    abstract val deletedMarsPhotoDao: DeletedMarsPhotoDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "main_database.db"

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}