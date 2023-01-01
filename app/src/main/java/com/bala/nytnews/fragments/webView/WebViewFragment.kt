package com.bala.nytnews.fragments.webView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bala.nytnews.R
import com.bala.nytnews.databinding.WebViewLayoutBinding
import com.bala.nytnews.fragments.main.MainViewModel
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bumptech.glide.Glide

class WebViewFragment : Fragment() {

    private val viewBinding
        get() = _viewBinding!!
    private var _viewBinding: WebViewLayoutBinding? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = WebViewLayoutBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsItem = arguments?.getParcelable("newsItem") as NewsItem?
        newsItem?.let {
            context?.let { it1 ->
                Glide.with(it1)
                    .load(newsItem.url)
                    .placeholder(R.drawable.news_item_place_holder)
                    .into(viewBinding.titleImage)
            }
            viewBinding.title.text = newsItem.title
            viewBinding.snippet.isVisible = !newsItem.favorite
            viewBinding.date.isVisible = newsItem.favorite
        }

        viewBinding.snippet.setOnClickListener {
            if (newsItem != null) {
                viewModel.updateNewsItem(newsItem.id,true)
            }
            viewBinding.snippet.isVisible = false
            viewBinding.date.isVisible = true
        }

        viewBinding.date.setOnClickListener {
            if (newsItem != null) {
                viewModel.updateNewsItem(newsItem.id,false)
            }
            viewBinding.snippet.isVisible = true
            viewBinding.date.isVisible = false
        }

        viewBinding.toolBar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

}