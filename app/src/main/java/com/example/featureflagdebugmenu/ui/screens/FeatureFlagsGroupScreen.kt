package com.example.featureflagdebugmenu.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.featureflagdebugmenu.ui.CatalogUi
import com.example.featureflagdebugmenu.ui.FeatureFlagAction
import com.example.featureflagdebugmenu.ui.theme.FeatureFlagDebugMenuTheme
import kotlinx.coroutines.channels.Channel

@Composable
fun FeatureFlagGroupList(
    catalogs: List<CatalogUi>,
    actionChannel: ActionChannel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(catalogs) { item ->
            FeatureFlagGroupItem(item = item, actionChannel = actionChannel)
        }
    }
}

@Composable
fun FeatureFlagGroupItem(
    item: CatalogUi,
    actionChannel: ActionChannel
) {
    Column(
        modifier = Modifier
            .clickable { actionChannel.trySend(FeatureFlagAction.CatalogClickedAction(item)) }
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = item.label, fontSize = 22.sp)
        Text(text = item.description, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GroupPreview() {
    FeatureFlagDebugMenuTheme {
        FeatureFlagGroupList(listOf(), Channel())
    }
}
