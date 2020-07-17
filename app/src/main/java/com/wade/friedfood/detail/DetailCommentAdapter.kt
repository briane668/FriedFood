package com.wade.friedfood.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.wade.friedfood.data.Comment
import com.wade.friedfood.databinding.ItemDetailCommentBinding

class DetailCommentAdapter (): ListAdapter<Comment, DetailCommentAdapter.DetailCommentLayoutViewHolder>(DiffCallback) {

    class DetailCommentLayoutViewHolder(var binding: ItemDetailCommentBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.review = comment
            binding.executePendingBindings()
        }
    }



    companion object DiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCommentLayoutViewHolder {
        return DetailCommentLayoutViewHolder(ItemDetailCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DetailCommentLayoutViewHolder, position: Int) {
        val review = getItem(position)


        if (review.comment != ""){
            holder.binding.commentComment.text = review.comment
        }
        holder.bind(review)
    }
}

