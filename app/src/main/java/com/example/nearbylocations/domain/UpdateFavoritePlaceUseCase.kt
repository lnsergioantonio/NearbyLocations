package com.example.nearbylocations.domain

import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.base.UseCase
import kotlinx.coroutines.flow.Flow

class UpdateFavoritePlaceUseCase(private val repository: DirectionsRepository):
    UseCase<Boolean, UpdateFavoritePlaceUseCase.Params>() {

    override fun run(params: Params): Flow<State<Boolean>> {
        return repository.saveFavorite(params.placeId, params.isFavorite)
    }

    data class Params(
        val placeId:String,
        val isFavorite:Boolean
    )

}