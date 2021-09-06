package com.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.DemoRecyclerAdapter
import com.customadapter.ListNewsAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.khaolok.myloadmoreitem.OnLoadMoreListener
import com.khaolok.myloadmoreitem.RecyclerViewLoadMoreScroll
import com.test.DetailNewsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit

class DemoRecyclerActivity : AppCompatActivity() {

    var listNewsAdapter = DemoRecyclerAdapter(JSONArray())
    lateinit var rclView: RecyclerView
    val arrNews = JSONArray()

    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_recycler)

        rclView = findViewById(R.id.rclView)

        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rclView.layoutManager = mLayoutManager

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
            }
        })
        rclView.addOnScrollListener(scrollListener)

        //refreshData()
    }

//    fun refreshData() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.appnews24h.com")
//            .build()
//
//        val service = retrofit.create(APIService::class.java)
//
////        Log.d("vietnb", "khi vao day kiem tra cate:" + category.toString())
//        var cid:String = "999"
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.getListNews(cid,"0", 0, Global.getHeaderMap())
//
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                        )
//                    )
//
//                    val jsonObject = JSONObject(prettyJson)
//                    if(jsonObject != null)
//                    {
//                        if (jsonObject.get("code").toString() == "200")
//                        {
//                            //Log.d("Pretty Printed JSON :", "hay vao day")
//                            if (jsonObject.getJSONArray("linfos") != null)
//                            {
//                                val linfos = jsonObject.getJSONArray("linfos")
//                                for (i in 0 until linfos.length()) {
//                                    val infos = JSONObject(linfos[i].toString())
//                                    if(infos.has("Doc")) {
//                                        val doc = JSONObject(infos.get("Doc").toString())
//                                        if(doc.has("Art")) {
//                                            val art = JSONObject(doc.get("Art").toString())
////                                            Log.d("vietnb","art: " + art.toString())
//                                            arrNews.put(art)
//                                        }
//                                    }
//                                }
//                                listNewsAdapter = DemoRecyclerAdapter(arrNews)
//                                rclView.adapter = listNewsAdapter
//                                listNewsAdapter.setDemoRecyclerAdapterListener(object : DemoRecyclerAdapter.DemoRecyclerAdapterListener{
//                                    override fun click_DemoRecyclerAdapterListener(position: Int) {
//
//                                    }
//                                })
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
}