package com.varpihovsky.jetiq

import android.util.Log
import androidx.lifecycle.ViewModel
import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.system.navigation.NavigationDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val profileModel: ProfileModel
) : ViewModel() {
    fun getStartDestination(): String =
        runBlocking {
            try {
                Log.d("Application", profileModel.getConfidential().first().toString())
                return@runBlocking NavigationDirections.profile.destination
            } catch (e: Exception) {
                return@runBlocking NavigationDirections.authentication.destination
            }
        }
}