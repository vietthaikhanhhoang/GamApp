package com.fragmentcustom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.customadapter.DetailNewsAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.protobuf.ByteString
import com.lib.Utils
import data.dataHtml
import model.PArticle
import model.PListingResponse
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
    private var param3: ByteArray? = null
    private var param4: String? = null

    lateinit var txtTitle: TextView
    lateinit var txtNative: TextView

    lateinit var rclView: RecyclerView
    lateinit var detailNewsAdapter: DetailNewsAdapter
    var arrayContent = ArrayList<dataHtml>()
    lateinit var imgBack: ImageView

    lateinit var relativeFragment:RelativeFragment

    lateinit var art: PArticle.ArticleMsg
    val arrNewsRelative = JSONArray()

    var category:String = ""

    fun parseContent() {
        var title = ""
        var desc = ""
        var content = ""

        if(art != null) {
            title = art.title
            desc = art.desc
            content = art.content
        }

        arrayContent.add(dataHtml(title, "title"));
        var category = category

        val sid = art.sid
        category = category + " • " + Global.getNameWebsite(sid, requireContext())

        val posttime:Long = art.posttime
        category = category + " • " + Global.currentTimeSecUTC(posttime)

        arrayContent.add(dataHtml(category, "category"))

        arrayContent.add(dataHtml(desc, "desc"))

        var isNative:Boolean = false
        txtNative.text = "WebView"

        if(art.native == PArticle.ENumEnableType.IS_ENABLE) {
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
                        if(arrayContent.last().type == "img" || arrayContent.last().type == "video") {
                            flag = true
                        }
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

                        var flag = false
                        if(arrayContent.last().type == "desc") {
                            flag = true
                        }

                        arrayContent.add(dataHtml(content, type, flag)) //image la the dau tien thi top = 0
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
                                val cover = art.cover
                                arrayContent.add(dataHtml(cover, "video"))
                            }
                        }
                    }
                }
            }
        }
        else {
            arrayContent.add(dataHtml(content, "webview"))
        }

        detailNewsAdapter = DetailNewsAdapter(this.requireContext(), arrayContent, rclView)
        rclView.adapter = detailNewsAdapter
    }

    private fun getRelativeNews() {
        var lid = ""

        if(art != null) {
            lid = art.lid
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
                            val linfos = jsonObject.getJSONArray("linfos")
                            for (i in 0 until linfos.length()) {
                                val infos = JSONObject(linfos[i].toString())
                                if(infos.has("type")) {
                                    val type = infos.getInt("type")
                                    if(type == 1) { //dang bai bao art
                                        arrNewsRelative.put(infos)
                                    }
                                }
                            }
                            relativeFragment.updateData(arrNewsRelative)
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
            param3 = it.getByteArray(ARG_PARAM3)
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
            art = PArticle.ArticleMsg.parseFrom(it)
        }

        val sid = art.sid
        val cid = art.cid
        category = Global.getNameCategory(sid, cid)

        param4.let {
            if (it != "") {
                category = it!!
            }
        }

//        //add RelativeFragment to layoutRelative
        relativeFragment = RelativeFragment.newInstance("", "")
        childFragmentManager.beginTransaction().replace(R.id.layoutRelative, relativeFragment).commit()

        txtNative = view.findViewById(R.id.txtNative)

        txtTitle = view.findViewById(R.id.txtStar)
        txtTitle.text = Global.getNameWebsite(sid, txtTitle.context)

        imgBack = view.findViewById(R.id.imgBack)
        imgBack.setOnClickListener{
//            childFragmentManager.popBackStack()
            Log.d("vietnb", "goi vao day da")
            val topActivity = Utils.getActivity(requireContext())
            if (topActivity is MainActivity) {
                topActivity.actionBack()
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