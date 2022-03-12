package com.example.nearbylocations.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlacesEntity(
    @PrimaryKey
    var placeId: String = "",
    var businessStatus: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var icon: String = "",
    var name: String = "",
    var priceLevel: Int = 0,
    var rating: Double = 0.0,
    var types: String = "",
    var vicinity: String = "",
    var isFavorite: Int = 0, // 1-yes/0-no
    var image: String = ""
)