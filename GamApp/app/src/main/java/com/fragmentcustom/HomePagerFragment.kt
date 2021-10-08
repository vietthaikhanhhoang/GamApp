package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ball.ListTitleGuessMatchAdapter
import com.customview.BottomView
import com.fragula.extensions.addFragment
import com.fragula.listener.OnFragmentNavigatorListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
import java.util.prefs.Preferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeCollectionAdapter(fragment: Fragment, var arrCategory: JSONArray, var websiteID:String?) : FragmentStateAdapter(fragment) {
    fun refreshData(arrCategory: JSONArray){
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrCategory.length()

    override fun createFragment(position: Int): Fragment {

        // Return a NEW fragment instance in createFragment(int)

        var param1 = ""
        if(websiteID != null) {
            param1 = websiteID!!
        }
        var fragment = HomeFragment.newInstance(param1, "", arrCategory[position].toString(), false)

//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment!!
    }
}

class HomePagerFragment : Fragment(), OnFragmentNavigatorListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewPager:ViewPager2
    lateinit var tab_layout: TabLayout
    private lateinit var homeCollectionAdapter: HomeCollectionAdapter
    var arrCategory:JSONArray? = null

    lateinit var imgWebsites: ImageView
    lateinit var bottomView: BottomView

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
        val view = inflater.inflate(R.layout.fragment_home_pager, container, false)

        bottomView = view.findViewById(R.id.bottomView)
        bottomView.setViewTab(0)

        imgWebsites = view.findViewById(R.id.imgWebsites)
        tab_layout = view.findViewById(R.id.tab_layout)

        imgWebsites.setOnClickListener {
            addFragment<OrderWebsiteFragment> {
                param1 to ""
                param2 to ""
            }
        }

        tab_layout.setTabTextColors(getResources().getColor(R.color.titlenewscolor, null), getResources().getColor(R.color.mainredcolor, null))
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mainredcolor, null))
//        tab_layout.setBackgroundResource(R.color.white)

        viewPager = view.findViewById(R.id.viewPager)

        if(param1?.isEmpty() == true) {
            // trang chu
            getCategory()
        } else {
            //trang dau bao
//                Log.d("vietnb", "parame 1: $param1")
            val sharedPreference:DataPreference = DataPreference(requireContext())
            var arrDICTWIDCATEGORY = sharedPreference.getValueJArrayMap(PREFERENCE.ARRAYDICTWIDCATEGORY)
            arrCategory = arrDICTWIDCATEGORY.get(param1!!.toInt())
            homeCollectionAdapter = HomeCollectionAdapter(this@HomePagerFragment, arrCategory!!, param1!!)
            viewPager.adapter = homeCollectionAdapter
            tab_layout.setupWithViewPager(viewPager, arrCategory!!)
            viewPager.offscreenPageLimit = 1
        }


        return view
    }

    private fun getCategory() {
        val sharedPreference:DataPreference = DataPreference(requireContext())
        val arrayCategoryHot = sharedPreference.getValueString(PREFERENCE.ARRAYCATEGORYHOT)

        var hasCategoryHot:Boolean = false
        if(arrayCategoryHot != null) {
            hasCategoryHot = true
            arrCategory = JSONArray(arrayCategoryHot)
            homeCollectionAdapter = HomeCollectionAdapter(this, arrCategory!!, null)
            viewPager.adapter = homeCollectionAdapter
            tab_layout.setupWithViewPager(viewPager, arrCategory!!)
            viewPager.offscreenPageLimit = 1
            //Log.d("vietnb", "kiem tra da $arrCategory")
        }

        var retrofit: APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        var response: Call<ResponseBody>? = null
        response = retrofit.getCategory(Global.getHeaderMap())

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
                            if(jsonObject.has("websites"))
                            {
                                val websites = jsonObject.getJSONArray("websites")
                                analyzeWebsite(websites)

                                val sharedPreference:DataPreference = DataPreference(requireContext())
                                val arrayCategoryHot = sharedPreference.getValueString(PREFERENCE.ARRAYCATEGORYHOT)!!
                                if(arrayCategoryHot.isNotEmpty() && !hasCategoryHot) {
                                    arrCategory = JSONArray(arrayCategoryHot)
                                    homeCollectionAdapter = HomeCollectionAdapter(this@HomePagerFragment, arrCategory!!, null)
                                    viewPager.adapter = homeCollectionAdapter
                                    tab_layout.setupWithViewPager(viewPager, arrCategory!!)
                                    viewPager.offscreenPageLimit = 1
                                }
                            }
                        }
                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("vietnb", "loi ham lay category")
            }

        })
    }

    fun analyzeWebsite(arrWebsite: JSONArray){
        val mapIdNameCoverWebsite = mutableMapOf<Int, String>()

        val mapIdNameWebsite = mutableMapOf<Int, String>()

        val arrDictIdWebsite_Category = mutableMapOf<Int, String>()
        val arrayNameWebsite = JSONArray()

        for (i in 0 until arrWebsite.length()) {
            val category = arrWebsite.getJSONObject(i)

            val id = category.getInt("id")
            val categorys = category.getJSONArray("categorys")
            arrDictIdWebsite_Category[id] = categorys.toString()

            arrayNameWebsite.put(category)

            val name = category.getString("name")
            mapIdNameWebsite.put(id, name)

            val icon_url = category.getString("icon_url")
            val jObject = JSONObject()
            jObject.put("name", name)
            jObject.put("icon_url", icon_url)
            mapIdNameCoverWebsite.put(id, jObject.toString())

//            Log.d("vietnb", "dang kiem tra id: $id || name: $name")
        }

        val sharedPreference:DataPreference = DataPreference(requireContext())

        sharedPreference.save(PREFERENCE.MAPIDNAMEWEBSITE, mapIdNameWebsite) ///999:Tin

        sharedPreference.save(PREFERENCE.MAPIDNAMECOVERWEBSITE, mapIdNameCoverWebsite) ///phuc vu order

        sharedPreference.save(PREFERENCE.ARRAYNAMEWEBSITE, arrayNameWebsite.toString())

        sharedPreference.save(PREFERENCE.ARRAYDICTWIDCATEGORY, arrDictIdWebsite_Category)

        val arrayNameWebsitesVSNameCategory = mutableMapOf<Int, HashMap<Int, String>>()
        for (i in 0 until arrWebsite.length()) {
            val category = arrWebsite.getJSONObject(i)

            val id = category.getInt("id")
            val arrayCategory = category.getJSONArray("categorys")

            val arrayNameCategory = HashMap<Int, String>()

            for(j in 0 until arrayCategory.length()) {
                val dictData = arrayCategory.getJSONObject(j)
                val name = dictData.getString("name")
                val id = dictData.getInt("id")
                arrayNameCategory[id] = name
            }

            arrayNameWebsitesVSNameCategory[id] = arrayNameCategory
//            Log.d("vietnb", "luu xong")
        }
        sharedPreference.save(PREFERENCE.ARRAYWEBSITETOPIC, arrayNameWebsitesVSNameCategory.toString())

        val arrCategoryHot = JSONArray()
        for (i in 0 until arrWebsite.length()) {
            val category = arrWebsite.getJSONObject(i)
            val id = category.getInt("id")
            if(id == 999) {
                val arrayCategory = category.getJSONArray("categorys")
                for (j in 0 until arrayCategory.length()) {
                    val dictData = arrayCategory.getJSONObject(j)
                    val show = dictData.getInt("show")
                    if(show == 1) {
                        arrCategoryHot.put(dictData)
                    }
                }
            }
        }
        sharedPreference.save(PREFERENCE.ARRAYCATEGORYHOT, arrCategoryHot.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOpenedFragment() {
        Log.i("TEST","OPENED_FRAGMENT")
    }

    override fun onReturnedFragment() {
        Log.i("TEST","RETURNED_FRAGMENT")
    }
}

fun TabLayout.setupWithViewPager(viewPager: ViewPager2, arrCategory: JSONArray) {
    if (arrCategory.length() != viewPager.adapter?.itemCount)
        throw Exception("The size of list and the tab count should be equal!")

    TabLayoutMediator(this, viewPager,
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->

            if(arrCategory != null) {
                val category = JSONObject(arrCategory!![position].toString())
                if(category.has("name")) {
                    tab.text = category.getString("name")
                }
            }

        }).attach()
}