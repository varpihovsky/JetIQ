package com.varpihovsky.jetiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.varpihovsky.core.appbar.AppbarManager
import com.varpihovsky.core.dataTransfer.ViewModelDataTransferManager
import com.varpihovsky.core_nav.main.NavigationControllerStorage
import com.varpihovsky.jetiq.ui.compose.Root
import com.varpihovsky.jetiq.ui.theme.JetIQTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appbarManager: AppbarManager

    @Inject
    lateinit var viewModelDataTransferManager: ViewModelDataTransferManager

    @Inject
    lateinit var navigationControllerStorage: NavigationControllerStorage

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<NavigationViewModel>()

            JetIQTheme {
                Root(
                    navigationViewModel = viewModel,
                    appbarManager = appbarManager,
                    navigationControllerStorage = navigationControllerStorage
                )
            }
        }
    }


}