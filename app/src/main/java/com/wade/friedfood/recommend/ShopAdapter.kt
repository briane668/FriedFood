/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.wade.friedfood.recommend

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.publisher.util.Logger
import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemShopBinding
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class ShopAdapter(private val onClickListener: OnClickListener,val recommendViewModel: RecommendViewModel) : ListAdapter<Shop, ShopAdapter.MarsPropertyViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class MarsPropertyViewHolder( var binding: ItemShopBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: Shop,recommendViewModel: RecommendViewModel) {


            val x = MapViewModel.userPosition.value?.latitude
            val y = MapViewModel.userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude

            val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
            val k = m.roundToInt()





//            recommendViewModel.coroutineScope.launch {
//
//                val commentCount =recommendViewModel.getCommentsByShop(shop)
//                Logger.w("shop=${shop.name}, commentCount=$commentCount")
//
//                binding.recommend.text="$commentCount 則評論"
//                binding.executePendingBindings()
//            }
//
//            recommendViewModel.coroutineScope.launch {
//
//                val rating =recommendViewModel.getRatingByShop(shop)
//                Logger.w("shop=${shop.name}, commentCount=$rating")
//
//                binding.star.text="$rating 顆星"
//                binding.executePendingBindings()
//            }





            recommendViewModel.coroutineScope.launch {

                shop.recommend =recommendViewModel.getCommentsByShop(shop)


                binding.recommend.text="${shop.recommend} 則評論"
                binding.executePendingBindings()
            }

            recommendViewModel.coroutineScope.launch {

                shop.star =recommendViewModel.getRatingByShop(shop)


                binding.star.text="${shop.star} 顆星"
                binding.executePendingBindings()
            }












            binding.distance.text = "${k}公尺"

            binding.shopItem = shop


            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
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

        return MarsPropertyViewHolder(ItemShopBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: MarsPropertyViewHolder, position: Int) {
        val marsProperty = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(marsProperty)
        }

        holder.bind(marsProperty, recommendViewModel)
    }

    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }


}
