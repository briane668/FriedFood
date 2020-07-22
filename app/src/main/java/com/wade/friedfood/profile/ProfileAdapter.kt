package com.wade.friedfood.profile


import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.publisher.util.Logger

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
class ProfileAdapter(private val onClickListener: OnClickListener,
                 val profileViewModel: ProfileViewModel
) : ListAdapter<Shop, ProfileAdapter.MarsPropertyViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */


    class MarsPropertyViewHolder( var binding: ItemMapBinding):
        RecyclerView.ViewHolder(binding.root) {


        fun bind(shop: Shop, profileViewModel: ProfileViewModel) {

            val x = userPosition.value?.latitude
            val y = userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude

            val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
            val k = m.roundToInt()

//            mapViewModel.getHowManyComments(shop)
//
//            mapViewModel.HowManyComments.observe(fragment.viewLifecycleOwner, Observer {
//                binding.mapItem = shop
//                binding.star.text="${shop.star}顆星"
//                binding.recommend.text="${mapViewModel.HowManyComments.value}則評論"
//                binding.distance.text = "${k}公尺"
//                binding.executePendingBindings()
//            })
            profileViewModel.coroutineScope.launch {

                val commentCount =profileViewModel.getCommentsByShop(shop)
                Logger.w("shop=${shop.name}, commentCount=$commentCount")

                binding.recommend.text="$commentCount 則評論"
                binding.executePendingBindings()
            }

            profileViewModel.coroutineScope.launch {

                val rating =profileViewModel.getRatingByShop(shop)
                Logger.w("shop=${shop.name}, commentCount=$rating")

                binding.star.text="$rating 顆星"
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
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MarsPropertyViewHolder {

//parent ,false 橫向recycleview 會影響?
//多載
        return MarsPropertyViewHolder(ItemMapBinding.inflate(LayoutInflater.from(parent.context), parent ,false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */


    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(marsProperty)
        }



        holder.bind(marsProperty ,profileViewModel)
    }

    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }







}