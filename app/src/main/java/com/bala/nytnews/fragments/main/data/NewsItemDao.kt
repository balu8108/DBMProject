package com.bala.nytnews.fragments.main.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(newsItems: List<NewsItem>)

    @Query("SELECT * FROM newsitem")
    fun getAllNewsItems(): PagingSource<Int, NewsItem>

    @Query("DELETE FROM newsitem")
    suspend fun clearAllNewsItems()

    @Query("UPDATE newsitem SET favorite = :isFavorite WHERE id = :id")
    suspend fun updateNewsItem(id: Long, isFavorite: Boolean)
}