package com.bala.nytnews.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bala.nytnews.fragments.main.data.NewsItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    @ExperimentalPagingApi
    fun getNewsItems(isFavorite: Boolean): Flow<PagingData<NewsItem>> {
        if (isFavorite)
            return NewsItemRepository.getInstance().letFavoritesDb().cachedIn(viewModelScope)
        return NewsItemRepository.getInstance().letNewsItemsDb().cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun updateNewsItem(id:Long, isFavorite:Boolean){
        viewModelScope.launch {
            NewsItemRepository.getInstance().updateNewsItem(id,isFavorite)
        }
    }
}