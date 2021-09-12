package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.MainActivity
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.dialog.LoginFragment
import com.lib.Utils
import data.DataPreference
import data.PREFERENCE
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var imgAvatar:CircleImageView
    lateinit var txtDangNhap: TextView
    lateinit var layoutLogin:ConstraintLayout

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
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        layoutLogin = view.findViewById(R.id.layoutLogin)

        imgAvatar = view.findViewById(R.id.imgAvatar)
        layoutLogin.setOnClickListener{
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                val fm = topActivity.supportFragmentManager
                val loginFragment = LoginFragment.newInstance("", "")
                loginFragment.show(fm, "login")
            }
        }

        txtDangNhap = view.findViewById(R.id.txtDangNhap)
        refreshDataAccountUser()

        return view
    }

    fun refreshDataAccountUser() {
        imgAvatar.setImageResource(R.drawable.facebook)
        txtDangNhap.text = "Đăng nhập"
        val sharedPreference:DataPreference = DataPreference(this.requireContext())
        val accountUser = sharedPreference.getValueJSON(PREFERENCE.ACCOUNTUSER)
        if(accountUser is JSONObject) {
            txtDangNhap.text = accountUser.getString("name")
            val avatar = accountUser.getString("avatar")
            val radius = imgAvatar.layoutParams.width // corner radius, higher value = more rounded
            val margin = 0 // crop margin, set to 0 for corners with no crop
            Glide.with(this.imgAvatar.context)
                .load(avatar)
                .transform(RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.thumbnews)
                .into(this.imgAvatar)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}