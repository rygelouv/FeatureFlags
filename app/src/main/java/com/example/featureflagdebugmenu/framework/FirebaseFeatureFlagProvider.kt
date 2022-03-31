package com.example.featureflagdebugmenu.framework

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

/**
 * Provider of feature flags set up on Firebase Remote Config. Usually used in production for toggling
 * mobile feature. Used by both mobile engineers and product managers
 *
 * This is refreshable because Firebase Remote Configs can be refresh programmatically from the app code
 */
class FirebaseFeatureFlagProvider @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : FeatureFlagProvider, RefreshableFeatureFlagProvider {

    private val features: Array<RemoteConfigFlag> = RemoteConfigFlag.values()

    override fun isFeatureEnabled(feature: Feature): Boolean =
        remoteConfig.getBoolean(feature.key)

    override fun hasFeature(feature: Feature): Boolean {
        if (feature !is RemoteConfigFlag) return false
        return features.contains(feature)
    }

    override fun refreshFeatureFlags() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) Log.i(
                    "FirebaseFeatureFlagProvider",
                    "successfully fetched and activated remote configs"
                )
            }
    }
}

/**
 * A feature flag is something that disappears over time (hence it is a tool to simplify development)
 * e.g we develop a feature, test it, release it, then we remove it and the feature remain in the app
 *
 * Note that this has nothing to do with being available as a remote feature flag or not. Some features
 * will be deployed using our feature flag tool, some will not.
 *
 * [key] Shared between Android and iOS featureflag backend
 */
enum class RemoteConfigFlag(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean = false
) : Feature {
    DARK_MODE("feature.darkmode", "Dark theme", "Enable dark mode"),
    NEW_PRICING("feature.newpricing", "New Pricing", "Enable new app pricing model"),
    DELETE_PROFILE("feature.delete_profile", "Delete Profile", "Enable the user profile deletion");

    companion object {
        fun toMap(): Map<String, Boolean> = values().associate { it.key to it.defaultValue }

        fun getOrNull(key: String): RemoteConfigFlag? = values().find { it.key == key }
    }
}
