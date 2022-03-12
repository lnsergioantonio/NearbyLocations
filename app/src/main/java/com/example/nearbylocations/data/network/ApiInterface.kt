package com.example.nearbylocations.data.network

import com.example.nearbylocations.data.network.enties.PlacesResponse
import com.example.nearbylocations.data.network.enties.RoutesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("maps/api/place/nearbysearch/json")
    suspend fun fetchNearby(
        @Query("location") location: String,
        @Query("radius") radius: String,// i.e. 5000 = 5KM
        @Query("key") key: String
    ): Response<PlacesResponse>
}