package com.wade.friedfood.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wade.friedfood.databinding.ItemDetailImageBinding


class DetailAdapter : ListAdapter<String, DetailAdapter.DetailViewHolder>(DiffCallback) {

    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class DetailViewHolder(private var binding: ItemDetailImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: String) {
            binding.detailimage = product
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                           viewType: Int): DetailViewHolder {
        return DetailViewHolder(ItemDetailImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val product = getItem(position)

        holder.bind(product)
    }

}