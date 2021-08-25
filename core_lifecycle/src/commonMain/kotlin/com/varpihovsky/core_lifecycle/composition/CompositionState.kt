package com.varpihovsky.core_lifecycle.composition

import androidx.compose.foundation.layout.PaddingValues

object LocalCompositionState {
    private var LocalCompositionState = CompositionState(
        paddingValues = PaddingValues(),
        currentMode = Mode.Mobile
    )

    val current get() = LocalCompositionState

    infix fun provides(state: CompositionState) {
        LocalCompositionState = state
    }
}

class CompositionState(val paddingValues: PaddingValues, val currentMode: Mode)

sealed class Mode {
    object Mobile : Mode()
    object Desktop : Mode()
}