package com.example.nearbylocations.domain

import com.example.nearbylocations.data.db.entities.PlacesEntity
import com.example.nearbylocations.data.network.enties.PlacesResponse
import com.example.nearbylocations.data.network.enties.RoutesResponse
import com.example.nearbylocations.domain.entities.NearbyPlace
import com.example.nearbylocations.domain.entities.PriceLevel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

fun RoutesResponse.toDomain(): MutableList<LatLng> {
    val polylineList: MutableList<LatLng> = arrayListOf()
    routes?.forEach { route ->
        route.overviewPolyline?.points.let { pointString ->
            polylineList.addAll(PolyUtil.decode(pointString))
        }
    }
    return polylineList
}

fun List<PlacesEntity>?.toDomain(): List<NearbyPlace> {
    val result: MutableList<NearbyPlace> = arrayListOf()
    this?.forEach {
        // 0 Free 1 Inexpensive 2 Moderate 3 Expensive 4 Very Expensive
        val priceLevel = when (it.priceLevel) {
            1 -> PriceLevel.FREE
            2 -> PriceLevel.INEXPENSIVE
            3 -> PriceLevel.MODERATE
            4 -> PriceLevel.EXPENSIVE
            5 -> PriceLevel.VERY_EXPENSIVE
            else -> {
                PriceLevel.NONE
            }
        }

        result.add(
            NearbyPlace(
                businessStatus = it.businessStatus,
                latitude = it.latitude,
                longitude = it.longitude,
                icon = it.icon,
                name = it.name,
                placeId = it.placeId,
                priceLevel = priceLevel,
                rating = it.rating,
                types = it.types.split(","),
                vicinity = it.vicinity,
                isFavorite = it.isFavorite == 1,
                image = it.image
            )
        )
    }
    return result
}

fun PlacesResponse.toDomain(): List<PlacesEntity> {
    val result: MutableList<PlacesEntity> = arrayListOf()
    placeResult?.forEach {
        // 0 Free 1 Inexpensive 2 Moderate 3 Expensive 4 Very Expensive

        val image = if (!it.photos.isNullOrEmpty()) {
            it.photos!![0].photoReference
        }else ""

        result.add(
            PlacesEntity(
                placeId = it.placeId.orEmpty(),
                businessStatus = it.businessStatus.orEmpty(),
                latitude = it.geometry?.location?.lat?:0.0,
                longitude = it.geometry?.location?.lng?:0.0,
                icon = it.icon.orEmpty(),
                name = it.name.orEmpty(),
                priceLevel = it.priceLevel?:-1,
                rating = it.rating?:0.0,
                types = it.types?.joinToString(",")?:"None",
                vicinity = it.vicinity?:"",
                isFavorite = 0,
                image = image.orEmpty()
            )
        )
    }
    return result
}

