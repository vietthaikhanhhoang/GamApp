package com.fragmentcustom.football

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ball.ListGuessMatchAdapter
import com.customadapter.ball.ListTitleGuessMatchAdapter
import com.customview.ball.GTQTLSView
import com.fragula.extensions.addFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.khaolok.myloadmoreitem.OnLoadMoreListener
import com.khaolok.myloadmoreitem.RecyclerViewLoadMoreScroll
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

/**
 * A simple [Fragment] subclass.
 * Use the [GuessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuessFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var listTitleGuessMatchAdapter = ListTitleGuessMatchAdapter(JSONArray())
    lateinit var rclView: RecyclerView
    val arrNews = JSONArray()

    lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_guess, container, false)

        val gtqtlsView = view.findViewById<GTQTLSView>(R.id.gtqtlsView)
        gtqtlsView.setGTQTLSViewListener(object : GTQTLSView.GTQTLSViewListener {
            override fun click_GTQTLSViewListener(position: Int) {
                if(position == 1) {
                    addFragment<GameLuckyFragment> {
                        param1 to ""
                        param2 to ""
                    }
                }
            }
        })

        rclView = view.findViewById(R.id.rclView)

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

    fun refreshData() {
        var retrofit: APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null
        response = retrofit.getListGuessMatch(Global.getHeaderMap())

        response!!.enqueue(object : retrofit2.Callback<ResponseBody>{
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
                            //Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.getJSONArray("football_match") != null)
                            {
                                val linfos = jsonObject.getJSONArray("football_match")
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    arrNews.put(infos)
                                }
                                listTitleGuessMatchAdapter = ListTitleGuessMatchAdapter(arrNews)
                                rclView.adapter = listTitleGuessMatchAdapter
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

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GuessFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GuessFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}