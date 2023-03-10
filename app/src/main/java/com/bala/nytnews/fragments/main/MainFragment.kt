package com.bala.nytnews.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bala.nytnews.R
import com.bala.nytnews.databinding.MainFragmentBinding
import com.bala.nytnews.fragments.main.adapters.NewsListAdapter
import com.google.android.material.tabs.TabLayoutMediator

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
    }

    private fun init() {
        newsListAdapter = NewsListAdapter(requireContext(), ({ newsItem ->
            findNavController().navigate(R.id.webViewFragment, bundleOf("newsItem" to newsItem))
        })) { id, isFavorite ->
            viewModel.updateNewsItem(id, isFavorite)
        }

        viewBinding.viewPager.adapter = MainFragmentViewPagerAdapter(requireActivity())
        TabLayoutMediator(viewBinding.tabs, viewBinding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "ALL"
                }
                1 -> {
                    tab.text = "FAVORITES"
                }
            }
        }.attach()
    }

    private inner class MainFragmentViewPagerAdapter(fm: FragmentActivity) :
        FragmentStateAdapter(fm) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return MainViewPager().apply {
                arguments = bundleOf("isFavorite" to (position == 1))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}