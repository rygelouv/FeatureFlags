package com.example.featureflagdebugmenu.ui

enum class Route(val routeName: String) {
    CATALOGS("flags_catalog"),
    FLAGS("feature_flags"),
    CONFIRMATION_DIALOG("confirmation_dialog")
}

sealed class FeatureFlagScreen(open val route: Route) {

    data class CatalogScreen(
        override val route: Route = Route.CATALOGS,
        val catalogs: List<CatalogUi> = listOf(),
    ) : FeatureFlagScreen(route)

    data class FlagScreen(
        override val route: Route = Route.FLAGS,
        val flags: List<FeatureUi> = listOf(),
        val isButtonEnabled: Boolean = false
    ) : FeatureFlagScreen(route)

    data class ConfirmationDialog(
        override val route: Route = Route.CONFIRMATION_DIALOG,
    ) : FeatureFlagScreen(route)
}

data class FeatureFlagUiState(
    val catalogScreen: FeatureFlagScreen = FeatureFlagScreen.CatalogScreen(),
    val flagsScreen: FeatureFlagScreen = FeatureFlagScreen.FlagScreen(),
    val confirmationDialog: FeatureFlagScreen = FeatureFlagScreen.ConfirmationDialog(),
    val title: String = ""
)

data class FeatureFlagEvents(
    val showDialog: Boolean = false,
    val restartApp: Boolean = false
)

sealed class FeatureFlagAction {
    data class CatalogClickedAction(val catalog: CatalogUi) : FeatureFlagAction()
    data class FeatureUpdatedAction(
        val feature: FeatureUi,
        val isEnabled: Boolean
    ) : FeatureFlagAction()

    object ChangesApplied : FeatureFlagAction()
    object SaveUpdates : FeatureFlagAction()
}

data class FeatureUi(
    val key: String,
    val name: String,
    val description: String,
    var isEnabled: Boolean
)

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
