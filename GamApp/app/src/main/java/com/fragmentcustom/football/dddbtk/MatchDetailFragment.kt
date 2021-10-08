package com.fragmentcustom.football.dddbtk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.APIService
import com.api.Global
import com.barservicegam.app.R
import com.customadapter.DetailNewsAdapter
import com.customadapter.PredictBallView
import com.customadapter.ball.OptionGuessAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import data.DataPreference
import data.PREFERENCE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MatchDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var isSelectStar:Boolean = false

    lateinit var rclView:RecyclerView
    var optionGuessAdapter = OptionGuessAdapter(JSONArray(), 0, JSONObject())

    lateinit var btnLuu: Button
    lateinit var txtDiem: TextView
    lateinit var txtDiemLock: TextView
    lateinit var txtNumGuess: TextView
    lateinit var imgStar: ImageView
    lateinit var txtStar: TextView

    var matchID:Int = 0

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
        val view = inflater.inflate(R.layout.fragment_match_detail, container, false)

        rclView = view.findViewById(R.id.rclView)
        btnLuu = view.findViewById(R.id.btnLuu)
        txtDiem = view.findViewById(R.id.txtDiem)
        txtDiemLock = view.findViewById(R.id.txtDiemLock)
        txtNumGuess = view.findViewById(R.id.txtNumGuess)
        imgStar = view.findViewById(R.id.imgStar)
        txtStar = view.findViewById(R.id.txtStar)

        imgStar.visibility = View.GONE
        txtStar.visibility = imgStar.visibility

        rclView.adapter = optionGuessAdapter
        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
        rclView.layoutManager = mLayoutManager

        refreshData()

        btnLuu.setOnClickListener {
            var selectid = optionGuessAdapter.selectId
            bettingMatch(JSONObject(), selectid, 0)
        }

        imgStar.setOnClickListener {
            if(!isSelectStar) {
                imgStar.setImageResource(R.drawable.starcolor)
            } else {
                imgStar.setImageResource(R.drawable.ic_star_gray)
            }
            isSelectStar = !isSelectStar
        }

        btnLuu.setBackgroundColor(resources.getColor(R.color.grayColor, null))

        return view
    }

    fun refreshData(){
        var matchInfo = JSONObject()
        if(param1 != "") {
            matchInfo = JSONObject(param1)

            if (matchInfo.has("id")) {
                matchID = matchInfo.getInt("id")
            }
        }

        val retrofit: APIService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.appnews24h.com")
            .build()
            .create(APIService::class.java)

        val response = retrofit.getGuessStatistic(matchID, Global.getHeaderMap())
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
                            var total_score = 0
                            if(jsonObject.has("total_score")) {
                                total_score = jsonObject.getInt("total_score")
                            }
                            txtDiem.text = "Điểm của bạn: $total_score"

                            var total_lock = 0
                            if(jsonObject.has("total_lock")) {
                                total_lock = jsonObject.getInt("total_lock")
                            }
                            txtDiemLock.text = "Điểm bị khoá: $total_lock"

                            var totalRate = 0
                            if(jsonObject.has("total_count")) {
                                totalRate = jsonObject.getInt("total_count")
                            }
                            txtNumGuess.text = "$totalRate dự đoán"

                            if(jsonObject.has("opt_id")) {
                                val opt_id = jsonObject.getInt("opt_id")
                                if(opt_id > 0) {
                                    //da du doan
                                    optionGuessAdapter.selectId = opt_id
                                    btnLuu.setBackgroundColor(resources.getColor(R.color.saveGuess, null))
                                }
                            }

                            if (matchInfo.has("hope_star")) {
                                imgStar.visibility = View.VISIBLE
                                txtStar.visibility = imgStar.visibility
                            }

                            if (jsonObject.has("opt_statistic"))
                            {
                                val opt_statistic = jsonObject.getJSONArray("opt_statistic")

                                optionGuessAdapter.mList = opt_statistic
                                optionGuessAdapter.totalRate = totalRate
                                optionGuessAdapter.matchInfo = matchInfo
                                optionGuessAdapter.notifyDataSetChanged()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MatchDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MatchDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun bettingMatch(matchinfo: JSONObject, optID: Int, hopeStar: Int) {
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

                            Log.d("vietnb", "thong tin tra ve: $jsonObject")


                            val opt_statistic = jsonObject.getJSONArray("opt_statistic")
                            val winObject = JSONObject(opt_statistic[0].toString())
                            val drawObject = JSONObject(opt_statistic[1].toString())
                            val loseObject = JSONObject(opt_statistic[2].toString())

                            val total_optWin = winObject.getInt("total_opt")
                            val total_optDraw = drawObject.getInt("total_opt")
                            val total_optLose = loseObject.getInt("total_opt")
                            var totalRate = total_optWin + total_optDraw + total_optLose
                            txtNumGuess.text = "$totalRate dự đoán"

                            val rateWin = (total_optWin*100f/totalRate).toInt().toString()
                            val rateDraw = (total_optDraw*100f/totalRate).toInt().toString()
                            val rateLose = (total_optLose*100f/totalRate).toInt().toString()

                            Toast.makeText(requireContext(), "Bạn đã lưu dự đoán thành công!", Toast.LENGTH_SHORT).show()

                            if(jsonObject.has("opt_statistic")) {
                                val sharedPreference: DataPreference = DataPreference(requireContext())

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