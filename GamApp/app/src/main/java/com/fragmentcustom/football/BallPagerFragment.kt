package com.fragmentcustom.football

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.barservicegam.app.R
import com.customview.BottomView
import com.fragmentcustom.HomeCollectionAdapter
import com.fragmentcustom.HomeFragment
import com.fragmentcustom.setupWithViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BallPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class BallCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    fun refreshData(){
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        if(position == 0) {
            return HomeFragment.newInstance("", "", "", true)
        } else if(position == 1) {
            return GuessFragment.newInstance("", "")
        }

        return GuessFragment.newInstance("", "")
    }
}

class BallPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewPager: ViewPager2
    lateinit var tab_layout: TabLayout
    private lateinit var ballCollectionAdapter: BallCollectionAdapter

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
        val view = inflater.inflate(R.layout.fragment_ball_pager, container, false)

        bottomView = view.findViewById(R.id.bottomView)
        bottomView.setViewTab(2)

        tab_layout = view.findViewById(R.id.tab_layout)
        tab_layout.setTabTextColors(getResources().getColor(R.color.titlenewscolor, null), getResources().getColor(R.color.mainredcolor, null))
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mainredcolor, null))

        viewPager = view.findViewById(R.id.viewPager)

        ballCollectionAdapter = BallCollectionAdapter(this)
        viewPager.adapter = ballCollectionAdapter
        tab_layout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit = 1
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BallPagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BallPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

fun TabLayout.setupWithViewPager(viewPager: ViewPager2) {
    TabLayoutMediator(this, viewPager,
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if(position == 0) {
                tab.text = "Bóng đá 24H"
            } else if(position == 1) {
                tab.text = "Dự đoán"
            } else if(position == 2) {
                tab.text = "Dự đoán"
            }
        }).attach()
}