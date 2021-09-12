package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.activity.MainActivity
import com.barservicegam.app.R
import com.customadapter.DetailNewsAdapter
import com.customadapter.ListNewsAdapter
import com.fragula.extensions.addFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.hannesdorfmann.swipeback.Position
import com.hannesdorfmann.swipeback.SwipeBack
import data.dataHtml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailNewsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null

    lateinit var txtTitle: TextView
    lateinit var txtNative: TextView

    lateinit var rclView: RecyclerView
    lateinit var detailNewsAdapter: DetailNewsAdapter
    var arrayContent = ArrayList<dataHtml>()
    lateinit var imgBack: ImageView

    lateinit var relativeFragment:RelativeFragment

    lateinit var art: JSONObject
    val arrNewsRelative = JSONArray()

    var category:String = ""

    fun parseContent() {
        var title = ""
        var desc = ""
        var content = ""

        if(art != null) {
            if(art.has("title")) {
                title = art.getString("title")
            }

            if(art.has("desc")) {
                desc = art.getString("desc")
            }

            if(art.has("content")) {
                content = art.getString("content")
            }
        }

        arrayContent.add(dataHtml(title, "title"));
        var category = category

        if(art?.has("sid")) {
            val sid = art.getInt("sid")
            //category = category + "|" + Global.getNameWebsite(sid)
        }

        if(art?.has("posttime")) {
            val posttime:Long = art["posttime"] as Long
            //category = category + "|" + Global.currentTimeSecUTC(posttime)
        }
        arrayContent.add(dataHtml(category, "category"))

        arrayContent.add(dataHtml(desc, "desc"))

        var isNative:Boolean = false
        txtNative.text = "WebView"

        if(art.has("native")) {
            if(art.getInt("native") == 1) {
                isNative = true
                txtNative.text = "Native"
            }
        }

        if(isNative) {
            val doc = Jsoup.parse(content)
            for (i in 0 until doc.body().children().size) {
                var element = doc.body().children()[i]
                if(element.tagName() == "p") {
                    if(element.children().size > 0) {
                        var text = element.html()
                        if(text.length > 0) {
                            var content = text
                            var type = "texthtml"
                            arrayContent.add(dataHtml(content, type))
//                            Log.d("vietnb", "co vao html:" + content)
                        }
                    } else {
                        var text = element.text()
                        if(text.length > 0) {
                            var content = text
                            var type = "text"
                            arrayContent.add(dataHtml(content, type))
                        }
                    }
                } else if (element.tagName() == "em" || element.tagName() == "i") {
                    var text = element.html();
                    if (text.isNotEmpty()) {
                        var content = text
                        var type = "italic"

                        var flag = false
                        if(arrayContent.last().type == "img") {
                            flag = true
                        }
//                        if(doc.body().children().size > 1) {
//                            var elementPrevious = doc.body().children()[i-1]
//                            if (elementPrevious.tagName() == "img") {
//                                var src = elementPrevious.attr("src")
//                                if (src != null) {
//                                    flag = true
//                                }
//                            }
//                        }
                        arrayContent.add(dataHtml(content, type, flag))
                    }
                } else if (element.tagName() == "strong" || element.tagName() == "b") {
                    var text = element.html();
                    if (text.isNotEmpty()) {
                        var content = text
                        var type = "strong"
                        arrayContent.add(dataHtml(content, type))
                        print("co vao strong: " + content)
                    }
                } else if (element.tagName() == "u") {
                    var text = element.html();
                    if (text.length > 0) {
                        var content = text
                        var type = "underline"
                        arrayContent.add(dataHtml(content, type))
                    }
                } else if (element.tagName() == "img") {
                    var src = element.attr("src")
                    if (src != null) {
                        var type = "img"
                        var content = src
                        arrayContent.add(dataHtml(content, type))
                    }
                } else if (element.tagName() == "video") {
                    Log.d("vietnb", "tag video: $element")
//<video controls="controls" id="fplayVideo0" onclick="showVideoArticle('https://nld.mediacdn.vn/291774122806476800/2021/9/7/2742493976237-16310038843781108475104.mp4')" width="100%"></video>
                    if(element.hasAttr("onclick")) {
                        var textElement = element.attr("onclick")

                        //showVideoArticle('https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-09-07/cp_special_worldcup-1631018001-var_vietuc.m3u8')

                        val start = textElement.indexOf("showVideoArticle('")
                        val end = textElement.indexOf("')")

                        if(start > -1 && end > -1) {
                            val strContent = textElement.subSequence(start + "showVideoArticle('".length, end).toString()
                            val arrContent = strContent.split(",").toTypedArray()
                            var type = "video"
                            arrayContent.add(dataHtml(arrContent[0].toString(), type))
                        } else {
                            if(art != null) {
                                if (art.has("cover")) {
                                    val cover = art.getString("cover")
                                    arrayContent.add(dataHtml(cover, "video"))
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            arrayContent.add(dataHtml(content, "webview"))
        }

//        var jInfo = "[{\"title\" : \"WHO nhận định không cần thiết tiêm mũi vắc-xin tăng cường\" ," +
//                "" + "cover\" : \"https://cdntm.24hstatic.com/2021/8/19/9/4aa4d207ecf49294a0fb0e26e6fd52a6.jpg\" ," +
//                "" + "posttime\" : \"1629338280000\"}]"
//        var json = "{\"Art\" : \"$jInfo\"}"
//
//        var arrRelative = JSONArray(json)

//        val stringJson = "[{\"Art\": [{\"title\": \"WHO nhận định không cần thiết tiêm mũi vắc-xin tăng cường\",\"cover\": \"https://cdntm.24hstatic.com/2021/8/19/9/4aa4d207ecf49294a0fb0e26e6fd52a6.jpg\",\"time\": 1560262643000}, {\"title\": \"WHO nhận định không cần thiết tiêm mũi vắc-xin tăng cường\",\"cover\": \"https://cdntm.24hstatic.com/2021/8/19/9/4aa4d207ecf49294a0fb0e26e6fd52a6.jpg\",\"time\": 1560262643000}]}  ]"
//        val arrRelative = JSONArray(stringJson)
//
//
//        arrayContent.add(dataHtml(arrRelative.toString(), "relative"))

        detailNewsAdapter = DetailNewsAdapter(this.requireContext(), arrayContent, rclView)
        rclView.adapter = detailNewsAdapter
    }

    private fun getRelativeNews() {
        var lid = ""
        if(art != null) {
            if(art.has("lid")) {
                lid = art.getString("lid")
            }
        }

        Log.d("vietnb", "lay tin lien quan: " + lid)

        val retrofit:APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        val response = retrofit.getRelativeNews(lid, Global.getHeaderMap())
        response.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    val jsonObject = JSONObject(prettyJson)
                    if(jsonObject != null)
                    {
                        if (jsonObject.get("code").toString() == "200")
                        {
                            //Log.d("Pretty Printed JSON :", "hay vao day")
                            if (jsonObject.getJSONArray("linfos") != null)
                            {
                                val linfos = jsonObject.getJSONArray("linfos")
                                for (i in 0 until linfos.length()) {
                                    val infos = JSONObject(linfos[i].toString())
                                    if(infos.has("Doc")) {
                                        val doc = JSONObject(infos.get("Doc").toString())
                                        if(doc.has("Art")) {
                                            val art = JSONObject(doc.get("Art").toString())
//                                            Log.d("vietnb","art: " + art.toString())
                                            arrNewsRelative.put(art)
                                        }
                                    }
                                }

                                relativeFragment.updateData(arrNewsRelative)
                            }
                        }
                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("vietnb", "co loi xay ra")
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_news, container, false)

        param3.let {
            art = JSONObject(it)
        }

        if(art.has("sid") && art.has("cid")) {
            val sid = art.getInt("sid")
            val cid = art.getInt("cid")
            category = Global.getNameCategory(sid, cid)
        }

        param4.let {
            if (it != "") {
                category = it!!
            }
        }

//        //add RelativeFragment to layoutRelative
        relativeFragment = RelativeFragment.newInstance("", "")
        childFragmentManager.beginTransaction().replace(R.id.layoutRelative, relativeFragment).commit()

        txtNative = view.findViewById(R.id.txtNative)

        txtTitle = view.findViewById(R.id.txtTitle)
        if(art.has("sid")) {
            val sid = art.getInt("sid")
            txtTitle.text = Global.getNameWebsite(sid)
        }

        imgBack = view.findViewById(R.id.imgBack)
        imgBack.setOnClickListener{
//            childFragmentManager.popBackStack()
            Log.d("vietnb", "goi vao day da")
            if (activity is MainActivity) {
                val mainActivity: MainActivity? = activity as? MainActivity
                mainActivity?.actionBack()
            }
        }

        rclView = view.findViewById(R.id.rclView)
        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = mLayoutManager

        parseContent()
        //getRelativeNews()

        return view
    }

    override fun onPause() {
        super.onPause()
        Log.d("vietnb", "nhay vao pause fragment")
        pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("vietnb", "nhay vao destroy fragment")
        pauseVideo()
    }

    fun pauseVideo() {
        if(detailNewsAdapter != null) {
            detailNewsAdapter.pauseVideo()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailNewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, art:String, category:String) =
            DetailNewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, art)
                    putString(ARG_PARAM4, category)
                }
            }
    }
}