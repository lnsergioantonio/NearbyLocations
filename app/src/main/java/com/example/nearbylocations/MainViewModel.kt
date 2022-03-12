package com.example.nearbylocations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearbylocations.domain.FetchNearbyLocationsUseCase
import com.example.nearbylocations.domain.UpdateFavoritePlaceUseCase
import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.entities.NearbyPlace
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val fetchNearbyLocationsUseCase: FetchNearbyLocationsUseCase,
    private val updateFavoritePlaceUseCase: UpdateFavoritePlaceUseCase
    ):ViewModel() {

    private val placesLiveData = MutableLiveData<State<List<NearbyPlace>>>()
    val placesState:LiveData<State<List<NearbyPlace>>> get() = placesLiveData

    fun fetchNearbyLocations(location: LatLng) {
        val params = FetchNearbyLocationsUseCase.Params(location.latitude, location.longitude)
        fetchNearbyLocationsUseCase.invoke(viewModelScope,params){
            placesLiveData.value = it
        }
    }

    fun saveFavorite(placeId: String, currentIsFavorite: Boolean) {
        val params = UpdateFavoritePlaceUseCase.Params(placeId, currentIsFavorite)
        updateFavoritePlaceUseCase.invoke(viewModelScope, params){}
    }
}