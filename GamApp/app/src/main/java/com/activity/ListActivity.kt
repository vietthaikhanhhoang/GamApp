package com.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.api.APIService
import com.barservicegam.app.R
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class ListActivity : AppCompatActivity() {
    //var countBackground:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getListNews()
        //postComment()

        //val btnDetect = findViewById<Button>(R.id.btnDetect)

        // Check if app is in background
//        ProcessLifecycleOwner.get().getLifecycle().getCurrentState() == Lifecycle.State.CREATED
//        ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)

    }

    override fun onStart() {
        super.onStart()

        Log.d("tag list", "Len foreground")
    }

    override fun onStop() {
        super.onStop()
        Log.d("tag list", "Xuong background")
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["device_id"] = "eebab29c-e540-4d0f-8fab-89f98221f05f"
        headerMap["APP_VERSION"] = "1.0.51"
        headerMap["APP_ID"] = "tinhay"
        headerMap["TOKEN"] = "0ed94bd8dcab8ea018df133da9180a80"
        headerMap["Content-Type"] = "application/json; charset=UTF-8"
        return headerMap
    }

    private fun getParamComment(): Map<String, String> {
        val fieldMap = mutableMapOf<String, String>()
        fieldMap["lid"] = "news:22:32:cb410c76eeab5cc027780dc40535f816"
        fieldMap["content"] = "Làm ăn kiểu gì cháy suốt"
        fieldMap["id"] = "1247334068942181"
        return fieldMap
    }

    fun getListNews() {
        Log.d("tag 2", "Goi retrofit")

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.appnews24h.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", "Jack")
        jsonObject.put("salary", "3540")
        jsonObject.put("age", "23")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
//            val response = service.createEmployee(getHeaderMap(), requestBody)
            //val response = service.getListNews(null, "0", 0, getHeaderMap())

//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//
//                    // Convert raw JSON to pretty JSON using GSON library
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                        )
//                    )
//
//
//                    Log.d("Pretty Printed JSON :", prettyJson)
//
//                    val jsonObject = JSONObject(prettyJson)
//
//                    if(jsonObject != null)
//                    {
//                        if (jsonObject.get("code").toString() == "200")
//                        {
//                            //Log.d("Pretty Printed JSON :", "hay vao day")
//                            if (jsonObject.getJSONArray("linfos") != null)
//                            {
//                                val linfos = jsonObject.getJSONArray("linfos")
//                                if(linfos.length() > 0)
//                                {
//                                    val infos = JSONObject(linfos[0].toString())
//                                    val doc = JSONObject(infos.get("Doc").toString())
//                                    val art = JSONObject(doc.get("Art").toString())
//
//                                    Log.d("tag 2", "so phan tu ${linfos.length()}")
//                                    Log.d("tag 2", "title: ${art.getString("title")}")
//                                }
//                            }
//                        }
//                    }
//
//                } else {
//
//                    //Log.e("RETROFIT_ERROR", response.code().toString())
//
//                }
//            }
        }
    }

    fun postComment() {
        Log.d("tag 2", "Goi retrofit")

        // Create Retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.appnews24h.com")
                .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("lid", "news:22:32:cb410c76eeab5cc027780dc40535f816")
        jsonObject.put("id", "1247334068942181")
        jsonObject.put("content", "Hay quá")

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.postComment(getHeaderMap(), getParamComment())

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )


                    Log.d("tag comment :", prettyJson)
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun postTracking() {
        Log.d("tag 2", "Goi tracking")

        // Create Retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.appnews24h.com")
                .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("lid", "news:22:32:cb410c76eeab5cc027780dc40535f816")
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.postTracking(getHeaderMap(), requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )


                    Log.d("tag tracking :", prettyJson)
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
}