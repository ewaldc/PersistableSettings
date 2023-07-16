package com.eco.virtuRun.ui.views.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.eco.virtuRun.ui.ScreenInfo
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavController, screenInfo: ScreenInfo) { //} appLayoutInfo: AppLayoutInfo) {
    Log.d("VRUN", "MainScreen init")
    //val viewModel: MainViewModel by viewModel()
    val viewModel = getViewModel<MainViewModel>()
    //val viewModel = koinViewModel<MainViewModel>()
    MainComposable(screenInfo, viewModel.appSettings,
        state = viewModel.viewState.value, //effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.emitEvent(event) }
    )
}
