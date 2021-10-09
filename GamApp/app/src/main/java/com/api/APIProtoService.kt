package com.api

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import model.PListingResponse
import okhttp3.OkHttpClient
import okhttp3.Request

object APIProtoService {
    val okHttpClient = OkHttpClient()
    val getListNews = Observable.just("https://api.appnews24h.com/news/v2.0/articles?count=24")
        .map{
            val request = Request.Builder()
                .header("APP_ID", "tinhay")
                .header("APP_VERSION", "1.0.87")
                .header("DEVICE_ID", "0ef50568-d402-472f-994e-bea239532a90")
                .header("DEVICE_MODEL", "IPHONE")
                .header("TOKEN", "8cd7ac6bebe1255d96ee4353339b4641")
                .url(it).build()
            val call = okHttpClient.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                val responseBody = response.body
                if (responseBody != null) {
                    val data = PListingResponse.ListingResponses.parseFrom(responseBody.byteStream())
                    Log.d("vietnb", "susu dep zai: $data")
                    return@map data
                }
            }
            return@map null
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}