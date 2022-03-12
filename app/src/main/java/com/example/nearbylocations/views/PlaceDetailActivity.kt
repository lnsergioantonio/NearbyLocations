package com.example.nearbylocations.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.example.nearbylocations.BuildConfig
import com.example.nearbylocations.R
import com.example.nearbylocations.databinding.ActivityPlaceDetailBinding
import com.example.nearbylocations.domain.entities.PriceLevel
import com.example.nearbylocations.views.adapters.NearbyPlaceItem

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var nearbyPlaceItem: NearbyPlaceItem
    private lateinit var binding: ActivityPlaceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initExtras()
        initViews()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_location_details)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        with(binding){
            labelPlaceDetailName.text = nearbyPlaceItem.name
            labelPlaceDetailAddress.text = nearbyPlaceItem.vicinity
            labelPlaceDetailRating.text = "${nearbyPlaceItem.rating}"
            labelPlaceDetailPriceLevel.setText(
                when(nearbyPlaceItem.priceLevel){
                    PriceLevel.FREE -> R.string.label_free
                    PriceLevel.INEXPENSIVE -> R.string.label_inexpensive
                    PriceLevel.MODERATE -> R.string.label_moderate
                    PriceLevel.EXPENSIVE -> R.string.label_expensive
                    PriceLevel.VERY_EXPENSIVE -> R.string.label_very_expensive
                    else -> R.string.none
                }
            )

            if (nearbyPlaceItem.isFavorite)
                imagePlaceDetailFav.setImageResource(R.drawable.ic_fav)
            else
                imagePlaceDetailFav.setImageResource(R.drawable.ic_unfav)

            ratingBar.rating = nearbyPlaceItem.rating.toFloat()

            labelPlaceDetailRating.text = "(${nearbyPlaceItem.rating})"

            labelPlaceDetailLocation.text = nearbyPlaceItem.latLng
            // load images
            if (nearbyPlaceItem.image.isNotEmpty()){
                val uri = "${BuildConfig.URL_BASE_PIC}${nearbyPlaceItem.image}&key=${BuildConfig.MAPS_API_KEY}"
                val imageLoader = ImageLoader(this@PlaceDetailActivity)
                val request = ImageRequest.Builder(this@PlaceDetailActivity)
                    .data(uri)
                    .target { drawable ->
                        imageProfile.load(drawable)
                        imageBack.load(drawable) {
                            transformations(BlurTransformation(this@PlaceDetailActivity,10f), CircleCropTransformation())
                        }
                    }
                    .build()
                val disposable = imageLoader.enqueue(request)
            }else{
                imageProfile.setImageResource(R.drawable.background_corner_view)
            }
        }
    }

    private fun initExtras() {
        intent.extras?.let {
            nearbyPlaceItem = it.getSerializable(ARG_DATA) as NearbyPlaceItem

        }
    }

    companion object{
        const val ARG_DATA = "data"
        fun launchScreen(activity: Context, nearbyPlaceItem: NearbyPlaceItem) {
            val intent = Intent(activity, PlaceDetailActivity::class.java).apply {
                putExtra(ARG_DATA, nearbyPlaceItem)
            }
            activity.startActivity(intent)
        }
    }
}