package com.fragmentcustom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.barservicegam.app.R
import com.fragula.extensions.addFragment
import com.fragula.extensions.parentNavigator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var txtTitle: TextView
    private lateinit var btnNext: Button

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
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        btnNext = view.findViewById(R.id.btnNext)
        txtTitle = view.findViewById(R.id.txtTitle)

        param1?.let {
            var str = txtTitle.text as String
            str = str + it
            txtTitle.text = str
        }

        btnNext.setOnClickListener {
            addFragment<BlankFragment> {
                ARG_PARAM1 to "Add fragment arg"
                ARG_PARAM2 to 12345
            }
        }

        view.post {
            val count = parentNavigator.fragmentCount.toString()
            txtTitle.text = count
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        param1?.let {
//            outState.putString(ARG_PARAM1, it)
//        }
//
//        param2?.let {
//            outState.putInt(ARG_PARAM2, it)
//        }
    }

//    override fun onOpenedFragment() {
//        Log.i("TEST","OPENED_FRAGMENT")
//    }
//
//    override fun onReturnedFragment() {
//        Log.i("TEST","RETURNED_FRAGMENT")
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}