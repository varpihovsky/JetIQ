package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.back.dto.ProfileDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface JetIQApi {
    @GET("api.php")
    fun authorize(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Call<ProfileDTO>

    @GET("api.php")
    fun getMessages(
        @Header("Cookie") cookie: String,
        @Query("get_mess") messageParam: Int = 1
    )

    @GET("api.php")
    fun getSuccessJournal(
        @Header("Cookie") cookie: String,
        @Query("action") action: String = "journ_list"
    ): Call<ResponseBody>

    @GET("api.php")
    fun getSubjectDetails(
        @Header("Cookie") cookie: String,
        @Query("card_id") cardId: Int,
        @Query("action") action: String = "journ_view"
    ): Call<ResponseBody>

    @GET("api.php")
    fun getMarkbookSubjects(
        @Header("Cookie") cookie: String,
        @Query("markbook") markbook: Int = 1
    ): Call<ResponseBody>
}