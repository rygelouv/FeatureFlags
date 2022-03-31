package com.example.featureflagdebugmenu

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.featureflagdebugmenu.framework.FeatureManager
import com.example.featureflagdebugmenu.framework.RemoteConfigFlag

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Inject
    lateinit var featureManager: FeatureManager

    @Before
    fun setUp() {
        featureManager.clearFeatureFlagProviders()
        featureManager.addProvider(TestFeatureFlagProvider)
    }

    @Test
    fun useAppContext() {
        TestFeatureFlagProvider.updateFeature(RemoteConfigFlag.DELETE_PROFILE, true)

        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.featureflagdebugmenu", appContext.packageName)
    }
}