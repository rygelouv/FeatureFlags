package com.example.featureflagdebugmenu

import android.content.SharedPreferences
import com.example.featureflagdebugmenu.framework.DebugConfig
import com.example.featureflagdebugmenu.framework.LocalFeatureFlagProvider
import com.example.featureflagdebugmenu.framework.RemoteConfigFlag
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class LocalFeatureFlagProviderTest {

    @MockK
    lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `use feature default value when no value in preferences`() {
        every { preferences.getBoolean(any(), any()) }
            .returns(DebugConfig.LEAK_CANARY.defaultValue)

        LocalFeatureFlagProvider(preferences).isFeatureEnabled(DebugConfig.LEAK_CANARY)
        verify {
            preferences.getBoolean(
                DebugConfig.LEAK_CANARY.key,
                DebugConfig.LEAK_CANARY.defaultValue
            )
        }
    }

    @Test
    fun `enable feature should set preference to true`() {
        val mockEditor = mockk<SharedPreferences.Editor>()
        every { mockEditor.apply() }.just(Runs)
        every { preferences.edit() }.returns(mockEditor)
        every { mockEditor.putBoolean(any(), any()) }.returns(mockEditor)

        LocalFeatureFlagProvider(preferences).updateFeature(RemoteConfigFlag.NEW_PRICING, true)

        verify { mockEditor.putBoolean(RemoteConfigFlag.NEW_PRICING.key, true) }
        verify { mockEditor.apply() }
    }

    @Test
    fun `make sure all features exist`() {

    }
}