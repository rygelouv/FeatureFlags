package com.example.featureflagdebugmenu.framework

/**
 * A Feature uniquely identifies a part of the app code that can either be enabled or disabled.
 * Features only have two states by design to simplify the implementation
 *
 * @param key unique value that identifies a test setting (for "Remote Config tool" flags this is shared across Android/iOS)
 */
interface Feature {
    val key: String
    val title: String
    val explanation: String
    val defaultValue: Boolean
}
