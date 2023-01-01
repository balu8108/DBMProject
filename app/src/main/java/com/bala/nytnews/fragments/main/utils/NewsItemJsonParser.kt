package com.bala.nytnews.fragments.main.utils

import com.bala.nytnews.fragments.main.data.NewsItem
import org.json.JSONObject

object NewsItemJsonParser {

    fun parseNewsItem(jsonObject: JSONObject): NewsItem {
        val lTitle = jsonObject.getString("title")
        val lId = jsonObject.getLong("id")
        val lThumbnailUrl = jsonObject.getString("thumbnailUrl")+".jpg"
        val lUrl = jsonObject.getString("url")+".jpg"

        return NewsItem(lTitle, lThumbnailUrl, lUrl, lId)
    }
}