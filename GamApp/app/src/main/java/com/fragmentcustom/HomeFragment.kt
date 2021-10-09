package com.fragmentcustom
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.fragula.extensions.addFragment
import com.khaolok.myloadmoreitem.OnLoadMoreListener
import com.khaolok.myloadmoreitem.RecyclerViewLoadMoreScroll
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import model.*

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

    var listNewsAdapter = ListNewsAdapter(mutableListOf())

    lateinit var rclView: RecyclerView
    val arrNews = mutableListOf<model.PListingResponse.DocumentOrBuilder>()

    var sid: Int? = null

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
        var retrofit: APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null

        var categoryname = ""
        if (isBall24H == false) {
            Log.d("vietnb", "khi vao day kiem tra cate:" + category.toString())
            var cid: String = "999"
            val categoryJSON = JSONObject(category)
            if (categoryJSON.has("id")) {
                cid = categoryJSON.getString("id")
            }
            if (categoryJSON.has("category")) {
                categoryname = categoryJSON.getString("name")
            }

            if (!param1.isNullOrEmpty()) {
                sid = param1!!.toInt()
            }

            response = retrofit.getListNews(sid, cid, "0", 0, Global.getHeaderMap())
        } else {
            response = retrofit.getBallNews(null, null, Global.getHeaderMap())
        }

        response!!.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    if (responseBody != null) {
                        val listingResponse = PListingResponse.ListingResponses.parseFrom(responseBody.byteStream())

                        arrNews.addAll(listingResponse.linfosOrBuilderList)
                        listNewsAdapter = ListNewsAdapter(arrNews)
                        rclView.adapter = listNewsAdapter

                        listNewsAdapter.setListNewsAdapterListener(object :
                            ListNewsAdapter.ListNewsAdapterListener {
                                override fun click_ListNewsAdapterListener(position: Int) {
                                    val doc = arrNews.get(position)
                                    val art = doc.art

                                    addFragment<DetailNewsFragment> {
                                        ARG_PARAM1 to ""
                                        ARG_PARAM2 to ""
                                        ARG_PARAM3 to art.toByteArray()
                                        ARG_PARAM4 to categoryname
                                    }
                                }
                            })
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
        val doc = arrNews.get(arrNews.count() - 1)
        val art = doc.art
        val lid = art.lid
        listNewsAdapter.addLoadingView()

        var retrofit: APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null

        var categoryname = ""
        if (isBall24H == false) {
            Log.d("vietnb", "khi vao day kiem tra cate:" + category.toString())
            var cid: String = "999"
            val categoryJSON = JSONObject(category)
            if (categoryJSON.has("id")) {
                cid = categoryJSON.getString("id")
            }
            if (categoryJSON.has("category")) {
                categoryname = categoryJSON.getString("name")
            }

            if (!param1.isNullOrEmpty()) {
                sid = param1!!.toInt()
            }

            response = retrofit.getListNews(sid, cid, "0", 0, Global.getHeaderMap())
        } else {
            response = retrofit.getBallNews(null, null, Global.getHeaderMap())
        }

        response!!.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                listNewsAdapter.removeLoadingView()
                scrollListener.setLoaded()

                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    if (responseBody != null) {
                        val listingResponse = PListingResponse.ListingResponses.parseFrom(responseBody.byteStream())

                        val positionStart = arrNews.count()
                        arrNews.addAll(listingResponse.linfosOrBuilderList)

                        listNewsAdapter.notifyItemRangeInserted(
                            positionStart,
                            arrNews.count()
                        )
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("vietnb", "co loi xay ra")
                listNewsAdapter.removeLoadingView()
                scrollListener.setLoaded()
                swipeRefreshLayout.isRefreshing = false
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
}