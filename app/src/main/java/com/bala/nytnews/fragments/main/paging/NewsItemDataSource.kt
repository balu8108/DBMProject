package com.bala.nytnews.fragments.main.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bala.nytnews.fragments.main.apiservice.ApiService
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bumptech.glide.load.HttpException
import java.io.IOException

class NewsItemDataSource : PagingSource<Int, NewsItem>() {

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val lPageIndex = params.key ?: NEWS_ITEM_FIRST_PAGE
        return try {
            val lNewsItems = ApiService.getNewsItemsInPage(lPageIndex)
            val lNextKey =
                if (lNewsItems.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3
                    // ensure we're not requesting duplicating items at the 2nd request
                    lPageIndex + (params.loadSize / 3)
                }
            LoadResult.Page(
                data = lNewsItems,
                prevKey = if (lPageIndex == NEWS_ITEM_FIRST_PAGE) null else lPageIndex,
                nextKey = lNextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object
    {
        private const val NEWS_ITEM_FIRST_PAGE = 1
    }
}