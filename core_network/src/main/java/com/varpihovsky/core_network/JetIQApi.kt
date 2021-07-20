package com.varpihovsky.core_network

import com.varpihovsky.core_network.result.EmptyResult
import com.varpihovsky.core_network.result.Result
import com.varpihovsky.repo_data.CSRF
import com.varpihovsky.repo_data.MessageDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface JetIQApi {
    /**
     * Authorizes account if login and password right. To get responses from other commands need to
     * get session from headers. Login can be in custom or preregistered form like 00-00-000.
     * If login or password is not right returns json with "wrong login or password" under "session"
     * key.
     *
     * Have fields:
     *
     * "session" - not null only when user is authorized during response sending. You have to get it
     *  from response headers. If authorization incomplete it have "wrong login or password"
     *  value while others fields are empty.
     *
     * "id" - id of current user.
     *
     * "u_name" - full name of current user.
     *
     * "gr_id" - group id of current user.
     *
     * "gr_name" - name of group of current user. Example: "1ІСТ-20б"
     *
     * "course_num" - course number of current user.
     *
     * "d_id" - department id of current user.
     *
     * "d_name" - department name of current user.
     *
     * "email" - always empty.
     *
     * "dob" - always null.
     *
     * "photo_url" - photo url of current user.
     *
     * "f_id" - faculty id of current user.
     *
     * @param login login of user
     * @param password password of user
     *
     * @return Due to every key can be null returns [NullableProfile]
     */
    @GET("api.php")
    suspend fun authorize(
        @Query("login") login: String,
        @Query("pwd") password: String
    ): Result<NullableProfile>

    /**
     * Deletes session stored in server.
     *
     * @param cookie current user session.
     *
     * @return [EmptyResult]
     */
    @GET("api.php")
    suspend fun logout(
        @Header("Cookie") cookie: String,
        @Query("logout") logout: Int = 1
    ): EmptyResult

    /**
     * Returns numbered array of subjects. Need to parse.
     * To parse use method [deserializeSubjects][com.varpihovsky.core_network.parsers.deserializeSubjects].
     * Each element have number key and has following keys:
     *
     * "card_id" - id of the subject.
     *
     * "subject" - name of the subject.
     *
     * "sem" - semester of the subject.
     *
     * "t_name" - name of the teacher.
     *
     * "scale" - no idea what is that, but it has no use for now.
     *
     * "f_control" - type of control. For example modules or course work.
     *
     * @param cookie session of current user.
     *
     * @return [ResponseBody] to get json from it.
     *
     */
    @GET("api.php")
    suspend fun getSuccessJournal(
        @Header("Cookie") cookie: String,
        @Query("action") action: String = "journ_list"
    ): Result<ResponseBody>

    /**
     * Returns details of subject which you get from [getSuccessJournal] method.
     * Response have php array of tasks which you have to parse.
     * To parse them use [deserializeSubjectTasks][com.varpihovsky.core_network.parsers.deserializeSubjectTasks].
     *
     * Each task have following structure:
     *
     * "legend" - Full name of task specified by teacher.
     *
     * "num_mod" - number of module.
     *
     * "points" - points that current student got from this task.
     *
     * Whole subject has following structure:
     *
     * "sum1" - sum of points that student got for first module.
     *
     * "sum2" - sum of points that student got for second module.
     *
     * "mark1" - mark for first module. Specified in five-point system.
     *
     * "mark2" - mark for second module. Specified in five-point system.
     *
     * "h_press1" - hours during which current student wasn't present for first module.
     *
     * "h_press2" - hours during which current student wasn't present for second module.
     *
     * "for_pres1" - points that were provided for attendance for first module.
     *
     * "for_pres2" - points that were provided for attendance for second module.
     *
     * "total_prev" - points for same subject but for previous semester. If in previous semester
     *                there wasn't this discipline, field has value 0.
     *
     * "total" - total points for this subject.
     *
     * "ects" - total mark in ects mark system.
     *
     * @param cookie session of current user.
     * @param cardId id of subject got by [getSuccessJournal] method.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getSubjectDetails(
        @Header("Cookie") cookie: String,
        @Query("card_id") cardId: Int,
        @Query("action") action: String = "journ_view"
    ): Result<ResponseBody>

    /**
     * Returns php array of semesters. Each of array contains php array of subjects.
     * To parse it use [deserializeMarkbookSubjects][com.varpihovsky.core_network.parsers.deserializeMarkbookSubjects]
     * method.
     *
     * Each subject has following structure:
     *
     * "subj_name" - name of subject.
     *
     * "form" - evaluation form.
     *
     * "hours" - hours provided to subject.
     *
     * "credits" - amount of credits provided to subject.
     *
     * "total" - total amount of point for subject.
     *
     * "ects" - total mark in ects mark system.
     *
     * "mark" - total mark. Specified majorly in five-point system.
     *
     * "date" - date of exam for subject.
     *
     * "teacher" - full name of teacher.
     *
     * @param cookie session of current user.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getMarkbookSubjects(
        @Header("Cookie") cookie: String,
        @Query("markbook") markbook: Int = 1
    ): Result<ResponseBody>

    /**
     * Returns list of messages stored in server.
     *
     * Each message has following structure:
     *
     * "msg_id" - id of message.
     *
     * "id_from" - id of sender.
     *
     * "is_t_from" - has value 1 if sender is teacher and value 0 if sender is student.
     *
     * "time" - time of message sending. Specified in long.
     *
     * "body" - message body. Contains html tags.
     *
     * @param cookie session of current user
     *
     * @return list of [MessageDTO]
     */
    @GET("api.php")
    suspend fun getMessages(
        @Header("Cookie") cookie: String,
        @Query("get_mess") getMessage: Int = 1
    ): Result<List<MessageDTO>>

    /**
     * Returns list of faculties. Each faculty has id key and faculty name value.
     * To parse use [deserializeListItem][com.varpihovsky.core_network.parsers.deserializeListItem]
     * with [faculty][com.varpihovsky.core_network.parsers.ListItemJsonKey] key.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getFaculties(
        @Query("msg") msg: Int = 1,
        @Query("facult_list") facultiesList: Int = 1
    ): Result<ResponseBody>

    /**
     * Returns list of student groups by faculty id. Each group has id key and group name value.
     * To parse use [deserializeLiseItem][com.varpihovsky.core_network.parsers.deserializeListItem]
     * with [group][com.varpihovsky.core_network.parsers.ListItemJsonKey] key.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getGroupByFaculty(
        @Query("msg") msg: Int = 1,
        @Query("group_list") groupList: Int = 1,
        @Query("f_id") facultyId: Int
    ): Result<ResponseBody>

    /**
     * Returns list of students by group id. Each student has id key and full name value.
     * To parse use [deserializeListItem][com.varpihovsky.core_network.parsers.deserializeListItem]
     * with [student][com.varpihovsky.core_network.parsers.ListItemJsonKey] key.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getStudentByGroup(
        @Query("msg") msg: Int = 1,
        @Query("stud_list") studentList: Int = 1,
        @Query("group_id") groupId: Int
    ): Result<ResponseBody>

    /**
     * Returns teachers list by query. Each teacher has id key and full name value.
     * To parse use [deserializeListItem][com.varpihovsky.core_network.parsers.deserializeListItem]
     * with [teacher][com.varpihovsky.core_network.parsers.ListItemJsonKey] key.
     *
     * @return [ResponseBody] to get json from it.
     */
    @GET("api.php")
    suspend fun getTeachersByQuery(
        @Query("msg") msg: Int = 1,
        @Query("t_list") teachersList: Int = 1,
        @Query("t_name") query: String
    ): Result<ResponseBody>

    /**
     * Sends message to specified user.
     *
     * @param cookie session of current user.
     * @param receiverId id of message receiver.
     * @param isTeacher is receiver teacher.
     * @param message message body
     * @param csrf needed for message sending. To get use [getCsrf] method.
     *
     * @return [EmptyResult]
     */
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

    /**
     * Needed for message sending.
     *
     * @param cookie session of current user.
     *
     * @return [CSRF]
     */
    @GET("api.php")
    suspend fun getCsrf(
        @Header("Cookie") cookie: String,
        @Query("msg") msg: Int = 1,
        @Query("pre") pre: Int = 1
    ): Result<CSRF>
}