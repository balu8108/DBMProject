package com.bala.nytnews.fragments.main.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class NewsItem(
    val title: String,
    val thumbnailUrl: String,
    val url: String,
    @PrimaryKey
    val id: Long,
    val favorite: Boolean = false
):Parcelable
