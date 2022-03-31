package com.example.featureflagdebugmenu.framework

import com.example.featureflagdebugmenu.BuildConfig
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Check whether a feature is enabled or not. Will first look into the providers to see which one has the feature
 * Normally, based on how the framework is built, two provider should provide the same feature flag at the same time
 * That would be a violation of the design. Each provider should provider a pool of feature flags that are different
 * from what the providers are providing.
 */
interface FeatureManager {
    fun initialize()
    fun addProvider(provider: FeatureFlagProvider)
    fun removeProvider(provider: FeatureFlagProvider)
    fun isFeatureEnabled(feature: Feature): Boolean
    fun refreshFeatureFlags()
    fun clearFeatureFlagProviders()
}

@Singleton
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
