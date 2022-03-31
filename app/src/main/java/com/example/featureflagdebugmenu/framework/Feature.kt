package com.example.featureflagdebugmenu.framework

/**
 * A Feature uniquely identifies a part of the app code that can either be enabled or disabled.
 * Features only have two states by design to simplify the implementation
 *
 * @property key unique value that identifies a test setting (for "Remote Config tool" flags this is shared across Android/iOS)
 * @property title the Feature flag label or human-friendly name i.e "New Pricing"
 * @property explanation it's alway good to explain why a feature flag is needed and how/when to use it.
 * @property defaultValue all feature flags must have a default value as a fallback solution
 */
interface Feature {
    val key: String
    val title: String
    val explanation: String
    val defaultValue: Boolean
}
