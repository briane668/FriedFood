package com.wade.friedfood.map


import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemMapBinding
import com.wade.friedfood.getDistance

import com.wade.friedfood.map.MapViewModel.Companion.userPosition
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class MapAdapter(
    private val onClickListener: OnClickListener,
    private val mapViewModel: MapViewModel
) : ListAdapter<Shop, MapAdapter.ShopsViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */


    class ShopsViewHolder(var binding: ItemMapBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(shop: Shop, mapViewModel: MapViewModel) {
            val x = userPosition.value?.latitude
            val y = userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude
            val m = getDistance(
                x ?: 0.toDouble(),
                y ?: 0.toDouble(),
                r ?: 0.toDouble(),
                s ?: 0.toDouble()
            )
            val distance = m.roundToInt()
            binding.distance.text = "距離:${distance}公尺"




            mapViewModel.coroutineScope.launch {
                val commentCount = mapViewModel.getCommentsByShop(shop)
                binding.recommend.text = "$commentCount 則評論"
                binding.executePendingBindings()
            }


            mapViewModel.coroutineScope.launch {
                val rating = mapViewModel.getRatingByShop(shop)
                binding.star.text = "$rating 顆星"
                binding.executePendingBindings()
            }

            binding.mapItem = shop

            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem === newItem
        }


        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopsViewHolder {

//parent ,false 橫向recycleview 會影響?
//多載
        return ShopsViewHolder(
            ItemMapBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: ShopsViewHolder, position: Int) {
        val shop = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(shop)
        }



        holder.bind(shop, mapViewModel)
    }

    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }


}
