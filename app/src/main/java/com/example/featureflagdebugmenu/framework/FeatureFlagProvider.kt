package com.example.featureflagdebugmenu.framework

/**
 * Every provider has an explicit priority so they can override each other (e.g. "Remote Config tool" > Store).
 *
 * Not every provider has to provide a flag value for every feature. This is to avoid implicitly relying on build-in
 * defaults (e.g. "Remote Config tool" returns false when no value for a feature) and to avoid that every provider has to provide a
 * value for every feature. (e.g. no "Remote Config tool" configuration needed, unless you want the toggle to be remote)
 */
interface FeatureFlagProvider {
    fun isFeatureEnabled(feature: Feature): Boolean
    fun hasFeature(feature: Feature): Boolean
}

/**
 * For flag providers that are refreshable i.e [FirebaseFeatureFlagProvider]
 */
interface RefreshableFeatureFlagProvider {
    fun refreshFeatureFlags()
}

/**
 * For flag providers that are updatable on Device i.e [LocalFeatureFlagProvider]
 */
interface UpdatableFeatureFlagProvider {
    fun updateFeature(feature: Feature, isEnabled: Boolean)
}