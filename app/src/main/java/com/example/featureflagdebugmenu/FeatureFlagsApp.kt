package com.example.featureflagdebugmenu

import android.app.Application
import com.example.featureflagdebugmenu.framework.FeatureManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FeatureFlagsApp: Application() {
    @Inject
    lateinit var featureManager: FeatureManager

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        featureManager.initialize()
    }
}