package com.bala.nytnews.fragments.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsItem(
    val title: String,
    val thumbnailUrl: String,
    val url: String,
    @PrimaryKey
    val id: Long,
    val favorite: Boolean = false
)
