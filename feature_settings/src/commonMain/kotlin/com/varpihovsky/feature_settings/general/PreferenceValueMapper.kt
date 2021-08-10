package com.varpihovsky.feature_settings.general

import com.varpihovsky.core_repo.repo.PreferencesKeys

internal expect object PreferenceValueMapper {
    inline fun <reified T> map(key: PreferencesKeys, initial: T): Any
}