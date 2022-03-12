package com.example.nearbylocations.domain

import com.example.nearbylocations.BuildConfig
import com.example.nearbylocations.data.db.dao.PlacesDao
import com.example.nearbylocations.data.db.entities.PlacesEntity
import com.example.nearbylocations.data.network.ApiInterface
import com.example.nearbylocations.data.network.NetworkHandler
import com.example.nearbylocations.domain.base.NetworkConnectionException
import com.example.nearbylocations.domain.base.NoDataException
import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.entities.NearbyPlace
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

interface DirectionsRepository {
    fun fetchNearbyLocations(latitude: Double, longitude: Double): Flow<State<List<NearbyPlace>>>
    fun saveFavorite(placeId:String, currentIsFavorite:Boolean): Flow<State<Boolean>>
}

class DirectionsRepositoryImpl(
    private val api: ApiInterface,
    private val placesDao: PlacesDao,
    private val networkHandler: NetworkHandler
) : DirectionsRepository {

    override fun fetchNearbyLocations(
        latitude: Double,
        longitude: Double
    ): Flow<State<List<NearbyPlace>>> {
        return flow {
            val result = when (networkHandler.isConnected) {
                true -> {
                    api.fetchNearby(
                        "$latitude,$longitude",
                        "1800",
                        BuildConfig.MAPS_API_KEY
                    ).run {
                        if (isSuccessful && body() != null) {
                            placesDao.deleteAllAndInsert(body()!!.toDomain())
                            State.Success(findLastPlaces())
                        } else {
                            State.Failure(NoDataException())
                        }
                    }
                }
                false -> {
                    State.Success(findLastPlaces())
                }
            }
            emit(result)
        }.onStart {
            State.Progress(true)
        }.catch {
            State.Failure(it)
        }
    }

    override fun saveFavorite(placeId: String, currentIsFavorite:Boolean): Flow<State<Boolean>> {
        return flow {
            placesDao.updateFavorite(placeId, isFav = if (currentIsFavorite) 0 else 1)
            emit(State.Success(true))
        }.onStart {
            State.Progress(true)
        }.catch {
            State.Failure(it)
        }
    }

    private suspend fun findLastPlaces(): List<NearbyPlace> {
        return placesDao.findAll().toDomain()
    }
}