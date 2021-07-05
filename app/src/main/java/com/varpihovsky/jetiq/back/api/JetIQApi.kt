package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.back.dto.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JetIQApi {
    @GET("api.php")
    fun authorize(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Call<Profile>

    @GET("api.php")
    fun getMessages(
        @Query("login") login: String,
        @Query("pwd") password: String,
        @Query("get_mess") messageParam: Int = 1
    )
}