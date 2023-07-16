package com.eco.virtuRun.ui.views.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eco.virtuRun.R
import com.eco.virtuRun.ui.theme.*
import com.eco.virtuRun.data.model.*
import com.eco.virtuRun.ui.ScreenInfo
import com.eco.virtuRun.ui.theme.AppFontFamily
import com.eco.virtuRun.ui.theme.AppGreen
import com.eco.virtuRun.ui.theme.AppOrange

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "Recycle", "RestrictedApi")
@Composable
fun MainComposable(screenInfo: ScreenInfo, appSettings: AppSettings,
                   state: MainViewModelInterface.State,
                   onEventSent: (event: MainViewModelInterface.Event) -> Unit,
) {
    Log.d("VRUN", "MainComposable - userMessage: ${state.userMessage} " )
    //val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val image = remember { screenInfo.backgroundImage }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isPortrait by remember(screenInfo.isPortrait) { mutableStateOf(screenInfo.isPortrait) }

    keyboardController?.hide()

    state.userMessage?.let { userMessage ->
        Log.d("VRUN", "MainComposable - LaunchedEffect - let - userMessage: ${state.userMessage}" )
        LaunchedEffect(userMessage) { snackbarHostState.showSnackbar(userMessage, duration = SnackbarDuration.Short)
            state.userMessage = null
        }
    }

    Box {
        Image(image, null, Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
        Scaffold(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
            containerColor = Color.Transparent,   // Make the background transparent
            snackbarHost = { SnackbarHost(snackbarHostState) {
                Snackbar(modifier = Modifier, actionColor = AppGreen,  //.absoluteOffset(y = 50.dp)
                    containerColor = AppOrange.copy(alpha=0.85F), contentColor = Color.White, snackbarData = it)}
            }
        ) { MainHeader(isPortrait, appSettings, onEventSent = onEventSent) }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainHeader(isPortrait: Boolean,
    appSettings: AppSettings, onEventSent: (event: MainViewModelInterface.Event) -> Unit) {
    //val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(10.dp)
            .paint(painterResource(R.drawable.filmframe), alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds, alpha = 0.6F)) {
        Text("Persistable Settings",
            textAlign = TextAlign.Center,
            color = Color.White, //MaterialTheme.colorScheme.primary,
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
        )
    }
}
