package com.example.featureflagdebugmenu.framework

import com.example.featureflagdebugmenu.BuildConfig
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

/**
 * Check whether a feature should be enabled or not. Based on the priority of the different providers and if said
 * provider explicitly defines a value for that feature, the value of the flag is returned.
 */
interface FeatureManager {
    fun initialize()
    fun addProvider(provider: FeatureFlagProvider)
    fun removeProvider(provider: FeatureFlagProvider)
    fun isFeatureEnabled(feature: Feature): Boolean
    fun refreshFeatureFlags()
    fun clearFeatureFlagProviders()
}

class FeatureManagerImpl @Inject constructor(
    private val firebaseFeatureFlagProvider: FirebaseFeatureFlagProvider,
    private val serverFeatureFlagProvider: ServerFeatureFlagProvider,
    private val localFeatureFlagProvider: LocalFeatureFlagProvider
) : FeatureManager {

    private val providers = CopyOnWriteArrayList<FeatureFlagProvider>()

    override fun initialize() {
        if (BuildConfig.DEBUG) {
            addProvider(localFeatureFlagProvider)
            if (localFeatureFlagProvider.isFeatureEnabled(DebugConfig.DEBUG_FIREBASE_REMOTE_CONFIG)) {
                addProvider(firebaseFeatureFlagProvider)
                localFeatureFlagProvider.excludeFirebaseFlags()
            }
            if (localFeatureFlagProvider.isFeatureEnabled(DebugConfig.DEBUG_SERVER_FLAGS)) {
                addProvider(serverFeatureFlagProvider)
                localFeatureFlagProvider.excludeServerFlags()
            }
        } else {
            addProvider(serverFeatureFlagProvider)
            addProvider(firebaseFeatureFlagProvider)
        }
    }

    override fun isFeatureEnabled(feature: Feature): Boolean {
        return providers.firstOrNull { it.hasFeature(feature) }
            ?.isFeatureEnabled(feature)
            ?: feature.defaultValue
    }

    override fun refreshFeatureFlags() {
        providers
            .filter { it is RefreshableFeatureFlagProvider }
            .forEach { (it as RefreshableFeatureFlagProvider).refreshFeatureFlags() }
    }

    override fun addProvider(provider: FeatureFlagProvider) {
        providers.add(provider)
    }

    override fun removeProvider(provider: FeatureFlagProvider) {
        providers.remove(provider)
    }

    override fun clearFeatureFlagProviders() = providers.clear()
}
