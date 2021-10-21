package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.customview.BottomView
import com.fragmentcustom.autoplayvideo.AutoplayVideoFragment
import com.fragula.extensions.addFragment
import com.lib.Utils
import data.DataPreference
import data.PREFERENCE
import data.datastorepreference.AUTOPLAY_MODE
import data.datastorepreference.newsreadedmanager
import data.datastorepreference.settingmanager
import data.datastoreproto.DocumentProtoManager
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import model.PListingResponse
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

    lateinit var bottomView: BottomView

    lateinit var layoutAutoPlay: LinearLayout
    lateinit var textAutoPlay: TextView
    private var isAutoPlayMode = true

    private lateinit var documentProtoManager: DocumentProtoManager
    lateinit var txtNameProtobuf: TextView

    var arrayDocumentProtobuf = ArrayList<PListingResponse.Document>()

    /////
    private lateinit var settingsManager: settingmanager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun observeUiPreferences() {
        settingsManager.autoplayMode.asLiveData().observe(requireActivity()) { it ->
            when (it) {
                AUTOPLAY_MODE.ON -> {
                    textAutoPlay.text = "Cài đặt autoPlay: isON"
                    isAutoPlayMode = true
                }
                AUTOPLAY_MODE.OFF -> {
                    textAutoPlay.text = "Cài đặt autoPlay: isOFF"
                    isAutoPlayMode = false
                }
            }
        }
    }

    private fun observeDocumentProto() {
//        documentProtoManager.documentProto.asLiveData().observe(this) {
//            Log.d("vietnb", "xem o day xem co cai veo gi: " + it.art.title)
//            txtNameProtobuf.text = "Tên protobuf:" + it.art.title
//        }
    }

    private fun observeDocumentNewsReaded() {
        val newsreaded_manager = newsreadedmanager(requireContext())
        newsreaded_manager.newsreaded.asLiveData().observe(requireActivity()) {
            var strLid = it //"news:28:1:411445cad974217aa022615cfea05b1f,news:52:1:32760f94f4e512a63c2d874655880e21" //it
            Log.d("vietnb", "chuoi: lid $strLid")
            Log.d("vienb", "")

            var arrLid = strLid.toString().split(",").toTypedArray()
            for (i in 0 until arrLid.size) {
                val lid = arrLid[i].toString()
                val documentProtoManager = DocumentProtoManager(requireContext(), lid)
                if(documentProtoManager != null) {
                    documentProtoManager.documentProto.asLiveData().observe(requireActivity()) {
                        val art = it.art
                        arrayDocumentProtobuf.add(it)

                        if(arrayDocumentProtobuf.size == 4) {
                            val art0 = arrayDocumentProtobuf[0]
                            val art1 = arrayDocumentProtobuf[1]
                            val art2 = arrayDocumentProtobuf[2]
                            val art3 = arrayDocumentProtobuf[3]

                            txtNameProtobuf.text = art0.art.title + art1.art.title + art2.art.title + art3.art.title
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        settingsManager = settingmanager(requireContext())

        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        layoutLogin = view.findViewById(R.id.layoutLogin)

        //////hien thi document proto da luu
        //documentProtoManager = DocumentProtoManager(requireContext(), "")

        observeDocumentNewsReaded()

        observeDocumentProto()
        txtNameProtobuf = view.findViewById(R.id.txtNameProtobuf)

        bottomView = view.findViewById(R.id.bottomView)
        bottomView.setViewTab(4)

        observeUiPreferences()
        textAutoPlay = view.findViewById(R.id.textAutoPlay)
        layoutAutoPlay = view.findViewById(R.id.layoutAutoPlay)
        layoutAutoPlay.setOnClickListener {
            lifecycleScope.launch {
                when (isAutoPlayMode) {
                    true -> {
                        settingsManager.setAutoPlayMode(AUTOPLAY_MODE.OFF)
                        Log.d("vietnb", "set autoplay off")
                    }
                    false -> {
                        settingsManager.setAutoPlayMode(AUTOPLAY_MODE.ON)
                        Log.d("vietnb", "set autoplay on")
                    }
                }
            }
        }

        imgAvatar = view.findViewById(R.id.imgAvatar)
        layoutLogin.setOnClickListener{

            val sharedPreference:DataPreference = DataPreference(this.requireContext())
            val accountUser = sharedPreference.getValueJSON(PREFERENCE.ACCOUNTUSER)
            if(accountUser !is JSONObject) {
                val topActivity = Utils.getActivity(requireContext())
                if(topActivity is MainActivity) {
                    addFragment<LoginFragment> {
                        ARG_PARAM1 to ""
                        ARG_PARAM2 to ""
                    }
                }
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