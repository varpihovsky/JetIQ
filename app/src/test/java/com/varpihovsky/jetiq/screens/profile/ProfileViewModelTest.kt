package com.varpihovsky.jetiq.screens.profile

import io.mockk.mockk
import org.junit.Before

class ProfileViewModelTest {
    private lateinit var profileViewModel: ProfileViewModel
    private val profileInteractor = mockk<ProfileInteractor>()

    @Before
    fun setup() {
        profileViewModel = ProfileViewModel(profileInteractor)
    }
}