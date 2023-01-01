package com.bala.nytnews.fragments.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(@PrimaryKey val repoId: Long, val prevKey: Int?, val nextKey: Int?)
