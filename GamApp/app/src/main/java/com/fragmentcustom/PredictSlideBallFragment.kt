package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.LoopBallAdapter
import com.github.infinitebanner.InfiniteBannerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.lib.toPx
import com.rd.PageIndicatorView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PredictSlideBallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PredictSlideBallFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var infiBannerView: InfiniteBannerView
    lateinit var loopBallAdapter: LoopBallAdapter
    var mList = JSONArray()

    lateinit var pageIndicatorView: PageIndicatorView
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

        val view = inflater.inflate(R.layout.fragment_predict_slide_ball, container, false)
        infiBannerView = view.findViewById(R.id.infiBannerView)
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView)

        loopBallAdapter = LoopBallAdapter(mList)
        infiBannerView.adapter = loopBallAdapter
        loopBallAdapter.setLoopBallAdapterListener(object : LoopBallAdapter.LoopBallAdapterListener{
            override fun clickJoinPredict_LoopBallAdapterListener(position: Int) {
                val currentPosition = infiBannerView.currentPosition
                loopBallAdapter.notifyDataSetChanged()
                infiBannerView.setInitPosition(currentPosition)
            }
        })

        infiBannerView.setPageTransformer(InfiniteBannerView.PageTransformer { view, offset ->
            view.scaleY = 0.8f + 0.2f * offset
            view.alpha = 0.5f + 0.5f * offset
        })

        infiBannerView.addOnPageChangeListener(object :
            InfiniteBannerView.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                pageIndicatorView.selection = position
            }
        })

        refreshData()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PredictSlideBallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PredictSlideBallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun refreshData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.appnews24h.com")
            .build()

        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getSlideMatch(Global.getHeaderMap())

            withContext(Dispatchers.Main) {
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
                            if(jsonObject.has("doc")) {
                                val doc = JSONObject(jsonObject.getString("doc"))
                                if(doc.has("type") && doc.has("Doc")) {
                                    val type = doc.getInt("type")
                                    val Doc = JSONObject(doc.getString("Doc"))
                                    if(type == 17) {
                                        Log.d("vietnb", "zzzz")
                                        if(Doc.has("MatchInfo")) {
                                            val MatchInfo = JSONObject(Doc.getString("MatchInfo"))
                                            Log.d("vietnb", "yyyyy")
                                            if(MatchInfo.has("matchinfo")) {
                                                val matchinfo = MatchInfo.getJSONArray("matchinfo")
                                                for (i in 0 until matchinfo.length()) {
                                                    Log.d("vietnb", "mmmm")
                                                    mList.put(matchinfo[i])
                                                }
                                            }
                                        }
                                    }
                                }
                            }

//                            val loopBallAdapter = LoopBallAdapter(mList)
//                            rclView.adapter = loopBallAdapter
                            pageIndicatorView.count = mList.length()
                            loopBallAdapter.notifyDataSetChanged()
                        }
                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
}