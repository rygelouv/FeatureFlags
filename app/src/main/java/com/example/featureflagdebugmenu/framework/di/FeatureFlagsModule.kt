package com.example.featureflagdebugmenu.framework.di

import android.content.Context
import android.content.SharedPreferences
import com.example.featureflagdebugmenu.framework.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FeatureFlagsSharedPreferences

@Module
@InstallIn(SingletonComponent::class)
class FeatureFlagsModule {

    @FeatureFlagsSharedPreferences
    @Provides
    fun provideLoginSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_FEATURE_FLAGS_FILENAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                    CACHE_EXPIRATION_SECS_DEV
                } else {
                    CACHE_EXPIRATION_SECS
                }
            }
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(RemoteConfigFlag.toMap())
        }
    }

    @Singleton
    @Provides
    fun provideFirebaseFeatureFlagProvider(remoteConfig: FirebaseRemoteConfig): FirebaseFeatureFlagProvider {
        return FirebaseFeatureFlagProvider(remoteConfig)
    }

    @Singleton
    @Provides
    fun provideLocalProvider(@FeatureFlagsSharedPreferences preferences: SharedPreferences): LocalFeatureFlagProvider {
        return LocalFeatureFlagProvider(preferences)
    }

    companion object {
        const val CACHE_EXPIRATION_SECS = 1 * 60 * 60L
        const val CACHE_EXPIRATION_SECS_DEV = 1L
        const val PREFS_FEATURE_FLAGS_FILENAME = "feature_flags_prefs"
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureManagerModule {
    @Binds
    abstract fun FeatureManagerImpl.provideFeatureManager(): FeatureManager
}