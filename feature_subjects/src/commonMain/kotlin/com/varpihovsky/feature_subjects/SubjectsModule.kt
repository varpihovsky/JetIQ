package com.varpihovsky.feature_subjects

import com.varpihovsky.core.di.viewModel
import com.varpihovsky.feature_subjects.markbook.MarkbookSubjectViewModel
import com.varpihovsky.feature_subjects.success.SuccessSubjectViewModel
import org.koin.dsl.module

object SubjectsModule {
    val module = module {
        viewModel { MarkbookSubjectViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { SuccessSubjectViewModel(get(), get(), get(), get(), get(), get()) }
    }
}