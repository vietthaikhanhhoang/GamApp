package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.fragula.extensions.addFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import data.DataPreference
import data.PREFERENCE
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class VideoCollectionAdapter(fragment: Fragment, var arrCategory: JSONArray) : FragmentStateAdapter(fragment) {
    fun refreshData(arrCategory: JSONArray){
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrCategory.length()

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = ListVideoFragment.newInstance("", "", arrCategory[position].toString())
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment
    }
}

class VideoPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewPager: ViewPager2
    lateinit var tab_layout: TabLayout

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
        val view = inflater.inflate(R.layout.fragment_video_pager, container, false)

        tab_layout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.viewPager)

        tab_layout.setTabTextColors(getResources().getColor(R.color.titlenewscolor, null), getResources().getColor(R.color.mainredcolor, null))
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mainredcolor, null))

        getCategoryVideo()

        return view
    }

    private fun getCategoryVideo() {

        val sharedPreference: DataPreference = DataPreference(requireContext())
        val arrayCategoryVideo = sharedPreference.getValueString(PREFERENCE.ARRAYCATEGORYVIDEO)

        var hasCategoryVideo:Boolean = false
        if(arrayCategoryVideo != null) {
            hasCategoryVideo = true
            val categories = JSONArray(arrayCategoryVideo)
            val videoCollectionAdapter = VideoCollectionAdapter(this@VideoPagerFragment, categories)
            viewPager.adapter = videoCollectionAdapter
            tab_layout.setupWithViewPager(viewPager, categories!!)
            viewPager.offscreenPageLimit = 1
        }

        val retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        val response = retrofit.getCategoryVideo(Global.getHeaderMap())
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
                    if (jsonObject.has("categories"))
                    {
                        val categories = jsonObject.getJSONArray("categories")
                        val sharedPreference: DataPreference = DataPreference(requireContext())
                        sharedPreference.save(PREFERENCE.ARRAYCATEGORYVIDEO, categories.toString())

                        if(!hasCategoryVideo) {
                            val videoCollectionAdapter = VideoCollectionAdapter(this@VideoPagerFragment, categories)
                            viewPager.adapter = videoCollectionAdapter
                            tab_layout.setupWithViewPager(viewPager, categories!!)
                            viewPager.offscreenPageLimit = 1
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPause() {
        super.onPause()
        Log.d("vietnb", "nhay vao pause fragment VideoPagerFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("vietnb", "nhay vao destroy fragment VideoPagerFragment")
    }
}