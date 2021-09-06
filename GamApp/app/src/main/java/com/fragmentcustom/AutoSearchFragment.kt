package com.fragmentcustom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.barservicegam.app.R
import com.lib.hideSoftKeyboard

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AutoSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AutoSearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var layoutParent: ConstraintLayout
    lateinit var txtSearch: EditText
    lateinit var imgClose: ImageView

    lateinit var layoutListWebsite: ConstraintLayout

    val listWebsiteFragment = ListWebsiteFragment.newInstance("", "")
    val nestedGridViewFragment = NestedGridViewFragment.newInstance("", "")

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
        val view = inflater.inflate(R.layout.fragment_auto_search, container, false)
        layoutParent = view.findViewById(R.id.layoutParent)
        txtSearch = view.findViewById(R.id.txtSearch)
        imgClose = view.findViewById(R.id.imgClose)

        layoutParent.setOnClickListener {
            this.hideSoftKeyboard()
            this.txtSearch.clearFocus()
        }

        imgClose.setOnClickListener{
            txtSearch.setText("")
        }

        txtSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                Log.d("vietnb", "xxxx: ${txtSearch.text}")
                if(s.isEmpty()) {
                    childFragmentManager.beginTransaction().replace(layoutListWebsite.id, nestedGridViewFragment).commit()
                } else
                {
                    childFragmentManager.beginTransaction().replace(layoutListWebsite.id, listWebsiteFragment).commit()
                }
            }
        })

        layoutListWebsite = view.findViewById(R.id.layoutListWebsite)
        childFragmentManager.beginTransaction().add(layoutListWebsite.id, nestedGridViewFragment).commit()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AutoSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AutoSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}