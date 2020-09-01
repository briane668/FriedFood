package com.wade.friedfood.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Shop
import com.wade.friedfood.databinding.ItemDetailCommentBinding
import com.wade.friedfood.databinding.ItemDetailImageBinding
import com.wade.friedfood.databinding.ItemNewsBinding
import com.wade.friedfood.detail.DetailCommentAdapter
import com.wade.friedfood.detail.DetailViewModel
import com.wade.friedfood.map.MapAdapter
import kotlinx.coroutines.launch

class NewsAdapter(
    private val onClickListener: NewsAdapter.OnClickListener,
    private val newsViewModel: NewsViewModel

) : ListAdapter<Comment,
        NewsAdapter.DetailCommentLayoutViewHolder>(DiffCallback) {

    class DetailCommentLayoutViewHolder(var binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment, newsViewModel: NewsViewModel) {
            binding.review = comment


            newsViewModel.coroutineScope.launch {
                val counts = newsViewModel.getUserCommentsCount(comment.user_id)
                binding.commentCounts.text = "已發表過 $counts 則評論"
                binding.executePendingBindings()
            }
            newsViewModel.coroutineScope.launch {
                val rating = comment.shop?.let { newsViewModel.getRatingByShop(it) }
                if (rating != null) {
                    binding.ratingBar.rating = rating.toFloat()
                }

                binding.executePendingBindings()
            }

            newsViewModel.coroutineScope.launch {
                val commentCount = comment.shop?.let { newsViewModel.getCommentsByShop(it) }
                binding.commentCount.text = "$commentCount 則評論"
                binding.executePendingBindings()
            }






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


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailCommentLayoutViewHolder {
        return DetailCommentLayoutViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailCommentLayoutViewHolder, position: Int) {
        val review = getItem(position)


        holder.itemView.setOnClickListener {
            onClickListener.onClick(review)
        }

        holder.bind(review, newsViewModel)
    }

    class OnClickListener(val clickListener: (comment: Comment) -> Unit) {
        fun onClick(comment: Comment) = clickListener(comment)
    }


}