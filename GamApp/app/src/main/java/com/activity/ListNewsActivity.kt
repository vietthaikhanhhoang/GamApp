package com.activity

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.khaolok.myloadmoreitem.ItemsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit

class ListNewsActivity : AppCompatActivity() {
    var arrNews = JSONArray()
    lateinit var mAdManagerAdView : AdManagerAdView
    lateinit var adapterLinear: ItemsAdapter
    lateinit var rclView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        mAdManagerAdView = findViewById<AdManagerAdView>(R.id.adManagerAdView)
        //refreshData()

        var itemsCells: ArrayList<String?>
        itemsCells = ArrayList()
        for (i in 0..40) {
            itemsCells.add("Item $i")
        }

        rclView = findViewById<RecyclerView>(R.id.rclView)
        adapterLinear = ItemsAdapter(itemsCells!!, this, this.mAdManagerAdView)
        rclView.adapter = adapterLinear

        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rclView.layoutManager = mLayoutManager
    }

//    fun refreshData() {
//        // Create Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.appnews24h.com")
//            .build()
//
//        // Create Service
//        val service = retrofit.create(APIService::class.java)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            // Do the POST request and get response
////            val response = service.createEmployee(getHeaderMap(), requestBody)
//            val response = service.getListNews("999","0", 0, Global.getHeaderMap())
//
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//
//                    // Convert raw JSON to pretty JSON using GSON library
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                            JsonParser.parseString(
//                                    response.body()
//                                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                            )
//                    )
//
//                    Log.d("Pretty Printed JSON :", prettyJson)
//                    val jsonObject = JSONObject(prettyJson)
//                    if(jsonObject != null)
//                    {
//                        if (jsonObject.get("code").toString() == "200")
//                        {
//                            //Log.d("Pretty Printed JSON :", "hay vao day")
//                            if (jsonObject.getJSONArray("linfos") != null)
//                            {
//                                val linfos = jsonObject.getJSONArray("linfos")
////                                if(linfos.length() > 0)
////                                {
////                                    val infos = JSONObject(linfos[0].toString())
////                                    val doc = JSONObject(infos.get("Doc").toString())
////                                    val art = JSONObject(doc.get("Art").toString())
////
////                                    Log.d("tag 2", "so phan tu ${linfos.length()}")
////                                    Log.d("tag 2", "title: ${art.getString("title")}")
////                                }
//
//                                arrNews = JSONArray()
//                                for (i in 0 until linfos.length()) {
//                                    arrNews.put(linfos.get(i))
//                                }
//
//                                Log.d("vietnb", "gia tri len am am")
//                                Log.d("vietnb", arrNews.length().toString())
//                            }
//                        }
//                    }
//
//                } else {
//                    Log.e("RETROFIT_ERROR", response.code().toString())
//                }
//            }
//        }
//    }

    fun loadAds()
    {
        Log.d("tag1", "goi ham lay quang cao")
        mAdManagerAdView = findViewById<AdManagerAdView>(R.id.adManagerAdView)
        val adRequest = AdManagerAdRequest.Builder().build()
        mAdManagerAdView.loadAd(adRequest)

        mAdManagerAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("tag1", "goi quang cao finish")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("tag1", "goi quang cao loi")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }
}