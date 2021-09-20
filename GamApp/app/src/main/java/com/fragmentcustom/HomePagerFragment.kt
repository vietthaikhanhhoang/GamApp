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
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
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

class HomeCollectionAdapter(fragment: Fragment, var arrCategory: JSONArray) : FragmentStateAdapter(fragment) {
    fun refreshData(arrCategory: JSONArray){
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrCategory.length()

    override fun createFragment(position: Int): Fragment {

        // Return a NEW fragment instance in createFragment(int)
        val fragment = HomeFragment.newInstance("", "", arrCategory[position].toString())
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
        return fragment
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

        tab_layout = view.findViewById(R.id.tab_layout)
        tab_layout.setTabTextColors(getResources().getColor(R.color.titlenewscolor, null), getResources().getColor(R.color.mainredcolor, null))
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mainredcolor, null))
//        tab_layout.setBackgroundResource(R.color.white)

        viewPager = view.findViewById(R.id.viewPager)

        getCategory()

        return view
    }

    private fun getCategory() {
        val sharedPreference:DataPreference = DataPreference(requireContext())
        val arrayCategoryHot = sharedPreference.getValueString(PREFERENCE.ARRAYCATEGORYHOT)

        var hasCategoryHot:Boolean = false
        if(arrayCategoryHot != null) {
            hasCategoryHot = true
            arrCategory = JSONArray(arrayCategoryHot)
            homeCollectionAdapter = HomeCollectionAdapter(this, arrCategory!!)
            viewPager.adapter = homeCollectionAdapter
            tab_layout.setupWithViewPager(viewPager, arrCategory!!)
            viewPager.offscreenPageLimit = 1
            //Log.d("vietnb", "kiem tra da $arrCategory")
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.appnews24h.com")
            .build()

        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getCategory(Global.getHeaderMap())

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
                            //Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.getJSONArray("websites") != null)
                            {
                                val websites = jsonObject.getJSONArray("websites")
                                analyzeWebsite(websites)

                                val sharedPreference:DataPreference = DataPreference(requireContext())
                                val arrayCategoryHot = sharedPreference.getValueString(PREFERENCE.ARRAYCATEGORYHOT)!!
                                if(arrayCategoryHot.isNotEmpty() && !hasCategoryHot) {
                                    arrCategory = JSONArray(arrayCategoryHot)
                                    homeCollectionAdapter = HomeCollectionAdapter(this@HomePagerFragment, arrCategory!!)
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
        }
    }

    fun analyzeWebsite(arrWebsite: JSONArray){
        val mapIdNameWebsite = mutableMapOf<Int, String>()

        val arrDictIdWebsite_Category = mutableMapOf<Int, JSONArray>()
        val arrayNameWebsite = JSONArray()

        for (i in 0 until arrWebsite.length()) {
            val category = arrWebsite.getJSONObject(i)

            val id = category.getInt("id")
            val categorys = category.getJSONArray("categorys")
            arrDictIdWebsite_Category[id] = categorys

            arrayNameWebsite.put(category)

            val name = category.getString("name")
            mapIdNameWebsite.put(id, name)

//            Log.d("vietnb", "dang kiem tra id: $id || name: $name")
        }

        val sharedPreference:DataPreference = DataPreference(requireContext())

        sharedPreference.save(PREFERENCE.MAPIDNAMEWEBSITE, mapIdNameWebsite) ///999:Tin

        sharedPreference.save(PREFERENCE.ARRAYNAMEWEBSITE, arrayNameWebsite.toString())

        sharedPreference.save(PREFERENCE.ARRAYDICTWIDCATEGORY, arrDictIdWebsite_Category.toString())

        val arrayNameWebsitesVSNameCategory = mutableMapOf<Int, Map<Int, String>>()
        for (i in 0 until arrWebsite.length()) {
            val category = arrWebsite.getJSONObject(i)

            val id = category.getInt("id")
            val arrayCategory = category.getJSONArray("categorys")

            val arrayNameCategory = mutableMapOf<Int, String>()

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