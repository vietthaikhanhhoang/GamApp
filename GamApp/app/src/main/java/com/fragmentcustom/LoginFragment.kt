package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.barservicegam.app.R
import com.fragula.extensions.addFragment
import com.lib.Utils
import com.main.app.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btnPhone: Button

    lateinit var tfPhone: EditText
    lateinit var imgClose: ImageView

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        btnPhone = view.findViewById(R.id.btnPhone)
        tfPhone = view.findViewById(R.id.tfPhone)
        imgClose = view.findViewById(R.id.imgClose)

        imgClose.setOnClickListener {
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.actionBack()
            }
        }

        btnPhone.setOnClickListener{
            if(tfPhone.length() > 0) {
                val topActivity = Utils.getActivity(requireContext())
                if(topActivity is MainActivity) {
                    topActivity.loginPhone(tfPhone.text.toString())
                    topActivity.setTheCallBack {
                        if(it != "") {
                            //Log.d("vietnb", "sau khi login xong")
                            addFragment<ConfirmOTP> {
                                ARG_PARAM1 to it
                                ARG_PARAM2 to ""
                            }
                        } else {
                            Toast.makeText(this.context, "Lỗi xác thực SĐT", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val btnLoginFacebook = view.findViewById<Button>(R.id.btnLoginFacebook)
        btnLoginFacebook.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.loginFacebook()
            }
        }

        var btnLoginGoogle = view.findViewById<Button>(R.id.btnLoginGoogle)
        btnLoginGoogle.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.loginGoogle()
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}