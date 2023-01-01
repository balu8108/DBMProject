package com.bala.nytnews.fragments.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bala.nytnews.R
import com.bala.nytnews.databinding.NewsItemLayoutBinding
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bumptech.glide.Glide

class NewsListAdapter(
    private val context: Context,
    val listener: (NewsItem) -> Unit,
    val favoriteListener: (Long, Boolean) -> Unit
) : PagingDataAdapter<NewsItem, RecyclerView.ViewHolder>(DiffCallBackNewsItems()) {

    inner class NewsItemViewHolder(val viewBinding: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.newItemContainer.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { lNewsItem -> listener(lNewsItem) }
            }

            viewBinding.snippet.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { lNewsItem -> favoriteListener(lNewsItem.id,true) }
            }

            viewBinding.date.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { lNewsItem -> favoriteListener(lNewsItem.id,false) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsItemViewHolder(
            NewsItemLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsItemViewHolder) {
            holder.viewBinding.apply {
                val lNewsItem = getItem(position)
                lNewsItem?.let {
                    holder.viewBinding.apply {
                        Glide.with(context)
                            .load(lNewsItem.thumbnailUrl)
                            .placeholder(R.drawable.news_item_place_holder)
                            .into(titleImage)
                        title.text = lNewsItem.title
                        snippet.isVisible = !lNewsItem.favorite
                        date.isVisible = lNewsItem.favorite
                    }
                }

            }
        }
    }


    class DiffCallBackNewsItems : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem) =
            oldItem == newItem
    }
}