package com.ray.pomelo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyContent()
    }
  }
}

@Preview(
  showSystemUi = true,
  showBackground = true,
  uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun MyContent() {
  val itemData = listOf(
    Triple(Icons.Default.Download, Icons.Outlined.Download, "已安裝"),
    Triple(Icons.Default.Explore, Icons.Outlined.Explore, "探索"),
  )
  var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
  val plugins = rememberSaveable { mutableStateListOf<Plugin>() }
  var isGettingStore by rememberSaveable { mutableStateOf(false) }
  var isStoreGotten by rememberSaveable { mutableStateOf(false) }

  suspend fun refresh(force: Boolean) {
    if (!force && isStoreGotten)
      return

    isGettingStore = true
    val newStore = withContext(Dispatchers.IO) {
      getStore()
    }
    plugins.clear()
    plugins.addAll(newStore)
    isStoreGotten = true
    isGettingStore = false
  }

  LaunchedEffect(Unit) {
    refresh(false)
  }

  MyTheme {
    Scaffold(
      bottomBar = {
        NavigationBar {
          for ((i, item) in itemData.withIndex())
            NavigationBarItem(
              selected = selectedIndex == i,
              onClick = { selectedIndex = i },
              icon = {
                Icon(
                  if (selectedIndex == i)
                    item.first
                  else
                    item.second,
                  contentDescription = item.third
                )
              },
              label = { Text(item.third) },
            )
        }
      }
    ) { padding ->
      when (selectedIndex) {
        0 -> PageInstalled(padding)
        1 -> PageExplore(padding, plugins, isGettingStore) {
          refresh(true)
        }
      }
    }
  }
}
