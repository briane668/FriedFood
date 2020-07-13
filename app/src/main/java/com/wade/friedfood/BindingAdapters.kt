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

package com.wade.friedfood

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wade.friedfood.data.Shop
import com.wade.friedfood.detail.DeatailAdapter
import com.wade.friedfood.map.MapAdapter
import com.wade.friedfood.map.MapViewModel

import com.wade.friedfood.recommend.ShopAdapter

/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("ShopData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Shop>?) {

    recyclerView.adapter.apply {
        when (this) {
            is ShopAdapter -> submitList(data)
            is MapAdapter -> submitList(data)
        }
    }
}

@BindingAdapter("imageData")
fun bindRecyclerViewImage(recyclerView: RecyclerView, data: List<String>?) {
    val adapter = recyclerView.adapter as DeatailAdapter
    adapter.submitList(data)
}


//    val shop = data!!.filter {
//        it.name == viewModel.selectedShop.value
////[shop]
//    }
//
////    創造一個list 去裝? 然後submitlist??bo
//    for (shop in data) {
//    if (shop.name == viewModel.selectedShop.value) {
//
//        val position = data!!.indexOf(shop)
//        viewModel.Position.value=position
//    }
//}
//    .sort


/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder))
                .into(imgView)
    }
}
