package com.example.nearbylocations

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.nearbylocations.data.db.NearbyLocationsDB
import com.example.nearbylocations.data.db.providePlacesDao
import com.example.nearbylocations.data.network.ApiInterface
import com.example.nearbylocations.data.network.Network
import com.example.nearbylocations.data.network.NetworkHandler
import com.example.nearbylocations.databinding.ActivityMainBinding
import com.example.nearbylocations.domain.DirectionsRepositoryImpl
import com.example.nearbylocations.domain.FetchNearbyLocationsUseCase
import com.example.nearbylocations.domain.UpdateFavoritePlaceUseCase
import com.example.nearbylocations.domain.base.State
import com.example.nearbylocations.domain.entities.NearbyPlace
import com.example.nearbylocations.views.adapters.ItemPlace
import com.example.nearbylocations.ext.checkPermission
import com.example.nearbylocations.ext.isPermissionGranted
import com.example.nearbylocations.ext.requestOnePermission
import com.example.nearbylocations.ext.toast
import com.example.nearbylocations.views.PlaceDetailActivity
import com.example.nearbylocations.views.adapters.NearbyPlacesAdapter
import com.example.nearbylocations.views.adapters.toItems
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places


const val REQUEST_CODE_LOCATION = 617254

class MainActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationButtonClickListener,
    OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var mMap: GoogleMap? = null
    private var origin: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mDestinationMarker: Marker? = null
    private var mDestinationItemPlace: ItemPlace? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initObservers()
        initToolbar()

        // validate permission for location or request permission
        if (checkPermission(ACCESS_FINE_LOCATION)) {
            initFusedLocation()
            initMap()
        } else
            requestOnePermission(ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION)
    }

    private fun initViewModel() {
        val db = NearbyLocationsDB.getDatabase(application)
        val dao = providePlacesDao(db)
        val api = Network.createNetworkClient(true, BuildConfig.URL_BASE).create(ApiInterface::class.java)
        val repository = DirectionsRepositoryImpl(api, dao, NetworkHandler(this))
        val placeUseCase = FetchNearbyLocationsUseCase(repository)
        val updateFavoritePlaceUseCase = UpdateFavoritePlaceUseCase(repository)
        viewModel = MainViewModel(placeUseCase, updateFavoritePlaceUseCase)
    }

    private fun initObservers() {
        viewModel.placesState.observe(this, this::onPlacesStateChange)
    }

    @SuppressLint("MissingPermission")
    private fun initFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                    updateMapLocation(location)
            }
    }

    private fun updateMapLocation(location: Location?) {
        origin = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

        location?.let {
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(
                location.latitude,
                location.longitude
            )))

            mMap?.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
            val params = LatLng(location.latitude, location.longitude)
            viewModel.fetchNearbyLocations(params)
        }
    }

    private fun onPlacesStateChange(result: State<List<NearbyPlace>>?) {
        result?.let { noNullResult ->
            when (noNullResult) {
                is State.Failure -> {
                    Log.e("Main","onPlacesStateChange", noNullResult.exception)
                }
                is State.Progress -> {}
                is State.Success -> {
                    drawNearPlaces(noNullResult.data)
                    initListPlaces(noNullResult.data)
                }
            }
        }
    }

    private fun initListPlaces(data: List<NearbyPlace>) {
        val adapter = NearbyPlacesAdapter(data.toItems()).apply {
            setOnClickFav {
                viewModel.saveFavorite(it.placeId, it.isFavorite)
            }

            setOnClickItem {
                PlaceDetailActivity.launchScreen(this@MainActivity, it)
            }
        }
        binding.recyclerViewPlaces.adapter = adapter

    }

    private fun drawNearPlaces(nearbyPlaceList: List<NearbyPlace>) {
        mMap?.clear()
        nearbyPlaceList.forEach {
            val options = MarkerOptions().apply {
                position(LatLng(it.latitude,it.longitude))
            }
            mMap?.addMarker(options)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap?.isMyLocationEnabled = true
        mMap?.setOnMyLocationButtonClickListener(this)
        mMap?.setOnMyLocationClickListener(this)
        mMap?.setOnMarkerClickListener(this)

    }

    override fun onMyLocationClick(location: Location) {
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker == mDestinationMarker && mDestinationItemPlace!=null){

            return false
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CODE_LOCATION) {
            return
        }
        if (isPermissionGranted(permissions, grantResults, ACCESS_FINE_LOCATION)) {
            initFusedLocation()
            initMap()
        } else {
            toast("Permission was denied")
        }
    }
}