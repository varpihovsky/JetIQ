package com.varpihovsky.ui_root.drawer

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.varpihovsky.core.log.i
import com.varpihovsky.core_lifecycle.DrawerController
import com.varpihovsky.core_repo.repo.ProfileRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DrawerComponent(
    componentContext: ComponentContext,
    private val onSettingsClick: () -> Unit,
    private val onMessagesClick: () -> Unit,
    private val onProfileClick: () -> Unit
) : ComponentContext by componentContext, KoinComponent, DrawerController {
    val temporalNavigationText: Value<String> by lazy { _temporalNavigationText }
    val temporalNavigationContent: StateFlow<@Composable (() -> Unit)?> by lazy { _temporalNavigationContent }
    val profileLink = get<ProfileRepo>().getProfile().map { it?.photoUrl ?: "" }
    val isShown: Value<Boolean> by lazy { _isShown }

    private val _temporalNavigationText = MutableValue("")
    private val _temporalNavigationContent = MutableStateFlow<@Composable (() -> Unit)?>(null)
    private val _isShown = MutableValue(false)

    private var time = Clock.System.now().epochSeconds

    override fun setNavigation(text: String, content: @Composable () -> Unit) {
        _temporalNavigationText.value = text
        _temporalNavigationContent.value = content
    }

    override fun clear() {
        _temporalNavigationText.value = ""
        _temporalNavigationContent.value = null
    }

    fun onSettingsClick() {
        toggle()
        onSettingsClick.invoke()
    }

    fun onMessagesClick() {
        toggle()
        onMessagesClick.invoke()
    }

    fun onProfileClick() {
        toggle()
        onProfileClick.invoke()
    }

    fun toggle() {
        if (Clock.System.now().epochSeconds - time > 0.1) {
            time = Clock.System.now().epochSeconds
            i("Toggled")
            _isShown.value = !isShown.value
        }
    }
}