package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.screens.profile.ProfileInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object Interactors {
    @Provides
    fun provideProfileInteractor(profileModel: ProfileModel, subjectModel: SubjectModel) =
        ProfileInteractor(profileModel, subjectModel)
}