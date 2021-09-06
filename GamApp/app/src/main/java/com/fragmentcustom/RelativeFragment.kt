package com.fragmentcustom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.fragula.extensions.addFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.test.DetailNewsActivity
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
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 * Use the [RelativeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RelativeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var rclView: RecyclerView
    var listNewsAdapter = ListNewsAdapter(JSONArray(), false)

    val arrNews = JSONArray()

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
        val view = inflater.inflate(R.layout.fragment_relative, container, false)
        rclView = view.findViewById(R.id.rclView)

        val mLayoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = mLayoutManager

        rclView.adapter = listNewsAdapter
        listNewsAdapter.setListNewsAdapterListener(object : ListNewsAdapter.ListNewsAdapterListener{
            override fun click_ListNewsAdapterListener(position: Int) {
                addFragment<DetailNewsFragment> {
                    ARG_PARAM1 to ""
                    ARG_PARAM2 to ""
                    ARG_PARAM3 to arrNews[position].toString()
                    ARG_PARAM4 to ""
                }
            }
        })

        return view
    }

    fun updateData(arrayData: JSONArray) {
        for (i in 0 until arrayData.length()) {
            arrNews.put(JSONObject(arrayData[i].toString()))
        }

//        Log.d("vietnb" , "lay duoc du lieu: " + arrNews.length())
        listNewsAdapter.mList = arrNews
        listNewsAdapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RelativeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RelativeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}