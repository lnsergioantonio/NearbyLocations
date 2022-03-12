package com.example.nearbylocations.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbylocations.R
import com.example.nearbylocations.databinding.ItemNearbyPlaceBinding

class NearbyPlacesAdapter(
    private var nearbyPlaceList: List<NearbyPlaceItem>
) : RecyclerView.Adapter<NearbyPlaceViewHolder>() {

    private var onClickItem: (NearbyPlaceItem) -> Unit = {}
    private var onClickFav: (NearbyPlaceItem) -> Unit = {}

    fun setOnClickItem(onClick: (NearbyPlaceItem) -> Unit) {
        this.onClickItem = onClick
    }

    fun setOnClickFav(onClick: (NearbyPlaceItem) -> Unit) {
        this.onClickFav = onClick
    }

    fun updatePlaces(places: ArrayList<NearbyPlaceItem>) {
        this.nearbyPlaceList = places
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyPlaceViewHolder {
        val binding =
            ItemNearbyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NearbyPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyPlaceViewHolder, position: Int) {
        holder.bind(nearbyPlaceList[position], onClickItem, onClickFav)
    }

    override fun getItemCount(): Int = nearbyPlaceList.size


}

class NearbyPlaceViewHolder(private val binding: ItemNearbyPlaceBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(itemPlace: NearbyPlaceItem, onClickItem: (NearbyPlaceItem) -> Unit, onClickFav: (NearbyPlaceItem) -> Unit) {
        with(binding) {
            labelName.text = itemPlace.name
            labelAddress.text = itemPlace.vicinity

            setFav(itemPlace.isFavorite)

            imageFav.setOnClickListener {
                onClickFav.invoke(itemPlace)
                setFav(!itemPlace.isFavorite)
                itemPlace.isFavorite = !itemPlace.isFavorite
            }
        }

        itemView.setOnClickListener {
            onClickItem.invoke(itemPlace)
        }
    }

    private fun setFav(isFav:Boolean){
        if (isFav)
            binding.imageFav.setImageResource(R.drawable.ic_fav)
        else
            binding.imageFav.setImageResource(R.drawable.ic_unfav)
    }
}