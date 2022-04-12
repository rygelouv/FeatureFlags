package com.example.featureflagdebugmenu.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.featureflagdebugmenu.*
import com.example.featureflagdebugmenu.framework.DebugConfig
import com.example.featureflagdebugmenu.framework.LocalFeatureFlagProvider
import com.example.featureflagdebugmenu.framework.RemoteConfigFlag
import com.example.featureflagdebugmenu.framework.ServerFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeatureFlagViewModel @Inject constructor(
    private val localFeatureFlagProvider: LocalFeatureFlagProvider,
    private val navigator: Navigator,
    private val mapper: FeatureFlagMapper
) : ViewModel() {

    var state by mutableStateOf(FeatureFlagUiState())
        private set

    private val _actionFlow =
        MutableSharedFlow<FeatureFlagAction>(extraBufferCapacity = CHANNEL_BUFFER_CAPACITY)

    private val updatedFeatures = mutableListOf<FeatureUi>()

    init {
        _actionFlow.process().launchIn(viewModelScope)
        loadFlagCatalogs()
    }

    /**
     * Process each Action individually and perform the right operation accordingly
     */
    private fun Flow<FeatureFlagAction>.process() = onEach {
        when (it) {
            is FeatureFlagAction.CatalogClickedAction -> onCatalogClicked(it.catalog)
            is FeatureFlagAction.FeatureUpdatedAction -> onFeatureUpdated(it.feature, it.isEnabled)
            FeatureFlagAction.ChangesApplied -> onChangesApplied()
            FeatureFlagAction.SaveUpdates -> saveUpdates()
        }
    }

    /**
     * This is the only function exposed to the view.
     * This is done to much more enforce the Unidirectional Data Flow
     */
    fun processAction(action: FeatureFlagAction) = _actionFlow.tryEmit(action)

    private fun loadFlagCatalogs() {
        state = state.copy(
            catalogScreen = FeatureFlagScreen.CatalogScreen(
                catalogs = CatalogUi.values().toList()
            ),
            title = "Feature Flags Catalog"
        )
    }

    private fun onCatalogClicked(catalog: CatalogUi) {
        state = when (catalog) {
            CatalogUi.FEATURES -> createFeatureListState()
            CatalogUi.DEBUG_CONFIG -> createDebugConfigListState()
        }
        navigator.navigateTo(state.flagsScreen)
    }

    /**
     * Called when a user has changed the state (on/off) of flag
     */
    private fun onFeatureUpdated(feature: FeatureUi, isEnabled: Boolean) {
        val screen = (state.flagsScreen as? FeatureFlagScreen.FlagScreen) ?: return
        screen.flags.find { it.key == feature.key }?.isEnabled = isEnabled
        feature.isEnabled = isEnabled
        updatedFeatures.add(feature)
    }

    private fun onChangesApplied() {
        navigator.navigateTo(FeatureFlagScreen.ConfirmationDialog())
    }

    private suspend fun saveUpdates() {
        val bulkSaveOperation = viewModelScope.async(Dispatchers.IO) {
            updatedFeatures.forEach {
                val feature = RemoteConfigFlag.getOrNull(it.key)
                    ?: ServerFlag.getOrNull(it.key)
                    ?: DebugConfig.getOrNull(it.key) ?: return@forEach

                localFeatureFlagProvider.updateFeature(
                    feature = feature,
                    isEnabled = it.isEnabled
                )
            }
        }
        bulkSaveOperation.await()
        navigator.restartApp()
    }

    private fun createDebugConfigListState(): FeatureFlagUiState {
        val features = localFeatureFlagProvider.getFeatures()
        return state.copy(
            flagsScreen = FeatureFlagScreen.FlagScreen(
                flags = mapper.mapToFeatureUiList(
                    features.filterIsInstance(DebugConfig::class.java),
                    localFeatureFlagProvider
                )
            )
        )
    }

    private fun createFeatureListState(): FeatureFlagUiState {
        val features = localFeatureFlagProvider.getFeatures()
        return state.copy(
            flagsScreen = FeatureFlagScreen.FlagScreen(
                flags = mapper.mapAndCombineFeatureUiLists(
                    features1 = features.filterIsInstance(RemoteConfigFlag::class.java),
                    features2 = features.filterIsInstance(ServerFlag::class.java),
                    localFeatureFlagProvider = localFeatureFlagProvider
                )
            )
        )
    }

    companion object {
        const val CHANNEL_BUFFER_CAPACITY = 16
    }
}