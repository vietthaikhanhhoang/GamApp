package com.api

import android.content.Context
import android.util.Log
import com.bumptech.glide.util.Util
import data.DataPreference
import data.PREFERENCE
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

object Global {
    init {
        println("Global Singleton class invoked.")
    }

    fun dataFake():JSONArray {
        var mList = JSONArray()
        val jVideo1 = JSONObject()
        jVideo1.put("name", "Thời sự")
        jVideo1.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/vaccine-1630122489-1630122501-9049-1630122565.jpg?w=680&h=408&q=100&dpr=2&fit=crop&s=pvk3mRh5P0gEVLnwH_fdGg")
        jVideo1.put("video", "http://admin.antt.vn/upload/video/2021/08/28/mot-ngay-theo-chan-nhung-co-gai-tren-chuyen-xe-mai-tang-0-dong-song-dep-mua-dich-zingnews.vn-convert-video-online.com_28082021140929.mp4")
        mList.put(jVideo1)

        val jVideo2 = JSONObject()
        jVideo2.put("name", "Thể thao")
        jVideo2.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/Covid19oando-1630126157-3342-1630126350.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=wFXcF6Tp2CUowCz9t2p8-w")
        jVideo2.put("video", "https://cdn.eva.vn/upload/3-2021/videoclip/2021-08-28/1630116282-thi.mp4")
        mList.put(jVideo2)

        val jVideo3 = JSONObject()
        jVideo3.put("name", "Giải trí - Gia ")
        jVideo3.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/CRAM2JPG-1630125420-5695-1630125519.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=m4DHHQLQWyuw-_hqU36G2w")
        jVideo3.put("video", "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-08-27/1630057050-b.m3u8")
        mList.put(jVideo3)

        val jVideo5 = JSONObject()
        jVideo5.put("name", "Giáo dục")
        jVideo5.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/thanhthanlau-1630122666-7104-1630126290.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=Q4Rv-HIYgRgKWba1dXZpTQ")
        jVideo5.put("video", "http://admin.antt.vn/upload/video/2021/08/28/mot-ngay-theo-chan-nhung-co-gai-tren-chuyen-xe-mai-tang-0-dong-song-dep-mua-dich-zingnews.vn-convert-video-online.com_28082021140929.mp4")
        mList.put(jVideo5)

        val jVideo6 = JSONObject()
        jVideo6.put("name", "Thời sự")
        jVideo6.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/vaccine-1630122489-1630122501-9049-1630122565.jpg?w=680&h=408&q=100&dpr=2&fit=crop&s=pvk3mRh5P0gEVLnwH_fdGg")
        jVideo6.put("video", "http://admin.antt.vn/upload/video/2021/08/28/mot-ngay-theo-chan-nhung-co-gai-tren-chuyen-xe-mai-tang-0-dong-song-dep-mua-dich-zingnews.vn-convert-video-online.com_28082021140929.mp4")
        mList.put(jVideo6)

        val jVideo7 = JSONObject()
        jVideo7.put("name", "Thể thao")
        jVideo7.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/Covid19oando-1630126157-3342-1630126350.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=wFXcF6Tp2CUowCz9t2p8-w")
        jVideo7.put("video", "https://cdn.eva.vn/upload/3-2021/videoclip/2021-08-28/1630116282-thi.mp4")
        mList.put(jVideo7)

        val jVideo8 = JSONObject()
        jVideo8.put("name", "Giải trí - Gia ")
        jVideo8.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/CRAM2JPG-1630125420-5695-1630125519.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=m4DHHQLQWyuw-_hqU36G2w")
        jVideo8.put("video", "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-08-27/1630057050-b.m3u8")
        mList.put(jVideo8)

        val jVideo9 = JSONObject()
        jVideo9.put("name", "Giáo dục")
        jVideo9.put("cover", "https://i1-vnexpress.vnecdn.net/2021/08/28/thanhthanlau-1630122666-7104-1630126290.jpg?w=220&h=132&q=100&dpr=2&fit=crop&s=Q4Rv-HIYgRgKWba1dXZpTQ")
        jVideo9.put("video", "http://admin.antt.vn/upload/video/2021/08/28/mot-ngay-theo-chan-nhung-co-gai-tren-chuyen-xe-mai-tang-0-dong-song-dep-mua-dich-zingnews.vn-convert-video-online.com_28082021140929.mp4")
        mList.put(jVideo9)

        return mList
    }

    var variableName = "I am Var"
    fun printVarName(){
        println(variableName)
    }

    fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["device_id"] = "0ef50568-d402-472f-994e-bea239532a90"
        headerMap["APP_VERSION"] = "1.0.89"
        headerMap["APP_ID"] = "tinhay"
        headerMap["TOKEN"] = "8cd7ac6bebe1255d96ee4353339b4641"
        headerMap["Content-Type"] = "application/json; charset=UTF-8"
        return headerMap
    }

    lateinit var arrayWebsite: JSONArray

    fun getNameWebsite(websiteID: Int, context: Context) : String {
        var name = "Tin Mới 24h"
        val sharedPreference: DataPreference = DataPreference(context)
        var mapIDNameWebsite = sharedPreference.getValueMap(PREFERENCE.MAPIDNAMEWEBSITE)
        if(mapIDNameWebsite.containsKey(websiteID)) {
            return mapIDNameWebsite?.get(websiteID).toString()
        }

//        if(arrayWebsite != null) {
//            if(arrayWebsite?.length() > 0) {
//                for (i in 0 until arrayWebsite?.length()) {
//                    val website = JSONObject(arrayWebsite[i].toString())
//                    if(website.has("id")) {
//                        val id = website["id"]
//                        if(id == websiteID) {
//                            if(website.has("name")) {
//                                name =  website.getString("name")
//                                break
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return name
    }

    fun getNameCategory(websiteID: Int, categoryID: Int) : String {
        var name = "Tổng hợp"

//        if(arrayWebsite != null) {
//            if(arrayWebsite?.length() > 0) {
//                for (i in 0 until arrayWebsite?.length()) {
//                    val website = JSONObject(arrayWebsite[i].toString())
//                    if(website.has("id")) {
//                        val id = website["id"]
//                        if(id == websiteID) {
//                            if(website.has("name")) {
//                                if(website.has("categorys")) {
//                                    val categorys =  website.getJSONArray("categorys")
//                                    for (j in 0 until categorys.length()) {
//                                        val category = JSONObject(categorys[j].toString())
//                                        if(category.has("id")) {
//                                            val catid = category.getInt("id")
//                                            if(catid == categoryID) {
//                                                if(category.has("name")) {
//                                                    name = category.getString("name")
//                                                    break
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
//                                break
//                            }
//                        }
//                    }
//                }
//            }
//        }

        return name
    }

    fun currentTimeSecUTC(timestamp: Long) : String{
//        val timestamp = 1629121740000 // 25 July 2018 10:59:59 UTC
        val date = Date(timestamp)

        var result: String?

        val timeIntervalInHours: Long = (Date().getTime() - date.getTime())/3600/1000
        val timeIntervalInMinutes: Long = (Date().getTime() - date.getTime())/60/1000

        if (timeIntervalInMinutes < 1) {
            result = "1 phút trước";
        }
        else if (timeIntervalInMinutes <= 50 && timeIntervalInMinutes>=1) {
            result = timeIntervalInMinutes.toString() + " phút"
        }
        else if (timeIntervalInMinutes <= 110) {
            result = "1 giờ"
        }
        else if (timeIntervalInMinutes <= 170) {
            result = "2 giờ"
        }
        else if (timeIntervalInHours <= 24 && timeIntervalInMinutes>120) {
            result = timeIntervalInHours.toString() + " giờ"
        }
        else if (timeIntervalInHours > 24 && timeIntervalInHours<=720) {
            result = (timeIntervalInHours/24).toString() + " ngày"
        }
        else
        {
            result = "Hơn 30 ngày"
        }

        return result
    }
}