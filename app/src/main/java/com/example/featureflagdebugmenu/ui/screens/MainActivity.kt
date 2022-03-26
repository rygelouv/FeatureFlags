package com.example.featureflagdebugmenu.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.featureflagdebugmenu.FeatureFlagAction
import com.example.featureflagdebugmenu.ui.FeatureFlagViewModel
import com.example.featureflagdebugmenu.ui.NavigationComponent
import com.example.featureflagdebugmenu.ui.Navigator
import com.example.featureflagdebugmenu.ui.theme.FeatureFlagDebugMenuTheme
import com.example.featureflagdebugmenu.ui.utils.triggerRestart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

typealias ActionChannel = Channel<FeatureFlagAction>

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<FeatureFlagViewModel>()

    @Inject
    lateinit var navigator: Navigator

    private val actionChannel = Channel<FeatureFlagAction>()

    /**
     * Merge all actions emitted by this view as a single flow that will be consumed by the VM
     */
    fun actions(): Flow<FeatureFlagAction> = actionChannel.consumeAsFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actions().onEach(viewModel::processAction).launchIn(lifecycleScope)

        navigator.singleEventsSharedFlow.onEach {
            if (it.restartApp) this.triggerRestart()
        }.launchIn(lifecycleScope)

        setContent {
            FeatureFlagDebugMenuTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = viewModel.state.title) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, "backIcon")
                                }
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White,
                            elevation = 10.dp
                        )
                    }
                ) {
                    val navController = rememberNavController()
                    NavigationComponent(
                        navController = navController,
                        navigator = navigator,
                        viewModel = viewModel,
                        actionChannel = actionChannel
                    )
                }
            }
        }
    }
}