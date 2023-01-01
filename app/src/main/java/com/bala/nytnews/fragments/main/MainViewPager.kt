package com.bala.nytnews.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.bala.nytnews.R
import com.bala.nytnews.fragments.main.adapters.LoaderStateAdapter
import com.bala.nytnews.fragments.main.adapters.NewsListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.bala.nytnews.databinding.ViewPagerLayoutBinding

class MainViewPager : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var newsListAdapter: NewsListAdapter

    private val viewBinding
        get() = _viewBinding!!
    private var _viewBinding: ViewPagerLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = ViewPagerLayoutBinding.inflate(inflater)
        return viewBinding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() {
        newsListAdapter = NewsListAdapter(requireContext(), ({ newsItem ->
            findNavController().navigate(R.id.webViewFragment, bundleOf("newsItem" to newsItem))
        })) { id, isFavorite ->
            viewModel.updateNewsItem(id, isFavorite)
        }
        viewBinding.newsList.adapter = newsListAdapter.withLoadStateFooter(LoaderStateAdapter {})
    }

    @ExperimentalPagingApi
    private fun initObservers() {
        val isFavourite = arguments?.getBoolean("isFavorite")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNewsItems().collectLatest { lNewsItems ->
                if (isFavourite == true) {
                    newsListAdapter.submitData(lNewsItems.filter {
                        isFavourite == true && it.favorite
                    })
                } else {
                    newsListAdapter.submitData(lNewsItems)
                }
            }
        }

        viewBinding.newsList.layoutManager = GridLayoutManager(context, 2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}