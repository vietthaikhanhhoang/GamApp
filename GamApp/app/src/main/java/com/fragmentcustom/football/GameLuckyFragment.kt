package com.fragmentcustom.football

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.ProgressBar
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

//    class JSBridge(val context: Context) {
//        private var con:Context
//        init {
//            this.con = context
//        }
//
//        @JavascriptInterface
//        fun showMessageInNative(message: String) {
//            Log.d("vietnb", "message: " + message)
//        }
//    }

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

        var url = "https://lucky.tinmoi24.vn/quay-thuong.html?utm_source=xxxx&utm_campain=game&utm_medium=zzz&platform=ios"
        url += "&device_id=0ef50568-d402-472f-994e-bea239532a90"

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

        val webSettings = webGame.settings
        webSettings.builtInZoomControls = true
        webSettings.javaScriptEnabled = true

        val webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                Log.d("vietnb", "progress: $newProgress")
                progressWeview.progress = newProgress
                view!!.setBackgroundColor(resources.getColor(R.color.black, null))
            }
        }
        webGame.webChromeClient = webChromeClient

        val interf = WebAppInterface(this.requireContext(), "jsHandler")
//        webGame.addJavascriptInterface(JSBridge(this.requireContext()),"jsHandler")
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

public class WebAppInterface(requireContext: Context, s: String) {
    private lateinit var context: Context
    fun WebAppInterface(context: Context) {
        this.context = context
    }

    @JavascriptInterface
    fun showMessageInNative(message: String) {
        Log.d("vietnb", "message: " + message)
    }
}










