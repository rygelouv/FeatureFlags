package com.example.featureflagdebugmenu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.featureflagdebugmenu.FeatureFlagAction
import com.example.featureflagdebugmenu.FeatureFlagScreen
import com.example.featureflagdebugmenu.FeatureUi
import com.example.featureflagdebugmenu.ui.theme.FeatureFlagDebugMenuTheme
import com.example.featureflagdebugmenu.ui.theme.GreenLight

@Composable
fun FeatureFlagList(
    screen: FeatureFlagScreen.FlagScreen,
    actionChannel: ActionChannel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(screen.flags) { item ->
                FeatureItem(item = item, actionChannel = actionChannel)
            }
        }
        Button(
            onClick = { actionChannel.trySend(FeatureFlagAction.ChangesApplied) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = GreenLight,
                contentColor = Color.White
            )
        ) {
            Text(text = "Apply Changes".uppercase(), fontSize = 18.sp)
        }
    }
}

@Composable
fun FeatureItem(item: FeatureUi, actionChannel: ActionChannel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(modifier = Modifier.weight(8f)) {
            Text(text = item.name, fontSize = 20.sp)
            Text(text = item.description, fontSize = 14.sp)
        }
        val checkedState = remember { mutableStateOf(item.isEnabled) }
        Switch(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                actionChannel.trySend(FeatureFlagAction.FeatureUpdatedAction(item, it))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FeatureFlagDebugMenuTheme {
        val item = FeatureUi(
            key = "flag_feature_key",
            name = "Leak Canary",
            description = "Enable Leak Canary to track memory leaks lzekng re lakenglaekn glair",
            isEnabled = false
        )
        val items = listOf(
            item,
            item,
            item,
            item,
            item,
        )
        //FeatureFlagList(items,{})
    }
}