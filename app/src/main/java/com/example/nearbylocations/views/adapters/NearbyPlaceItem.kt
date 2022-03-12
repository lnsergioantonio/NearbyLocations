package com.example.nearbylocations.views.adapters

import com.example.nearbylocations.domain.entities.NearbyPlace
import com.example.nearbylocations.domain.entities.PriceLevel
import java.io.Serializable

data class NearbyPlaceItem(
    var businessStatus: String,
    var latLng: String,
    var icon: String,
    var name: String,
    var placeId: String,
    var priceLevel: PriceLevel,
    var rating: Double,
    var types: List<String> = emptyList(),
    var vicinity: String,
    var isFavorite: Boolean = false,
    var image: String = ""
):Serializable

fun List<NearbyPlace>.toItems(): List<NearbyPlaceItem> {
    return map {
        NearbyPlaceItem(
            businessStatus = it.businessStatus,
            latLng = "${it.latitude},${it.latitude}",
            icon = it.icon,
            name = it.name,
            placeId = it.placeId,
            priceLevel = it.priceLevel,
            rating = it.rating,
            types = it.types,
            vicinity = it.vicinity,
            isFavorite = it.isFavorite,
            image = it.image
        )
    }
}