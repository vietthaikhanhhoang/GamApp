package com.customadapter

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.customview.segmentpredict
import com.github.infinitebanner.AbsBannerAdapter
import com.github.infinitebanner.InfiniteBannerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.lib.toPx
import data.DataPreference
import data.PREFERENCE
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*

class PredictBallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var txtTenGiai: TextView
    lateinit var txtThoiGian: TextView
    lateinit var imgLogoHome: ImageView
    lateinit var imgLogoAway: ImageView
    lateinit var txtHomeName: TextView
    lateinit var txtAwayName: TextView
    lateinit var txtVong: TextView
    lateinit var txtVS: TextView
    lateinit var txtScore:TextView

    /////
    lateinit var txtHoaCenter: TextView
    lateinit var imgClose: ImageView

    lateinit var txtJoinPredict: TextView
    lateinit var txtWin: TextView
    lateinit var txtDraw: TextView
    lateinit var txtLose: TextView

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.predict_ball_viewholder, this)
        txtTenGiai = view.findViewById(R.id.txtTenGiai)
        txtThoiGian = view.findViewById(R.id.txtThoiGian)
        imgLogoHome = view.findViewById(R.id.imgLogoHome)
        imgLogoAway = view.findViewById(R.id.imgLogoAway)
        txtHomeName = view.findViewById(R.id.txtHomeName)
        txtAwayName = view.findViewById(R.id.txtAwayName)
        txtVong = view.findViewById(R.id.txtVong)
        txtVS = view.findViewById(R.id.txtVS)
        txtScore = view.findViewById(R.id.txtScore)
        txtJoinPredict = view.findViewById(R.id.txtJoinPredict)

        txtWin = view.findViewById(R.id.txtWin)
        txtDraw = view.findViewById(R.id.txtDraw)
        txtLose = view.findViewById(R.id.txtLose)

        ////
        txtHoaCenter = view.findViewById(R.id.txtHoaCenter)
        imgClose = view.findViewById(R.id.imgSearch)

        txtHoaCenter.setPadding(16, 16, 16, 16)
    }
}

public class LoopBallAdapter(var mList: JSONArray) : AbsBannerAdapter() {
    interface LoopBallAdapterListener {
        fun clickJoinPredict_LoopBallAdapterListener(position: Int)
    }

    var listener: LoopBallAdapterListener? = null

    fun setLoopBallAdapterListener(listener: LoopBallAdapterListener) {
        this.listener = listener
    }

    override fun getCount(): Int {
        return mList.length()
    }


    override fun makeView(parent: InfiniteBannerView?): View {
        val predictBallView = PredictBallView(parent!!.context, null, 0)
        predictBallView.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        return predictBallView
    }

    override fun bind(view: View?, position: Int) {
        if(view is PredictBallView) {
            val matchinfo = JSONObject(mList[position].toString())

            val layoutParamWin = view.txtWin.layoutParams
            layoutParamWin.width = 20.toPx()
            view.txtWin.layoutParams = layoutParamWin
            view.txtWin.setBackgroundColor(R.color.com_facebook_blue)

            val layoutParamDraw = view.txtDraw.layoutParams
            layoutParamDraw.width = 20.toPx()
            view.txtDraw.layoutParams = layoutParamDraw


            ///
            if(matchinfo.has("predic_opts")) {
                val predic_opts = matchinfo.getJSONArray("predic_opts")
                if(predic_opts.length() > 2) {
                    val jWin = predic_opts.getJSONObject(0)//.getInt("total_opt")
                    if(jWin.has("total_opt")) {
                        val win = jWin.getInt("total_opt")
                        Log.d("vietnb", "hehehe" + win)
//                        view.segmentPredict.rateSegmentPredict(0, 0, 0)
//                        view.segmentPredict.setBackgroundColor(R.color.browser_actions_divider_color)
                    }

//
//
//
//
//                    val draw = predic_opts.getJSONObject(1)//.getInt("total_opt")
//                    val lose = predic_opts.getJSONObject(2)//.getInt("total_opt")
////                    val win = JSONObject(predic_opts[0].toString()).getInt("total_opt")
////                    val draw = JSONObject(predic_opts[1].toString()).getInt("total_opt")
////                    val lose = JSONObject(predic_opts[2].toString()).getInt("total_opt")
////
//                    view.segmentPredict.rateSegmentPredict(0, 0, 0)
                }
            }

            var optIDUser: Int = -1

            val dataPreference:DataPreference = DataPreference(view.context)
            val array = dataPreference.getValueArray(PREFERENCE.BETTINGMATCH)
            var matchID = 0
            if(matchinfo.has("id")) {
                matchID = matchinfo.getInt("id")
            }

            for (i in 0 until array.size) {
                val jObject = array[i]
                if(jObject.has("match_id")) {
                    val match_id = jObject.getInt("match_id")
                    if(match_id == matchID)
                    {
                        optIDUser = jObject.getInt("opt_id")
                        break
                    }
                }
            }

            var status = 0
            if(matchinfo.has("status")) {
                status = matchinfo.getInt("status")
            }

            if(matchinfo.has("tournament")) {
                val tournament = matchinfo.getJSONObject("tournament")
                if(tournament.has("fullname")) {
                    view.txtTenGiai.text = tournament.getString("fullname")
                }
            }

            if(matchinfo.has("circle")) {
                view.txtVong.text = "Vòng " + matchinfo.getString("circle")
            }

            if(matchinfo.has("home_club")) {
                val home_club = matchinfo.getJSONObject("home_club")
                if(home_club.has("name")) {
                    view.txtHomeName.text = home_club.getString("name")
                }

                if(home_club.has("logo")) {
                    val cover = home_club.getString("logo")

                    val radius = 20; // corner radius, higher value = more rounded
                    val margin = 0; // crop margin, set to 0 for corners with no crop
                    Glide.with(view.imgLogoHome.context)
                        .load(cover)
                        .transform(RoundedCornersTransformation(radius, margin))
                        .placeholder(R.drawable.thumbnews)
                        .into(view.imgLogoHome)
                }
            }

            if(matchinfo.has("away_club")) {
                val away_club = matchinfo.getJSONObject("away_club")
                if(away_club.has("name")) {
                    view.txtAwayName.text = away_club.getString("name")
                }

                if(away_club.has("logo")) {
                    val cover = away_club.getString("logo")

                    val radius = 20; // corner radius, higher value = more rounded
                    val margin = 0; // crop margin, set to 0 for corners with no crop
                    Glide.with(view.imgLogoAway.context)
                        .load(cover)
                        .transform(RoundedCornersTransformation(radius, margin))
                        .placeholder(R.drawable.thumbnews)
                        .into(view.imgLogoAway)
                }
            }

            ////// ti so
            var home_scored = 0
            if(matchinfo.has("home_scored")) {
                home_scored = matchinfo.getInt("home_scored")
            }

            var away_scored = 0
            if(matchinfo.has("away_scored")) {
                away_scored = matchinfo.getInt("away_scored")
            }

            view.txtScore.text = "$home_scored : $away_scored"

            visibleItem(view, matchinfo)

//            if(listener != null) {
//                    this.listener!!.clickJoinPredict_LoopBallAdapterListener(position)
//                }

//            view.txtJoinPredict.setOnClickListener {
//                if(status == 2) { //Neu tran da ket thuc
//                    var total_betted = 0
//                    if(matchinfo.has("total_betted")) {
//                        total_betted = matchinfo.getInt("total_betted")
//                    }
//                    view.txtThoiGian.text = "Thống kê $total_betted dự đoán"
//                    view.txtJoinPredict.text = "Xem trận đấu"
//                    view.txtScore.visibility = View.INVISIBLE
//                    view.segmentPredict.visibility = View.VISIBLE
//
//                    view.txtVong.visibility = View.INVISIBLE
//                    view.imgClose.visibility = View.VISIBLE
//
//
//                } else {
//                    //Bam vao tham gia du doan 24h
//                    view.txtVong.visibility = View.INVISIBLE
//                    view.txtVS.visibility = View.INVISIBLE
//                    view.imgClose.visibility = View.VISIBLE
//                    view.txtScore.visibility = View.INVISIBLE
//
//                    if(optIDUser == -1) {//User chua chon hien thi cho chon
//                        view.txtHoaCenter.visibility = View.VISIBLE
//                        view.txtThoiGian.text = "Dự đoán kết quả trận đấu"
//                        view.txtJoinPredict.text = "Xem trận đấu"
//                    }
//                    else { //User da du doan show segment
//                        view.segmentPredict.visibility = View.VISIBLE
//
//                        var total_betted = 0
//                        if(matchinfo.has("total_betted")) {
//                            total_betted = matchinfo.getInt("total_betted")
//                        }
//                        view.txtThoiGian.text = "Thống kê $total_betted dự đoán"
//                        view.txtJoinPredict.text = "Sửa dự đoán"
//                    }
//                }
////                rateSegmentPredict(0, 0, 0, view.segmentPredict)
//                if(listener != null) {
//                    //this.listener!!.clickJoinPredict_LoopBallAdapterListener(position)
//                }
//            }
//
//            view.imgClose.setOnClickListener {
//                //Bam vao icon X
//                view.imgClose.visibility = View.INVISIBLE
//                view.txtVong.visibility = View.VISIBLE
//
//                visibleItem(view, matchinfo) // hien thi lai nhu ban dau
//            }
//
//            view.imgLogoHome.setOnClickListener{
//                if(status == 0) {
//                    if(view.txtHoaCenter.visibility == View.VISIBLE) {
//                        //goi ham betting
//                        var optID = 1
//                        var hopeStar = 0
//                        bettingMatch(matchinfo, optID, hopeStar, view)
//                    }
//                }
//            }
//
//            view.txtHoaCenter.setOnClickListener{
//                if(status == 0) {
//                    if(view.txtHoaCenter.visibility == View.VISIBLE) {
//                        //goi ham betting
//                        var optID = 2
//                        var hopeStar = 0
//                        bettingMatch(matchinfo, optID, hopeStar, view)
//                    }
//                }
//
//            }
//
//            view.imgLogoAway.setOnClickListener{
//                if(status == 0) {
//                    if(view.txtHoaCenter.visibility == View.VISIBLE) {
//                        //goi ham betting
//                        var optID = 3
//                        var hopeStar = 0
//                        bettingMatch(matchinfo, optID, hopeStar, view)
//                    }
//                }
//            }
        }
    }

    fun rateSegmentPredict(win: Int, draw: Int, lose: Int, view: segmentpredict){
        val lPWin = view.viewWin.layoutParams
        val lPDraw = view.viewDraw.layoutParams
        val lPLose = view.viewLose.layoutParams

        Log.d("vietnb", "co vao manh ong oi")
        if(win == 0 && draw == 0 && lose == 0)
        {
            Log.d("vietnb", "tinh toan lai roi ma nhi")
            lPWin.width = view.width/3
            view.viewWin.layoutParams = lPWin

            lPDraw.width = view.width/3
            view.viewDraw.layoutParams = lPDraw
        }
        else {
            if(win == 0 && draw == 0) {
                lPWin.width = 10
                view.viewWin.layoutParams = lPWin

                lPDraw.width = 10
                view.viewDraw.layoutParams = lPDraw
            } else if(win == 0 && lose == 0) {
                lPWin.width = 10
                view.viewWin.layoutParams = lPWin

                lPLose.width = 10
                view.viewLose.layoutParams = lPLose
            } else if(draw == 0 && lose == 0) {
                lPDraw.width = 10
                view.viewDraw.layoutParams = lPDraw

                lPLose.width = 10
                view.viewLose.layoutParams = lPLose
            }
        }
    }

    fun trimTimeMatch(matchinfo: JSONObject) : String{
        if(matchinfo.has("begin")) {
            val begin = matchinfo["begin"].toString()
            val timeMatch = begin.toLong()
            val date = Date(timeMatch)
            var formatter = SimpleDateFormat("dd/MM")
            val today = formatter.format(date)
            formatter = SimpleDateFormat("hh:mm")
            val time = formatter.format(date)
            formatter = SimpleDateFormat("EEEE")
            var day = formatter.format(date)
            if(day == "Monday") day = "T2"
            else if(day == "Tuesday") day = "T3"
            else if(day == "Wednesday") day = "T4"
            else if(day == "Thursday") day = "T5"
            else if(day == "Friday") day = "T6"
            else if(day == "Saturday") day = "T7"
            else if(day == "Monday") day = "CN"

            return "$day, $today, $time"
        }
        return ""
    }

    fun visibleItem(view: PredictBallView, matchinfo: JSONObject) {
        var status = 0
        if(matchinfo.has("status")) {
            status = matchinfo.getInt("status")
        }

        var betting_id = 0
        if(matchinfo.has("betting_id")) {
            betting_id = matchinfo.getInt("betting_id")
        }

        var total_betted = 0
        if(matchinfo.has("total_betted")) {
            total_betted = matchinfo.getInt("total_betted")
        }

        var time_match = trimTimeMatch(matchinfo)
        view.txtThoiGian.text = time_match

        view.txtJoinPredict.text = "Tham gia dự đoán cùng 24H"
        view.txtScore.visibility = View.INVISIBLE
        view.txtThoiGian.visibility = View.VISIBLE
        view.txtVS.visibility = View.VISIBLE
        view.txtHoaCenter.visibility = View.INVISIBLE
//        view.segmentPredict.visibility = View.INVISIBLE

        if(status == 0) { //chua dien ra
            if(betting_id > 0) {
                view.txtJoinPredict.text = "Dự đoán ngay"
            }
        } else if(status == 1) { //dang dien ra
            view.txtScore.visibility = View.VISIBLE
            view.txtThoiGian.visibility = View.INVISIBLE
            view.txtVS.visibility = View.INVISIBLE

            if(betting_id > 0) {
                view.txtJoinPredict.text = "Xem $total_betted dự đoán"
                view.txtThoiGian.text = "Live"
            }
        } else if(status == 2) { //da ket thuc
            view.txtScore.visibility = View.VISIBLE
            view.txtVS.visibility = View.INVISIBLE

            view.txtJoinPredict.text = "Xem $total_betted dự đoán"
            view.txtThoiGian.text = "Đã kết thúc"
        }
    }

    fun bettingMatch(matchinfo: JSONObject, optID: Int, hopeStar: Int, view: PredictBallView) {

        var matchID = 0
        if(matchinfo.has("id")) {
            matchID = matchinfo.getInt("id")
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.appnews24h.com")
            .build()

        val service = retrofit.create(APIService::class.java)

        val fieldMap = JSONObject()
        fieldMap.put("match_id", matchID)
        fieldMap.put("opt_id", optID)
        fieldMap.put("hope_star", hopeStar)

        val jsonObjectString = fieldMap.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.postBetting(Global.getHeaderMap(), requestBody)

            withContext(Dispatchers.Main) {

                Log.e("vietnb", response.toString())
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    val jsonObject = JSONObject(prettyJson)
                    if(jsonObject != null) {
                        if (jsonObject.get("code").toString() == "200") {
//                            view.segmentPredict.visibility = View.VISIBLE
                            view.txtHoaCenter.visibility = View.INVISIBLE
                            view.txtJoinPredict.text = "Sửa dự đoán"
                            var total_betted = 0
                            if(matchinfo.has("total_betted")) {
                                total_betted = matchinfo.getInt("total_betted")
                            }
                            view.txtThoiGian.text = "Thống kê $total_betted dự đoán"

                            if(jsonObject.has("opt_statistic")) {
                                val sharedPreference:DataPreference = DataPreference(view.context)

                                val jObject = JSONObject()
                                jObject.put("match_id", matchID)
                                jObject.put("opt_id", optID)
                                sharedPreference.save(PREFERENCE.BETTINGMATCH, jObject.toString())
                            }
                        }
                    }
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
}
