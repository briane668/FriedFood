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
import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemShopBinding
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class ShopAdapter(private val onClickListener: OnClickListener,
                  private val recommendViewModel: RecommendViewModel)
    : ListAdapter<Shop, ShopAdapter.ShopViewHolder>(DiffCallback) {

    class ShopViewHolder(var binding: ItemShopBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(shop: Shop,recommendViewModel: RecommendViewModel) {


            val x = MapViewModel.userPosition.value?.latitude
            val y = MapViewModel.userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude
            val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
            val distance = m.roundToInt()

            binding.distance.text = "${distance}公尺"


            recommendViewModel.coroutineScope.launch {
                shop.recommend =recommendViewModel.shopAddComments(shop)
                binding.recommend.text="${shop.recommend} 則評論"
                binding.executePendingBindings()
            }

            recommendViewModel.coroutineScope.launch {
                shop.star =recommendViewModel.shopAddRating(shop)
                binding.star.text="${shop.star} 顆星"
                binding.executePendingBindings()
            }


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
                                    viewType: Int): ShopViewHolder {

        return ShopViewHolder(ItemShopBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(shop)
        }

        holder.bind(shop, recommendViewModel)
    }

    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }


}
