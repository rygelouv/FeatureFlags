package com.example.featureflagdebugmenu.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.featureflagdebugmenu.ui.Navigator.Companion.FEATURE_FLAGS_NAVIGATION_KEY
import com.example.featureflagdebugmenu.ui.screens.ActionChannel
import com.example.featureflagdebugmenu.ui.screens.FeatureFlagGroupList
import com.example.featureflagdebugmenu.ui.screens.FeatureFlagList
import com.example.featureflagdebugmenu.ui.screens.RestartConfirmationDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun NavigationComponent(
    navController: NavHostController,
    navigator: Navigator,
    viewModel: FeatureFlagViewModel,
    actionChannel: ActionChannel
) {
    LaunchedEffect(FEATURE_FLAGS_NAVIGATION_KEY) {
        navigator.navSharedFlow.onEach {
            navController.navigate(it.route.routeName)
        }.launchIn(this)
    }

    NavHost(
        navController = navController,
        startDestination = Route.CATALOGS.routeName
    ) {
        composable(Route.CATALOGS.routeName) {
            FeatureFlagGroupList(
                (viewModel.state.catalogScreen as FeatureFlagScreen.CatalogScreen).catalogs,
                actionChannel
            )
        }
        composable(Route.FLAGS.routeName) {
            FeatureFlagList(
                (viewModel.state.flagsScreen as FeatureFlagScreen.FlagScreen),
                actionChannel
            )
        }
        dialog(Route.CONFIRMATION_DIALOG.routeName) {
            RestartConfirmationDialog(true, actionChannel)
        }
    }
}