package com.example.nearbylocations.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nearbylocations.data.db.dao.PlacesDao
import com.example.nearbylocations.data.db.entities.PlacesEntity

private const val CURRENT_VERSION = 1

private fun provideDatabase(application: Application): NearbyLocationsDB {
    return Room.databaseBuilder(application, NearbyLocationsDB::class.java, "NearbyLocations.db")
        .build()
}

fun providePlacesDao(database: NearbyLocationsDB): PlacesDao {
    return database.placesDao()
}

@Database(entities = [PlacesEntity::class], version = CURRENT_VERSION)
abstract class NearbyLocationsDB : RoomDatabase() {
    abstract fun placesDao(): PlacesDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NearbyLocationsDB? = null

        fun getDatabase(context: Application): NearbyLocationsDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = provideDatabase(context)
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}