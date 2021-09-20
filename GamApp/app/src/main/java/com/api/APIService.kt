package com.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIService {
//    @GET("group/{id}/users") dang bi sai
//    fun groupList(@Path("id") groupId: Int, @QueryMap options: Map<String?, String?>?): Call<List<com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User?>?>?

    @GET("/news/v2.0/articles?sid=999&count=24&json=1")
    fun getListNews(@Query("cid") cid: String, @Query("lid") lid: String, @Query("realsize") realsize: Int, @HeaderMap headers: Map<String, String>) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("/news/commentv1/exe/cmt")
    suspend fun postComment(@HeaderMap headers: Map<String, String>, @FieldMap params: Map<String, String>) : Response<ResponseBody>

    @POST("/news/v2.0/tracking")
    suspend fun postTracking(@HeaderMap headers: Map<String, String>, @Body requestBody: RequestBody) : Response<ResponseBody>

    @GET("/ul/lichviet/data_20210213_1.zip")
    fun downloadFileWithDynamicUrlSync(): Call<ResponseBody>

    @GET("/news/v2.0/website")
    suspend fun getCategory(@HeaderMap headers: Map<String, String>) : Response<ResponseBody>

    @GET("/news/v2.0/relativev2?&json=1")
    fun getRelativeNews(@Query("lid") lid: String, @HeaderMap headers: Map<String, String>) : Call<ResponseBody>

    @GET("/fanclub/livescores?json=1")
    suspend fun getSlideMatch(@HeaderMap headers: Map<String, String>) : Response<ResponseBody>

    //tren postman ném {"match_id":20215339, "opt_id":2} vào Body: raw
    @POST("/game/user_betting?type=betting&json=1")
    suspend fun postBetting(@HeaderMap headers: Map<String, String>, @Body requestBody: RequestBody) : Response<ResponseBody>

    @GET("/news/v2.0/video/getallV2?count=15&json=1")
    fun getListVideos(@Query("subcid") subcid: Int?, @Query("lid") lid: String?, @Query("realsize") realsize: Int?, @HeaderMap headers: Map<String, String>) : Call<ResponseBody>

    @GET("/news/v2.0/video/getvideocategoriesV2")
    fun getCategoryVideo(@HeaderMap headers: Map<String, String>) : Call<ResponseBody>
}