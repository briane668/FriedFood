package com.wade.friedfood.map


import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wade.friedfood.GetDistance
import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemMapBinding
import com.wade.friedfood.databinding.ItemShopBinding
import com.wade.friedfood.getDistance
import com.wade.friedfood.map.MapViewModel.Companion.userPosition
import kotlin.math.pow
import kotlin.math.roundToInt


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class MapAdapter(private val onClickListener: OnClickListener) : ListAdapter<Shop, MapAdapter.MarsPropertyViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class MarsPropertyViewHolder( var binding: ItemMapBinding):
        RecyclerView.ViewHolder(binding.root) {



        fun bind(shop: Shop) {

            val x = userPosition.value?.latitude
            val y = userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude

//            val g = (s?.let {
//                y?.minus(it) })?.pow(2)?.let {
//                    it ->
//                (r?.let {
//                    x?.minus(it) })?.pow(2)?.plus(it)
//            }?.let {
//                it.pow(0.5) }

//var m= x?.let {
//    if (y != null) {
//        if (r != null) {
//            if (s != null) {
//                GetDistance(it,y,r,s)
//            }
//        }
//    }
//}
            val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
            val k = m.roundToInt()
            binding.mapItem = shop
            binding.star.text="${shop.star}顆星"
            binding.recommend.text="${shop.recommend}則評論"
            binding.distance.text = "${k}公尺"



//            binding.distance.text="${userPosition.value?.latitude?.toInt()?.let {
//                shop.location?.latitude?.toInt()
//                    ?.minus(it)
//            }}"
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
        
        
        holder.bind(marsProperty )
    }

    class OnClickListener(val clickListener: (shop: Shop) -> Unit) {
        fun onClick(shop: Shop) = clickListener(shop)
    }


}
