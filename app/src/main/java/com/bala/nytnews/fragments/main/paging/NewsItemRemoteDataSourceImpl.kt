package com.bala.nytnews.fragments.main.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bala.nytnews.fragments.main.data.NewsItem
import kotlinx.coroutines.flow.Flow

object NewsItemRemoteDataSourceImpl {
    private const val NETWORK_PAGE_SIZE = 3

    fun getNewsItems(): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsItemDataSource()
            }
        ).flow
    }
}