package com.example.nearbylocations.domain

import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.base.UseCase
import com.example.nearbylocations.domain.entities.NearbyPlace
import kotlinx.coroutines.flow.Flow

class FetchNearbyLocationsUseCase(private val repository: DirectionsRepository):
    UseCase<List<NearbyPlace>, FetchNearbyLocationsUseCase.Params>() {

    override fun run(params: Params): Flow<State<List<NearbyPlace>>> {
        return repository.fetchNearbyLocations(params.latitude,params.longitude)
    }

    data class Params(
        val latitude: Double = 0.0,
        val longitude:Double = 0.0
    )

}