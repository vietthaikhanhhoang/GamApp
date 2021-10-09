package com.fragmentcustom.football

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.barservicegam.app.R
import com.lib.Utils
import com.main.app.MainActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameLuckyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameLuckyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var imgBack: ImageView
    lateinit var webGame: WebView
    lateinit var progressWeview: ProgressBar

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
        val view = inflater.inflate(R.layout.fragment_game_lucky, container, false)
        webGame = view.findViewById(R.id.webGame)
        imgBack = view.findViewById(R.id.imgBack)
        progressWeview = view.findViewById(R.id.progressWeview)

        progressWeview.visibility = View.INVISIBLE

        imgBack.setOnClickListener {
            val topActivity = Utils.getActivity(requireContext())
            if(topActivity is MainActivity) {
                topActivity.actionBack()
            }
        }

        //android: "https://lucky.tinmoi24.vn/quay-thuong.html?utm_source=xxxx&utm_campain=game&utm_medium=zzz"

        var url = "https://lucky.tinmoi24.vn/quay-thuong.html?utm_source=xxxx&utm_campain=game&utm_medium=zzz&platform=android"
        url += "&device_id=0ef50568-d402-472f-994e-bea239532a90"

        //webGame.loadUrl("http://192.168.55.104:2000/")
        webGame.loadUrl(url)

        webGame.addJavascriptInterface(
            WebAppInterface(requireContext()),
            "JsInterface"
        ) // To call methods in Android from using js in the html, AndroidInterface.showToast, AndroidInterface.getAndroidVersion etc

        val webSettings: WebSettings = webGame.getSettings()
        webSettings.javaScriptEnabled = true

        val webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("vietnb", "bat dau chay")
                view!!.setBackgroundColor(resources.getColor(R.color.black, null))
                progressWeview.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("vietnb", "da chay xong")
                view!!.setBackgroundColor(resources.getColor(R.color.black, null))
                progressWeview.visibility = View.INVISIBLE

                //view.loadUrl("javascript:alert(showVersion('called by Android'))")
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                // log click event
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webGame.webViewClient = webViewClient
//
        val webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult
            ): Boolean {
                Log.d("LogTag", message!!)
                result.confirm()
                return true
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                Log.d("vietnb", "progress: $newProgress")
                progressWeview.progress = newProgress
                view!!.setBackgroundColor(resources.getColor(R.color.black, null))
            }
        }
        webGame.webChromeClient = webChromeClient

        webGame.loadUrl(url)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameLuckyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameLuckyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

////
class WebAppInterface internal constructor(c: Context) {
    var mContext: Context

    // Show a toast from the web page
    @JavascriptInterface
    fun showToast(toast: String?) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        Log.d("vietnb", "xem len khong: $toast")
    }

    @JavascriptInterface
    fun showText(toast: String?) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        Log.d("vietnb", "xem len khong AAA: $toast")
    }

    @JavascriptInterface
    fun postMessage(toast: String?) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        Log.d("vietnb", "xem len khong AAA 111: $toast")
    }

    @JavascriptInterface
    fun getAndroidVersion(): Int {
        Log.d("vietnb", "xem len khong 111")
        return Build.VERSION.SDK_INT
    }

    @JavascriptInterface
    fun showAndroidVersion(versionName: String?) {
        Toast.makeText(mContext, versionName, Toast.LENGTH_SHORT).show()
    }

    // Instantiate the interface and set the context
    init {
        mContext = c
    }
}










