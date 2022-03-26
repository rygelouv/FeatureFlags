package com.example.featureflagdebugmenu

import com.example.featureflagdebugmenu.framework.*

/**
 * For use during unit/instrumentation tests, allows to dynamically enable/disable features
 * during automated tests
 */
object TestFeatureFlagProvider : FeatureFlagProvider, UpdatableFeatureFlagProvider {

    private val features: ArrayList<Feature> = buildFeaturesList()
    private val enabledFeatures = arrayListOf<Feature>()

    private fun buildFeaturesList(): ArrayList<Feature> {
        return arrayListOf<Feature>().apply {
            addAll(RemoteConfigFlag.values())
            addAll(ServerFlag.values())
            addAll(DebugConfig.values())
        }
    }

    override fun isFeatureEnabled(feature: Feature) = enabledFeatures.contains(feature)

    override fun hasFeature(feature: Feature) = features.contains(feature)

    override fun updateFeature(feature: Feature, isEnabled: Boolean) {
        if (isEnabled) enabledFeatures.add(feature) else enabledFeatures.remove(feature)
    }
}