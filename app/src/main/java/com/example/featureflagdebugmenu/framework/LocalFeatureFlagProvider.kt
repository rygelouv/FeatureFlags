package com.example.featureflagdebugmenu.framework

import android.content.SharedPreferences
import com.example.featureflagdebugmenu.framework.di.FeatureFlagsSharedPreferences
import javax.inject.Inject

/**
 * Provides all possible feature flags in the system. This include [RemoteConfigFlag], [ServerFlag]
 * and its own [DebugConfig]. This is the priority feature flag provider for a Debug build
 * It has the ability to enable [FirebaseFeatureFlagProvider] and [ServerFeatureFlagProvider] and when
 * that's the case, the flags provided by those provider are just ignored (excluded)
 *
 * This provider is the one that is used to build and auto-generate the debug menu
 * It is [UpdatableFeatureFlagProvider] because these flags can be turned on/off from the debug menu
 *
 * @param preferences this provider relies on [SharedPreferences] to store feature flags
 */
class LocalFeatureFlagProvider @Inject constructor(
    @FeatureFlagsSharedPreferences private val preferences: SharedPreferences
) : FeatureFlagProvider, UpdatableFeatureFlagProvider {

    private val features: ArrayList<Feature> = buildFeaturesList()

    private fun buildFeaturesList(): ArrayList<Feature> {
        return arrayListOf<Feature>().apply {
            addAll(RemoteConfigFlag.values())
            addAll(ServerFlag.values())
            addAll(DebugConfig.values())
        }
    }

    override fun isFeatureEnabled(feature: Feature): Boolean =
        preferences.getBoolean(feature.key, feature.defaultValue)

    override fun hasFeature(feature: Feature): Boolean = features.contains(feature)

    override fun updateFeature(feature: Feature, isEnabled: Boolean) =
        preferences.edit().putBoolean(feature.key, isEnabled).apply()

    fun excludeFirebaseFlags() = features.removeAll(RemoteConfigFlag.values())

    fun excludeServerFlags() = features.removeAll(ServerFlag.values())

    fun getFeatures(): List<Feature> = features
}

/**
 * A debug config is something that stays in our app forever (hence it is a tool to simplify testing)
 * e.g. it is a hook into our app to allow something that a production app shouldn’t allow.
 * (enable logging, bypass software update,…)
 *
 * Debug Configs must never be exposed via our remote feature flag tool.
 */
enum class DebugConfig(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean = false
) : Feature {
    USE_DEVELOP_PORTAL(
        "debug_config.usedevelopportal",
        "Development portal",
        "Use developer REST endpoint"
    ),
    IDLING_RESOURCES(
        "debug_config.idlingresources",
        "Idling resources",
        "Enable idling resources for Espresso"
    ),
    LEAK_CANARY("debug_config.leakcanary", "Leak Canary", "Enable leak canary"),
    DEBUG_LOGGING(
        "debug_config.debuglogging",
        "Enable logging",
        "Print all app logging to console",
        defaultValue = true
    ),
    STRICT_MODE(
        "debug_config.strictmode",
        "Enable strict mode",
        "Detect IO operations accidentally performed on the main Thread",
        defaultValue = true
    ),
    CRASH_APP("debug_config.crashapp", "Crash app", "Force java crash next app startup"),
    DEBUG_FIREBASE_REMOTE_CONFIG(
        "debug_config.debug_firebase_remote_config",
        "Enable Firebase remote config",
        "Enable reading feature flags from Firebase on debug builds",
        false
    ),
    DEBUG_SERVER_FLAGS(
        "debug_config.debug_server_flags",
        "Enable Server flags",
        "Enable reading feature flags from Server on debug builds",
        false
    );

    companion object {
        fun getOrNull(key: String): DebugConfig? = values().find { it.key == key }
    }
}
