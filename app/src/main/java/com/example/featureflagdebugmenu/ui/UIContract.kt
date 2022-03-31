package com.example.featureflagdebugmenu.ui

/**
 * Represents the routes to the different screens of our debug menu UI
 */
enum class Route(val routeName: String) {
    CATALOGS("flags_catalog"),
    FLAGS("feature_flags"),
    CONFIRMATION_DIALOG("confirmation_dialog")
}

/**
 * Represents a screen of the debug menu UI
 */
sealed class FeatureFlagScreen(open val route: Route) {

    /**
     * Displays the List of Flags catalog
     */
    data class CatalogScreen(
        override val route: Route = Route.CATALOGS,
        val catalogs: List<CatalogUi> = listOf(),
    ) : FeatureFlagScreen(route)

    /**
     * Displays the list of feature flags
     */
    data class FlagScreen(
        override val route: Route = Route.FLAGS,
        val flags: List<FeatureUi> = listOf(),
        val isButtonEnabled: Boolean = false
    ) : FeatureFlagScreen(route)

    /**
     * Displays the confirmation Alert Dialog
     */
    data class ConfirmationDialog(
        override val route: Route = Route.CONFIRMATION_DIALOG,
    ) : FeatureFlagScreen(route)
}

/**
 * UI state being dispatched to the UI side. Compose screens (composables) will consume this UI
 * State in order to trigger recompositions
 */
data class FeatureFlagUiState(
    val catalogScreen: FeatureFlagScreen = FeatureFlagScreen.CatalogScreen(),
    val flagsScreen: FeatureFlagScreen = FeatureFlagScreen.FlagScreen(),
    val confirmationDialog: FeatureFlagScreen = FeatureFlagScreen.ConfirmationDialog(),
    val title: String = ""
)

/**
 * Single shot ViewModel events used for Navigation and singe shot events like Snack bars
 */
data class FeatureFlagEvents(
    val showDialog: Boolean = false,
    val restartApp: Boolean = false
)

/**
 * Represent an action performed by the user on UI side
 */
sealed class FeatureFlagAction {
    data class CatalogClickedAction(val catalog: CatalogUi) : FeatureFlagAction()
    data class FeatureUpdatedAction(
        val feature: FeatureUi,
        val isEnabled: Boolean
    ) : FeatureFlagAction()

    object ChangesApplied : FeatureFlagAction()
    object SaveUpdates : FeatureFlagAction()
}

/**
 * UI model for a [Feature]
 */
data class FeatureUi(
    val key: String,
    val name: String,
    val description: String,
    var isEnabled: Boolean
)

/**
 * UI model for a Catalog
 */
enum class CatalogUi(
    val label: String,
    val description: String
) {
    FEATURES(
        label = "Feature Flags",
        description = "All the feature flags that can be remotely enabled"
    ),
    DEBUG_CONFIG(
        label = "Debug Configs",
        description = "All the debug configuration necessary for developers"
    )
}
