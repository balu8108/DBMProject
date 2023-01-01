package com.bala.nytnews

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bala.nytnews.fragments.main.data.NewsItemDao
import com.bala.nytnews.fragments.main.data.RemoteKeys
import com.bala.nytnews.fragments.main.data.RemoteKeysDao


@Database(version = 1, entities = [NewsItem::class, RemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getNewsItemDao(): NewsItemDao

    companion object {

        val NYT_DB = "nyt.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(NytApplication.application).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NYT_DB)
                .build()
    }

}