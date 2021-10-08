package com.fragmentcustom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.customadapter.ball.ListTitleGuessMatchAdapter
import com.fragula.extensions.addFragment
import com.fragula.extensions.replaceFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.khaolok.myloadmoreitem.OnLoadMoreListener
import com.khaolok.myloadmoreitem.RecyclerViewLoadMoreScroll
import com.testfragment.Test3Fragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import model.*
import okhttp3.OkHttpClient
import okhttp3.Request

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var category: String? = null
    private var isBall24H: Boolean? = null

    var listNewsAdapter = ListNewsAdapter(JSONArray())
    lateinit var rclView: RecyclerView
    val arrNews = JSONArray()

    var sid:Int? = null

    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            category = it.getString(ARG_PARAM3)
            isBall24H = it.getBoolean(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rclView = view.findViewById(R.id.rclView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(refreshListener)

        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = mLayoutManager

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        rclView.addOnScrollListener(scrollListener)

        refreshData()

        return view
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefreshLayout.isRefreshing = true
        // call api to reload the screen
        refreshData()
    }

    fun refreshData() {
        var retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null

        var categoryname = ""
        if(isBall24H == false) {
//        Log.d("vietnb", "khi vao day kiem tra cate:" + category.toString())
            var cid:String = "999"
            val categoryJSON = JSONObject(category)
            if(categoryJSON.has("id")) {
                cid = categoryJSON.getString("id")
            }
            if(categoryJSON.has("category")) {
                categoryname = categoryJSON.getString("name")
            }

            if(!param1.isNullOrEmpty()) {
                sid = param1!!.toInt()
            }

            response = retrofit.getListNews(sid, cid,"0", 0, Global.getHeaderMap())
        } else {
            response = retrofit.getBallNews(null, null, Global.getHeaderMap())
        }

        response!!.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    val jsonObject = JSONObject(prettyJson)
                    if(jsonObject != null)
                    {
                        if (jsonObject.get("code").toString() == "200")
                        {
                            //Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.getJSONArray("linfos") != null)
                            {
                                val linfos = jsonObject.getJSONArray("linfos")
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    if(infos.has("type")) {
                                        val type = infos.getInt("type")
                                        if(type == 1) { //dang bai bao art
                                            arrNews.put(infos)
                                        }
                                    }
                                }
                                listNewsAdapter = ListNewsAdapter(arrNews)
                                rclView.adapter = listNewsAdapter
                                listNewsAdapter.setListNewsAdapterListener(object : ListNewsAdapter.ListNewsAdapterListener{
                                    override fun click_ListNewsAdapterListener(position: Int) {
                                        val jObject = JSONObject(arrNews[position].toString())
                                        val doc = jObject.getJSONObject("Doc")
                                        val art = doc.getJSONObject("Art")

                                        addFragment<DetailNewsFragment> {
                                            ARG_PARAM1 to ""
                                            ARG_PARAM2 to ""
                                            ARG_PARAM3 to art.toString()
                                            ARG_PARAM4 to categoryname
                                        }
                                    }
                                })
                            }
                        }
                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("vietnb", "co loi xay ra")
                swipeRefreshLayout.isRefreshing = false
            }

        })
    }

    private fun LoadMoreData() {

        Log.d("tag2", "tien hanh loadmore")
        val jObject = JSONObject(arrNews[arrNews.length() - 1].toString())
        val doc = jObject.getJSONObject("Doc")
        val art = doc.getJSONObject("Art")
        val lid = art.getString("lid")
        listNewsAdapter.addLoadingView()

        val retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null
        var categoryname = ""
        if(isBall24H == false) {
            var cid:String = "999"
            val categoryJSON = JSONObject(category)
            if(categoryJSON.has("id")) {
                cid = categoryJSON.getString("id")
            }

            response = retrofit.getListNews(sid, cid, lid, arrNews.length(), Global.getHeaderMap())
        } else {
            response = retrofit.getBallNews(lid, arrNews.length(), Global.getHeaderMap())
        }

        response!!.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("vietnb", "successful")
                listNewsAdapter.removeLoadingView()

                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    val jsonObject = JSONObject(prettyJson)
                    if(jsonObject != null)
                    {
                        if (jsonObject.get("code").toString() == "200")
                        {
                            //Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.has("linfos") != null)
                            {
                                val linfos = jsonObject.getJSONArray("linfos")

                                val positionStart = arrNews.length()
                                val arrNewsMore = JSONArray()
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    if(infos.has("type")) {
                                        val type = infos.getInt("type")
                                        if(type == 1) { //dang bai bao art
                                            arrNews.put(infos)
                                        }
                                    }
                                }

                                listNewsAdapter.addData(arrNewsMore)
                                scrollListener.setLoaded()
                                listNewsAdapter.notifyItemRangeInserted(positionStart, arrNews.length())
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("vietnb", "error")
                listNewsAdapter.removeLoadingView()
                scrollListener.setLoaded()
                Toast.makeText(context, "Có sự cố. Vui lòng thử lại", Toast.LENGTH_SHORT)
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, category: String, isBall: Boolean) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, category)
                    putBoolean(ARG_PARAM4, isBall)
                }
            }
    }

    ///protobuf
//    fun refreshDataProtobuf() {
//        var retrofit: APIService = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("https://api.appnews24h.com")
//            .build()
//            .create(APIService::class.java)
//
//        var response: Call<ResponseBody>? = null
//        response = retrofit.getListGuessMatch(Global.getHeaderMap())
//
//        response!!.enqueue(object : retrofit2.Callback<ResponseBody>{
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//
//                    if(response.body() != null) {
//                        val responseBody = response.body()
//                        if (responseBody != null) {
//                            model.PGame.GListFootBallMatchs.parseFrom(responseBody.byteStream())
//                            return@map Person.parseFrom(responseBody.byteStream())
//                        }
//
//                        //val personList = model.PGame.GListFootBallMatchs.parseFrom(response.body().byteStream())//model..parseFrom(response.body().byteStream())
//                        val a = model.PGame.GListFootBallMatchs.parseFrom(response.body()!!.bytes()!!)
//                    }
//
////                    val gson = GsonBuilder().setPrettyPrinting().create()
////                    val prettyJson = gson.toJson(
////                        JsonParser.parseString(
////                            response.body()
////                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
////                        )
////                    )
////
////                    val jsonObject = JSONObject(prettyJson)
////                    if(jsonObject != null)
////                    {
////                        if (jsonObject.get("code").toString() == "200")
////                        {
////                            //Log.d("Pretty Printed JSON :", "hay vao day")
////                            if (jsonObject.getJSONArray("football_match") != null)
////                            {
////                                val linfos = jsonObject.getJSONArray("football_match")
////                                for (i in 0 until linfos.length()) {
////                                    val infos = JSONObject(linfos[i].toString())
////                                    arrNews.put(infos)
////                                }
////                                listTitleGuessMatchAdapter = ListTitleGuessMatchAdapter(arrNews)
////                                rclView.adapter = listTitleGuessMatchAdapter
////                            }
////                        }
////                    }
//
//                } else {
//                    Log.e("RETROFIT_ERROR", response.code().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                Log.d("vietnb", "co loi xay ra")
//            }
//
//        })
//    }

    fun refreshDataProtobuf(){

    }

    private val okHttpClient = OkHttpClient()

    private val observable = Observable.just("http://elyeproject.x10host.com/experiment/protobuf")
        .map{
            val request = Request.Builder().url(it).build()
            val call = okHttpClient.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                val responseBody = response.body
                if (responseBody != null) {
//                    model.PWhatnews.whatNewsMsg.parseFrom(responseBody.byteStream())
//                    model.PWhatnews.whatNewsResponse.parseFrom(responseBody.byteStream())
//                    return@map model.PWhatnews.whatNewsMsg.parseFrom(responseBody.byteStream())
                }
            }
            return@map null

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}