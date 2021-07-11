package com.varpihovsky.jetiq.di

import com.varpihovsky.jetiq.back.model.ProfileModel
import com.varpihovsky.jetiq.back.model.SubjectModel
import com.varpihovsky.jetiq.screens.profile.ProfileInteractor
import com.varpihovsky.jetiq.system.ConnectionManager
import com.varpihovsky.jetiq.system.util.CoroutineDispatchers
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
        profileModel: ProfileModel,
        subjectModel: SubjectModel,
        connectionManager: ConnectionManager
    ) =
        ProfileInteractor(dispatchers, profileModel, subjectModel, connectionManager)
}