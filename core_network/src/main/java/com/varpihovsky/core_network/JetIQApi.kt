package com.varpihovsky.core_network

import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import com.varpihovsky.repo_data.ProfileDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface JetIQApi {
    @GET("api.php")
    suspend fun authorize(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Result<ProfileDTO>

    @GET("api.php")
    suspend fun logout(
        @Query("logout") logout: Int = 1
    ): EmptyResult

    @GET("api.php")
    suspend fun getSuccessJournal(
        @Header("Cookie") cookie: String,
        @Query("action") action: String = "journ_list"
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getSubjectDetails(
        @Header("Cookie") cookie: String,
        @Query("card_id") cardId: Int,
        @Query("action") action: String = "journ_view"
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getMarkbookSubjects(
        @Header("Cookie") cookie: String,
        @Query("markbook") markbook: Int = 1
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getMessages(
        @Header("Cookie") cookie: String,
        @Query("get_mess") getMessage: Int = 1
    ): Result<List<MessageDTO>>

    @GET("api.php")
    suspend fun getFaculties(
        @Query("msg") msg: Int = 1,
        @Query("facult_list") facultiesList: Int = 1
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getGroupByFaculty(
        @Query("msg") msg: Int = 1,
        @Query("group_list") groupList: Int = 1,
        @Query("f_id") facultyId: Int
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getStudentByGroup(
        @Query("msg") msg: Int = 1,
        @Query("stud_list") studentList: Int = 1,
        @Query("group_id") groupId: Int
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getTeachersByQuery(
        @Query("msg") msg: Int = 1,
        @Query("t_list") teachersList: Int = 1,
        @Query("t_name") query: String
    ): Result<ResponseBody>

    @GET("api.php")
    suspend fun getCsrf(
        @Header("Cookie") cookie: String,
        @Query("msg") msg: Int = 1,
        @Query("pre") pre: Int = 1
    ): Result<CSRF>

    @GET("api.php")
    suspend fun sendMessage(
        @Header("Cookie") cookie: String,
        @Query("msg") msg: Int = 1,
        @Query("send") send: Int = 1,
        @Query("s_id") receiverId: Int,
        @Query("isteacher") isTeacher: Int,
        @Query("message") message: String,
        @Query("csrf") csrf: String
    ): EmptyResult
}