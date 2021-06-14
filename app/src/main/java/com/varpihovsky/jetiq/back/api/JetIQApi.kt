package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.back.dto.ProfileDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JetIQApi {
    @GET("api.php")
    fun authorize(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Call<ProfileDTO>

    @GET("api.php")
    fun authorizeDebug(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Call<ResponseBody>

    @GET("api.php")
    fun getMessages(
        @Query("login") login: String,
        @Query("pwd") password: String,
        @Query("get_mess") messageParam: Int = 1
    )

    @GET("api.php")
    fun getSuccessJournal(
        @Query("login") login: String,
        @Query("pwd") password: String,
        @Query("action") action: String = "journ_list"
    ): Call<ResponseBody>

    @GET("api.php")
    fun getSubjectDetails(
        @Query("login") login: String,
        @Query("pwd") password: String,
        @Query("card_id") cardId: Int,
        @Query("action") action: String = "journ_view"
    ): Call<ResponseBody>
}