package com.varpihovsky.feature_contacts

import com.varpihovsky.core.di.viewModel
import com.varpihovsky.feature_contacts.addition.ContactAdditionViewModel
import org.koin.dsl.module

object ContactsModule {
    val module = module {
        viewModel { ContactsViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { ContactAdditionViewModel(get(), get(), get(), get(), get(), get()) }
    }
}