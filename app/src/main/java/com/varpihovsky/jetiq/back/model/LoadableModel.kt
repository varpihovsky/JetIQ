package com.varpihovsky.jetiq.back.model

import androidx.lifecycle.MutableLiveData

interface LoadableModel {
    val isLoading: MutableLiveData<Boolean>

}