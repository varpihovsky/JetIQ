package com.varpihovsky.jetiq.back.api

import com.varpihovsky.jetiq.back.dto.MessageDTO
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
    fun logout(
        @Query("logout") logout: Int = 1
    ): Call<ResponseBody>

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

    @GET("api.php")
    fun getMessages(
        @Header("Cookie") cookie: String,
        @Query("get_mess") getMessage: Int = 1
    ): Call<List<MessageDTO>>

    @GET("api.php")
    fun getFaculties(
        @Query("msg") msg: Int = 1,
        @Query("facult_list") facultiesList: Int = 1
    ): Call<ResponseBody>

    @GET("api.php")
    fun getGroupByFaculty(
        @Query("msg") msg: Int = 1,
        @Query("group_list") groupList: Int = 1,
        @Query("f_id") facultyId: Int
    ): Call<ResponseBody>

    @GET("api.php")
    fun getStudentByGroup(
        @Query("msg") msg: Int = 1,
        @Query("stud_list") studentList: Int = 1,
        @Query("group_id") groupId: Int
    ): Call<ResponseBody>

    @GET("api.php")
    fun getTeachersByQuery(
        @Query("msg") msg: Int = 1,
        @Query("t_list") teachersList: Int = 1,
        @Query("t_name") query: String
    ): Call<ResponseBody>
}