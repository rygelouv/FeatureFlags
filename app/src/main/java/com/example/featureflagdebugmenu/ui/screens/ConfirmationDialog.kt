package com.example.featureflagdebugmenu.ui.screens

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.featureflagdebugmenu.ui.FeatureFlagAction
import kotlinx.coroutines.channels.Channel

@Composable
fun RestartConfirmationDialog(showDialog: Boolean, actionChannel: ActionChannel) {
    val openDialog = remember { mutableStateOf(showDialog) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    actionChannel.trySend(FeatureFlagAction.SaveUpdates)
                    openDialog.value = false
                })
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false })
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Restart App") },
            text = {
                Text(
                    text = "Are you sure you want to change update these feature flags?" +
                            "\nThis will restart the app"
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RestartConfirmationDialog(true, Channel())
}