package com.wade.friedfood.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wade.friedfood.data.Comment
import com.wade.friedfood.databinding.ItemDetailCommentBinding
import com.wade.friedfood.databinding.ItemDetailImageBinding
import com.wade.friedfood.detail.DetailCommentAdapter
import com.wade.friedfood.detail.DetailViewModel
import kotlinx.coroutines.launch

class NewsAdapter (private val newsViewModel: NewsViewModel): ListAdapter<Comment,
        NewsAdapter.DetailCommentLayoutViewHolder>(DiffCallback) {

    class DetailCommentLayoutViewHolder(var binding: ItemDetailCommentBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment,newsViewModel: NewsViewModel) {
            binding.review = comment
            if (comment.image == ""){
                binding.commentImage.visibility=View.GONE
            }

            newsViewModel.coroutineScope.launch {

                val counts =newsViewModel.getUserCommentsCount(comment.user_id)


                binding.commentCounts.text="已發表過 $counts 則評論"
                binding.executePendingBindings()
            }


            binding.commentImage.visibility
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
        holder.bind(review,newsViewModel)
    }
}