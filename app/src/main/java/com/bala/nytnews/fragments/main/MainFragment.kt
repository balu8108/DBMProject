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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bala.nytnews.R
import com.bala.nytnews.databinding.MainFragmentBinding
import com.bala.nytnews.fragments.main.adapters.LoaderStateAdapter
import com.bala.nytnews.fragments.main.adapters.NewsListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var newsListAdapter: NewsListAdapter

    private val viewBinding
        get() = _viewBinding!!
    private var _viewBinding: MainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = MainFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() {
        newsListAdapter = NewsListAdapter(requireContext(),({ url ->
            findNavController().navigate(R.id.webViewFragment, bundleOf("url" to url))
        })){id, isFavorite ->
            viewModel.updateNewsItem(id,isFavorite)
        }
        viewBinding.newsList.adapter = newsListAdapter.withLoadStateFooter(LoaderStateAdapter {})
    }

    @ExperimentalPagingApi
    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNewsItems().collectLatest { lNewsItems ->
                newsListAdapter.submitData(lNewsItems)
            }
        }

        viewBinding.newsList.layoutManager = GridLayoutManager(context, 2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}