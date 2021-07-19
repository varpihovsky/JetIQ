package com.varpihovsky.jetiq.di

import com.varpihovsky.core.util.CoroutineDispatchers
import com.varpihovsky.core_repo.repo.ProfileRepo
import com.varpihovsky.core_repo.repo.SubjectRepo
import com.varpihovsky.jetiq.screens.profile.ProfileInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {
    @Provides
    fun provideProfileInteractor(
        dispatchers: CoroutineDispatchers,
        profileModel: ProfileRepo,
        subjectModel: SubjectRepo,
    ) = ProfileInteractor(
        dispatchers,
        profileModel,
        subjectModel,
    )
}