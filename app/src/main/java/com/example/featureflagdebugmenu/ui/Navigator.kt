package com.example.featureflagdebugmenu.ui

import com.example.featureflagdebugmenu.FeatureFlagEvents
import com.example.featureflagdebugmenu.FeatureFlagScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _navSharedFlow = MutableSharedFlow<FeatureFlagScreen>(extraBufferCapacity = 1)
    val navSharedFlow = _navSharedFlow.asSharedFlow()

    private val _singleEventsSharedFlow = MutableSharedFlow<FeatureFlagEvents>(extraBufferCapacity = 1)
    val singleEventsSharedFlow = _singleEventsSharedFlow.asSharedFlow()

    fun navigateTo(target: FeatureFlagScreen) {
        _navSharedFlow.tryEmit(target)
    }

    fun restartApp() {
        _singleEventsSharedFlow.tryEmit(FeatureFlagEvents(restartApp = true))
    }

    fun showDialog() {
        _singleEventsSharedFlow.tryEmit(FeatureFlagEvents(showDialog = true))
    }

    companion object {
        const val FEATURE_FLAGS_NAVIGATION_KEY = "feature_flags_navigation"
        const val RESTART_APP_KEY = "restart_app"
    }
}