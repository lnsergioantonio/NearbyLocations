package com.example.nearbylocations.data.network.enties

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    @SerializedName("results") var placeResult: ArrayList<PlaceResult>?,
    @SerializedName("status") var status: String?
)

data class PlaceResult(
    @SerializedName("business_status") var businessStatus: String?,
    @SerializedName("geometry") var geometry: Geometry?,
    @SerializedName("icon") var icon: String?,
    @SerializedName("icon_background_color") var iconBackgroundColor: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("place_id") var placeId: String?,
    @SerializedName("price_level") var priceLevel: Int?, //     0 Free 1 Inexpensive 2 Moderate 3 Expensive 4 Very Expensive
    @SerializedName("rating") var rating: Double?,
    @SerializedName("types") var types: ArrayList<String>?,
    @SerializedName("vicinity") var vicinity: String?,
    @SerializedName("photos") var photos: ArrayList<Photos>?,

    )

data class Photos(
    @SerializedName("height") var height: Int? = null,
    @SerializedName("html_attributions") var htmlAttributions: ArrayList<String> = arrayListOf(),
    @SerializedName("photo_reference") var photoReference: String? = null,
    @SerializedName("width") var width: Int? = null
)

data class Geometry(
    @SerializedName("location") var location: Location?,
)

data class Location(
    @SerializedName("lat") var lat: Double?,
    @SerializedName("lng") var lng: Double?
)