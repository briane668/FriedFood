package com.wade.friedfood.profile


import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wade.friedfood.util.Logger

import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemCollectBinding
import com.wade.friedfood.getDistance

import com.wade.friedfood.map.MapViewModel.Companion.userPosition
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class ProfileAdapter(
    private val onClickListener: OnClickListener,
    val profileViewModel: ProfileViewModel
) : ListAdapter<Shop, ProfileAdapter.ShopViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */


    class ShopViewHolder(var binding: ItemCollectBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(shop: Shop, profileViewModel: ProfileViewModel) {

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
            val k = m.roundToInt()

            profileViewModel.coroutineScope.launch {

                val commentCount = profileViewModel.getCommentsByShop(shop)
                Logger.w("shop=${shop.name}, commentCount=$commentCount")

                binding.recommend.text = "$commentCount 則評論"
                binding.executePendingBindings()
            }

            profileViewModel.coroutineScope.launch {

                val rating = profileViewModel.getRatingByShop(shop)
                Logger.w("shop=${shop.name}, commentCount=$rating")

                binding.star.text = "$rating 顆星"
                binding.executePendingBindings()
            }
            binding.mapItem = shop
            binding.distance.text = "${k}公尺"
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
    ): ShopViewHolder {

//parent ,false

        return ShopViewHolder(
            ItemCollectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(shop)
        }



        holder.bind(shop, profileViewModel)
    }
    
    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }


}