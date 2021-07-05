package com.varpihovsky.jetiq.screens.profile

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varpihovsky.jetiq.ui.dto.MarksInfo
import com.varpihovsky.jetiq.ui.dto.UIProfileDTO
import com.varpihovsky.jetiq.ui.dto.UISubjectDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor
) : ViewModel(), ProfileInteractor.Interactable {
    val data by lazy { Data() }
    val scrollState = ScrollState(0)

    private val profile = MutableLiveData<UIProfileDTO>()
    private val successMarksInfo = MutableLiveData<List<MarksInfo>>()
    private val successSubjects = MutableLiveData<List<UISubjectDTO>>()
    private val successChecked = MutableLiveData(false)
    private val markbookChecked = MutableLiveData(false)

    inner class Data {
        val profile: LiveData<UIProfileDTO> = this@ProfileViewModel.profile
        val successMarksInfo: LiveData<List<MarksInfo>> = this@ProfileViewModel.successMarksInfo
        val successSubjects: LiveData<List<UISubjectDTO>> = this@ProfileViewModel.successSubjects
        val successChecked: LiveData<Boolean> = this@ProfileViewModel.successChecked
        val markbookChecked: LiveData<Boolean> = this@ProfileViewModel.markbookChecked
    }

    init {
        profileInteractor.subscribe(this)
    }

    fun onSuccessToggle(checked: Boolean) {
        successChecked.value = checked
    }

    fun onMarkbookToggle(checked: Boolean) {
        markbookChecked.value = checked
    }


    override fun onProfileChange(uiProfileDTO: UIProfileDTO) {
        profile.postValue(uiProfileDTO)
    }

    override fun onSuccessMarksInfoChange(successMarksInfo: List<MarksInfo>) {
        this.successMarksInfo.postValue(successMarksInfo)
    }

    override fun onSuccessSubjectsChange(successSubjects: List<UISubjectDTO>) {
        this.successSubjects.postValue(successSubjects)
    }
}
