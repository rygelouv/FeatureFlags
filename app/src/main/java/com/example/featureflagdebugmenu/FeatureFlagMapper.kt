package com.example.featureflagdebugmenu

import com.example.featureflagdebugmenu.framework.Feature
import com.example.featureflagdebugmenu.framework.LocalFeatureFlagProvider
import javax.inject.Inject

/**
 * Helps with transformation of models from one type ot another
 */
class FeatureFlagMapper @Inject constructor() {

    /**
     * Maps 2 list of [Feature] and add them to create one mapped list of [FeatureUi]
     * @param localFeatureFlagProvider passed down to the [Feature.toFeatureUi] function. See KDoc.
     */
    fun mapAndCombineFeatureUiLists(
        features1: List<Feature>,
        features2: List<Feature>,
        localFeatureFlagProvider: LocalFeatureFlagProvider
    ): List<FeatureUi> {
        val featureUis1 = mapToFeatureUiList(features1, localFeatureFlagProvider)
        val featureUis2 = mapToFeatureUiList(features2, localFeatureFlagProvider)
        return featureUis1.toMutableList() + featureUis2
    }

    /**
     * Maps a list of [Feature] to a list of [FeatureUi]
     * @param localFeatureFlagProvider passed down to the [Feature.toFeatureUi] function. See KDoc.
     */
    fun mapToFeatureUiList(
        features: List<Feature>,
        localFeatureFlagProvider: LocalFeatureFlagProvider
    ): List<FeatureUi> = features.map { it.toFeatureUi(localFeatureFlagProvider) }

    /**
     * Maps a [Feature] to a [FeatureUi]
     * @param localFeatureFlagProvider used to evaluate if the feature is enabled or not
     */
    private fun Feature.toFeatureUi(localFeatureFlagProvider: LocalFeatureFlagProvider): FeatureUi =
        FeatureUi(
            key = this.key,
            name = this.title,
            description = this.explanation,
            isEnabled = localFeatureFlagProvider.isFeatureEnabled(this)
        )
}