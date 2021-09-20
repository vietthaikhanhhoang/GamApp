package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ListVideoAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.khaolok.myloadmoreitem.OnLoadMoreListener
import com.khaolok.myloadmoreitem.RecyclerViewLoadMoreScroll
import com.khaolok.myloadmoreitem.RecyclerViewPositionHelper
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [ListVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListVideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var category: String? = null

    lateinit var rclView: RecyclerView
    lateinit var listVideoAdapter: ListVideoAdapter

    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    var arrVideos = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            category = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_video, container, false)
        rclView = view.findViewById(R.id.rclView)

        listVideoAdapter = ListVideoAdapter(requireContext(), arrVideos, rclView)
        rclView.adapter = listVideoAdapter

        listVideoAdapter.setListener(object : ListVideoAdapter.ListVideoAdapterListener {
            override fun clickListVideoAdapter(position: Int) {
//                Log.d("vietnb", "click vao thang : $position")
//
//                val positionAuto = RecyclerViewPositionHelper.createHelper(rclView).findFirstCompletelyVisibleItemPosition()
//                Log.d("vietnb", "thang tu dong hien thi: $positionAuto")
            }
        })

        val layoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = layoutManager

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        rclView.addOnScrollListener(scrollListener)

        refreshData()

//        val btnClick = Button(this.context)
//        btnClick.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//        btnClick.text = "Click"
//        btnClick.setBackgroundColor(R.color.browser_actions_bg_grey)
//        rclView.addView(btnClick)

        return view
    }

    fun refreshData() {
        val retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var subcid = 888
        if(category != null) {
            Log.d("vietnb", "kiem tra thang category cua video $category")
            val categoryJSON = JSONObject(category)
            if(categoryJSON.has("id")) {
                subcid = categoryJSON.getInt("id")
            }
        }

        val response = retrofit.getListVideos(subcid, null,null, Global.getHeaderMap())
        response.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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
                            Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.getJSONArray("linfos") != null)
                            {
                                val linfos = jsonObject.getJSONArray("linfos")
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    if(infos.has("Doc")) {
                                        val doc = JSONObject(infos.get("Doc").toString())
                                        if(doc.has("Video")) {
                                            val video = JSONObject(doc.get("Video").toString())
                                            //Log.d("vietnb", "video: " + video.toString())
                                            arrVideos.put(video)
                                        }
                                    }
                                }

                                listVideoAdapter.mList = arrVideos
                                listVideoAdapter.notifyDataSetChanged()
                            }
                        }
                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("vietnb", "co loi xay ra")
            }

        })
    }

    private fun LoadMoreData() {
        var lid: String = ""
        var realsize = arrVideos.length()

        val jVideo = arrVideos.getJSONObject(arrVideos.length() - 1)
        if(jVideo.has("lid")) {
            lid = jVideo.getString("lid")
        }

        listVideoAdapter.addLoadingView()

//        Log.d("vietnb", "vao khong nhi")
        val retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var subcid = 888
        if(category != null) {
            val categoryJSON = JSONObject(category)
            if(categoryJSON.has("id")) {
                subcid = categoryJSON.getInt("id")
            }
        }

        val response = retrofit.getListVideos(subcid, lid, realsize, Global.getHeaderMap())
        response.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("vietnb", "successful")
                listVideoAdapter.removeLoadingView()

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

                                val arrVideosMore = JSONArray()
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    if(infos.has("Doc")) {
                                        val doc = JSONObject(infos.get("Doc").toString())
                                        if(doc.has("Video")) {
                                            val art = JSONObject(doc.get("Video").toString())
                                            arrVideosMore.put(art)
                                            arrVideos.put(art)
                                        }
                                    }
                                }

                                listVideoAdapter.addData(arrVideosMore)
                                scrollListener.setLoaded()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("vietnb", "error")
                listVideoAdapter.removeLoadingView()
                scrollListener.setLoaded()
                Toast.makeText(context, "Có sự cố. Vui lòng thử lại", Toast.LENGTH_SHORT)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d("vietnb", "nhay vao start fragment ZZZZZZZ")
//        listVideoAdapter.playVideo()
    }

    override fun onStop() {
        super.onStop()
        Log.d("vietnb", "nhay vao stop fragment")
    }

    override fun onResume() {
        super.onResume()
        readyVideo(true)
    }

    override fun onPause() {
        super.onPause()
        Log.d("vietnb", "nhay vao pause fragment")
        readyVideo(false)
        pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("vietnb", "nhay vao destroy fragment")
        pauseVideo()
    }

    fun pauseVideo() {
        if(listVideoAdapter != null) {
            listVideoAdapter.pauseVideo()
        }
    }

    fun readyVideo(isReady: Boolean) {
        if(listVideoAdapter != null) {
            listVideoAdapter.readyVideo(isReady)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListVideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
            ListVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}