package com.ray.pomelo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun PageExplore(
  padding: PaddingValues,
  plugins: SnapshotStateList<Plugin>,
  isGettingStore: Boolean,
  refresh: suspend () -> Unit
) {
  val scope = rememberCoroutineScope()

  Column(modifier = Modifier.padding(padding)) {
    if (isGettingStore)
      Text("刷新中")
    else
      LazyColumn {
        items(plugins) {
          Text(it.name)
          Text(it.desc)
          HorizontalDivider()
        }
      }

    Button(onClick = {
      scope.launch {
        refresh()
      }
    }) {
      Text("重新整理")
    }
  }
}
