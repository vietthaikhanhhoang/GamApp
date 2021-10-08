package com.fragmentcustom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.orderwebsite.OrderWebsiteAdapter
import com.fragula.extensions.addFragment
import com.lib.Utils
import com.main.app.MainActivity
import data.DataPreference
import data.PREFERENCE
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderWebsiteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderWebsiteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var imgBack: ImageView

    lateinit var rclView: RecyclerView
    var orderWebsiteAdapter = OrderWebsiteAdapter(mapOf<Int, String>())

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
        val view = inflater.inflate(R.layout.fragment_order_website, container, false)
        rclView = view.findViewById(R.id.rclView)
        imgBack = view.findViewById(R.id.imgBack)

        imgBack.setOnClickListener {
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.actionBack()
            }
        }

        val sharedPreference: DataPreference = DataPreference(this.requireContext())
        var mapIDNameCoverWebsite = sharedPreference.getValueJObjectMap(PREFERENCE.MAPIDNAMECOVERWEBSITE)
        orderWebsiteAdapter.mList = mapIDNameCoverWebsite

        orderWebsiteAdapter.setOrderWebsiteAdapterListener(object : OrderWebsiteAdapter.OrderWebsiteAdapterListener{
            override fun click_OrderWebsiteAdapter(position: Int) {
                var id = mapIDNameCoverWebsite.keys.elementAt(position).toString()

                addFragment<HomePagerFragment> {
                    ARG_PARAM1 to id
                    ARG_PARAM2 to ""
                }
            }

        })

        rclView.adapter = orderWebsiteAdapter
        val layoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = layoutManager

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderWebsiteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderWebsiteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}