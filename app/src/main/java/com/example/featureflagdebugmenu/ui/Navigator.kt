package com.example.featureflagdebugmenu.ui

import androidx.compose.material.AlertDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The navigator is a middleware between the ViewModel and the view. The goal of the navigator is to
 * dispatch single shot events like navigation event or dialogs and snack bars
 *
 * To be able to differentiate between navigation event and the others, we use two different [SharedFlow]s
 * In Compose [AlertDialog]s are handled by the navigation compose library
 */
@Singleton
class Navigator @Inject constructor() {
    private val _navSharedFlow = MutableSharedFlow<FeatureFlagScreen>(extraBufferCapacity = 1)
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _singleEventsSharedFlow =
        MutableSharedFlow<FeatureFlagEvents>(extraBufferCapacity = 1)
    val singleEventsSharedFlow = _singleEventsSharedFlow.asSharedFlow()

    fun navigateTo(target: FeatureFlagScreen) {
        _navSharedFlow.tryEmit(target)
    }

    fun restartApp() {
        _singleEventsSharedFlow.tryEmit(FeatureFlagEvents(restartApp = true))
    }

    companion object {
        const val FEATURE_FLAGS_NAVIGATION_KEY = "feature_flags_navigation"
        const val RESTART_APP_KEY = "restart_app"
    }
}