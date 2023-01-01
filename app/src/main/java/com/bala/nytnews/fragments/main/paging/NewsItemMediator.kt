package com.bala.nytnews.fragments.main.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bala.nytnews.AppDatabase
import com.bala.nytnews.fragments.main.apiservice.ApiService
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bala.nytnews.fragments.main.data.RemoteKeys
import java.io.IOException


@ExperimentalPagingApi
class NewsItemMediator(private val appDatabase: AppDatabase = AppDatabase.getInstance()) :
    RemoteMediator<Int, NewsItem>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, NewsItem>
    ): MediatorResult {

        val lPage = when (val lPageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return lPageKeyData
            }
            else -> {
                lPageKeyData as Int
            }
        }

        try {
            val lResponse = ApiService.getNewsItemsInPage(lPage)
            val lIsEndOfList = lResponse.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    //appDatabase.getNewsItemDao().clearAllNewsItems()
                }
                val lPrevKey = if (lPage == DEFAULT_PAGE_INDEX) null else lPage - 1
                val lNextKey = if (lIsEndOfList) null else lPage + 1
                val lKeys = lResponse.map {
                    RemoteKeys(repoId = it.id, prevKey = lPrevKey, nextKey = lNextKey)
                }
                appDatabase.getRepoDao().insertAll(lKeys)
                appDatabase.getNewsItemDao().insertAll(lResponse)
            }
            return MediatorResult.Success(endOfPaginationReached = lIsEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, NewsItem>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val lRemoteKeys = getClosestRemoteKey(state)
                lRemoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val lRemoteKeys = getLastRemoteKey(state)
                    ?: return DEFAULT_PAGE_INDEX
                lRemoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val lRemoteKeys = getFirstRemoteKey(state)
                    ?: return DEFAULT_PAGE_INDEX
                //end of list condition reached
                lRemoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                lRemoteKeys.prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, NewsItem>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { lNewsItem -> appDatabase.getRepoDao().remoteKeysNewsItemId(lNewsItem.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, NewsItem>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { lNewsItem ->
                appDatabase.getRepoDao().remoteKeysNewsItemId(lNewsItem.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, NewsItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysNewsItemId(repoId)
            }
        }
    }

    companion object
    {
        const val DEFAULT_PAGE_INDEX = 1
    }

}