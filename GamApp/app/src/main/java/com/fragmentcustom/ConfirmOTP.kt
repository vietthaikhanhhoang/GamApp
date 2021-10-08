package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.barservicegam.app.R
import com.goodiebag.pinview.Pinview
import com.lib.Utils
import com.main.app.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmOTP.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmOTP : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var tfOTP:Pinview
    lateinit var btnConfirm: Button
    lateinit var imgClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            //Log.d("vietnb", "verify 111: $param1")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_confirm_o_t_p, container, false)
        tfOTP = view.findViewById(R.id.tfOTP)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        imgClose = view.findViewById(R.id.imgClose)

        imgClose.setOnClickListener {
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.actionBack()
            }
        }

        btnConfirm.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                Log.d("vietnb", "verify: $param1")
                topActivity.verifyPhoneNumberWithCode(param1, tfOTP.value)
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConfirmOTP.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConfirmOTP().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}