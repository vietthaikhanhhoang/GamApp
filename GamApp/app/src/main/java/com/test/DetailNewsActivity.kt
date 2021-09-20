package com.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.DetailNewsAdapter
import com.fragmentcustom.RelativeFragment
import com.hannesdorfmann.swipeback.Position
import com.hannesdorfmann.swipeback.SwipeBack
import data.dataHtml
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Retrofit


class DetailNewsActivity : AppCompatActivity() {
    lateinit var txtTitle: TextView
    lateinit var txtNative: TextView

    lateinit var rclView: RecyclerView
    lateinit var detailNewsAdapter: DetailNewsAdapter
    var arrayContent = ArrayList<dataHtml>()
    lateinit var btnBack: Button

    lateinit var relativeFragment:RelativeFragment

    lateinit var art:JSONObject
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
            category = category + "|" + Global.getNameWebsite(sid, txtNative.context)
        }

        if(art?.has("posttime")) {
            val posttime:Long = art["posttime"] as Long
            category = category + "|" + Global.currentTimeSecUTC(posttime)
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
                }
            }

//            for (i in 0 until arrayContent.size) {
//                val data = arrayContent[i]
//                Log.d("type", "kiem tra nhanh: " + data.type)
//            }


            //        Log.d("vietnb", "content native: " + arrayContent.toString())
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

//        detailNewsAdapter = DetailNewsAdapter(arrayContent)
//        rclView.adapter = detailNewsAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.swipeback_stack_to_front,
            R.anim.swipeback_stack_right_out
        )
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)

        //add RelativeFragment to layoutRelative
        relativeFragment = RelativeFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().replace(R.id.layoutRelative, relativeFragment).commit()

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
            .setContentView(R.layout.activity_detail_news)
            .setSwipeBackView(R.layout.swipeback_default)

        txtNative = findViewById(R.id.txtNative)

        if(intent.hasExtra("art")) {
            art = JSONObject(intent.getStringExtra("art"))
        }

        if(intent.hasExtra("category")) {
            category = intent.getStringExtra("category").toString()
        } else {
            if(art.has("sid") && art.has("cid")) {
                val sid = art.getInt("sid")
                val cid = art.getInt("cid")
                category = Global.getNameCategory(sid, cid)
            }
        }

        txtTitle = findViewById(R.id.txtTitle)
        if(art.has("sid")) {
            val sid = art.getInt("sid")
            txtTitle.text = Global.getNameWebsite(sid, txtTitle.context)
        }

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            this.onBackPressed()
        }

        rclView = findViewById(R.id.rclView)
        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rclView.layoutManager = mLayoutManager

        parseContent()
        getRelativeNews()
    }

    fun getRelativeNews() {
        var lid = ""
        if(art != null) {
            if(art.has("lid")) {
                lid = art.getString("lid")
            }
        }

        Log.d("vietnb", "lay tin lien quan: " + lid)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.appnews24h.com")
            .build()

        val service = retrofit.create(APIService::class.java)

//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.getRelativeNews(lid, Global.getHeaderMap())
//
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                        )
//                    )
//
//                    val jsonObject = JSONObject(prettyJson)
//                    if(jsonObject != null)
//                    {
//                        if (jsonObject.get("code").toString() == "200")
//                        {
//                            //Log.d("Pretty Printed JSON :", "hay vao day")
//                            if (jsonObject.getJSONArray("linfos") != null)
//                            {
//                                val linfos = jsonObject.getJSONArray("linfos")
//                                for (i in 0 until linfos.length()) {
//                                    val infos = JSONObject(linfos[i].toString())
//                                    if(infos.has("Doc")) {
//                                        val doc = JSONObject(infos.get("Doc").toString())
//                                        if(doc.has("Art")) {
//                                            val art = JSONObject(doc.get("Art").toString())
//                                            //Log.d("vietnb", "art: " + art.toString())
//                                            arrNewsRelative.put(art)
//                                        }
//                                    }
//                                }
//
////                                Log.d("vietnb", "count tin lien quan: " + arrNewsRelative.length())
//                                relativeFragment.updateData(arrNewsRelative)
//                            }
//                        }
//                    }
//                } else {
//                    Log.e("RETROFIT_ERROR", response.code().toString())
//                }
//            }
//        }
    }
}