package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.api.JetIQApi
import com.varpihovsky.jetiq.back.db.managers.ProfileDatabaseManager
import com.varpihovsky.jetiq.system.exceptions.ResponseUnsuccessfulException

class ProfileModel(
    private val jetIQApi: JetIQApi,
    private val profileDatabaseManager: ProfileDatabaseManager
) {
    fun login(login: String, password: String){
        val response = jetIQApi.authorize(login, password).execute()

        if(!response.isSuccessful || response.body() == null){
            throw ResponseUnsuccessfulException("Неправильний логін або пароль")
        }

        profileDatabaseManager.putProfile(response.body()!!)
    }

    fun getProfile() = profileDatabaseManager.getProfile()
}