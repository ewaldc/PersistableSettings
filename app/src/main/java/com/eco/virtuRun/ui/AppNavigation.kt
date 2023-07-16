package com.eco.virtuRun.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eco.virtuRun.ui.appLayoutInfo.AppLayoutInfo
import com.eco.virtuRun.ui.views.main.MainScreen

data class ScreenInfo(
    val isPortrait: Boolean,
    val screenWidthDp: Dp,
    val screenHeightDp: Dp,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val backgroundImage: ImageBitmap
)

@Composable
fun AppNavigation(appLayoutInfo: AppLayoutInfo) {
    Log.d("VRUN", "AppNavigation")
    val navController = rememberNavController()
    val isPortrait = remember { appLayoutInfo.appLayoutMode.isPortrait() }
    val screenWidthDp = remember(isPortrait) { appLayoutInfo.windowDpSize.width }
    val screenHeightDp = remember(isPortrait) { appLayoutInfo.windowDpSize.height }
    val screenWidthPx = remember(isPortrait) { appLayoutInfo.windowClassWithSize!!.sizePX.width() }
    val screenHeightPx = remember(isPortrait) { appLayoutInfo.windowClassWithSize!!.sizePX.height() }
    val backgroundImage = remember(isPortrait) { appLayoutInfo.backgroundImage }
    val screenInfo by remember(isPortrait) { mutableStateOf(ScreenInfo(isPortrait, screenWidthDp, screenHeightDp, screenWidthPx, screenHeightPx, backgroundImage)) }

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.MAIN
    ) {
        composable(route = Navigation.Routes.MAIN) {
            MainScreen(navController, screenInfo)
        }
    }
}

object Navigation {
    object Routes {
        const val MAIN = "main"
    }
}


