package com.example.nearbylocations.domain.entities

// 0 Free 1 Inexpensive 2 Moderate 3 Expensive 4 Very Expensive
enum class PriceLevel {
    FREE, INEXPENSIVE, MODERATE, EXPENSIVE, VERY_EXPENSIVE, NONE
}
data class NearbyPlace(
    var businessStatus: String,
    var latitude: Double,
    var longitude: Double,
    var icon: String,
    var name: String,
    var placeId: String,
    var priceLevel: PriceLevel,
    var rating: Double,
    var types: List<String> = emptyList(),
    var vicinity: String,
    var isFavorite: Boolean = false,
    var image: String = ""
)
