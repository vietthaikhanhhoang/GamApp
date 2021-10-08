package com.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.google.android.gms.common.SignInButton
import com.lib.Utils


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PopupDialogFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var signInButton: SignInButton

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
        val btnLoginFacebook = view.findViewById<Button>(R.id.btnLoginFacebook)
        btnLoginFacebook.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.loginFacebook()
            }
            //Toast.makeText(this.context, "hello", Toast.LENGTH_SHORT).show()
            //onResult?.invoke(10.0, "be continue")
            dismiss()
        }

        //signInButton = view.findViewById<View>(R.id.signInButton) as SignInButton
        var btnLoginGoogle = view.findViewById<Button>(R.id.btnLoginGoogle)
        btnLoginGoogle.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.loginGoogle()
            }

            //Toast.makeText(this.context, "hello", Toast.LENGTH_SHORT).show()

            //onResult?.invoke(10.0, "be continue")
            dismiss()
        }

        var tfPhone = view.findViewById<EditText>(R.id.tfPhone)

        var btnLoginPhone = view.findViewById<Button>(R.id.btnPhone)
        btnLoginPhone.setOnClickListener{
//            val topActivity = Utils.getActivity(requireContext())
//            if(topActivity is MainActivity) {
//                topActivity.loginPhone(tfPhone.text.toString())
//            }
            //Toast.makeText(this.context, "hello", Toast.LENGTH_SHORT).show()

            dismiss()
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
         * @return A new instance of fragment PopupDialogFrag.
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