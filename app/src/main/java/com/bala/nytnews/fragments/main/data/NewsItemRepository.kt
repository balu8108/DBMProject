package com.bala.nytnews.fragments.main.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.bala.nytnews.AppDatabase
import com.bala.nytnews.fragments.main.paging.NewsItemDataSource
import com.bala.nytnews.fragments.main.paging.NewsItemMediator
import kotlinx.coroutines.flow.Flow

/**
 * repository class to manage the data flow and map it if needed
 */
@ExperimentalPagingApi
class NewsItemRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 9

        fun getInstance() = NewsItemRepository()
    }


    fun letNewsItemsFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<NewsItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NewsItemDataSource() }
        ).flow
    }


    fun letNewsItemsLiveData(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<NewsItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { NewsItemDataSource() }
        ).liveData
    }


    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun letNewsItemsDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<NewsItem>> {

        val lPagingSourceConfig = { AppDatabase.getInstance().getNewsItemDao().getAllNewsItems() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = lPagingSourceConfig,
            remoteMediator = NewsItemMediator()
        ).flow
    }

    suspend fun updateNewsItem(id: Long, isFavorite: Boolean) {
        AppDatabase.getInstance().getNewsItemDao().updateNewsItem(id, isFavorite)
    }

}