package com.bala.nytnews.fragments.main.apiservice

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bala.nytnews.NytApplication
import com.bala.nytnews.fragments.main.data.NewsItem
import com.bala.nytnews.fragments.main.utils.NewsItemJsonParser
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ApiService {

    private val requestQueue by lazy {
        Volley.newRequestQueue(
            NytApplication.application,
            HurlStack()
        )
    }

    suspend fun getNewsItemsInPage(
        page: Int
    ): List<NewsItem> = suspendCoroutine { cont ->

        val lUrl =
            "https://jsonplaceholder.typicode.com/photos?albumId=$page"
        val lNewsItemRequest = StringRequest(
            Request.Method.GET, lUrl,
            { response ->
                val lJsonArray = JSONArray(response)
                val lNewsItems = mutableListOf<NewsItem>()
                for (i in 0 until lJsonArray.length()) {
                    val lJsonObject = lJsonArray.getJSONObject(i)
                    val lNewsItem = NewsItemJsonParser.parseNewsItem(lJsonObject)
                    lNewsItems.add(lNewsItem)
                }
                cont.resume(lNewsItems)
            },
            {
                // Handle error
            })

        Log.d("balauri", "getNewsItemsInPage: ${lNewsItemRequest.url}")

        requestQueue.add(lNewsItemRequest)
    }

}