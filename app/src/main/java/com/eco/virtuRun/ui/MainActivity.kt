package com.eco.virtuRun.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eco.virtuRun.data.model.AppSettings
import com.eco.virtuRun.ui.appLayoutInfo.getFoldableInfoFlow
import com.eco.virtuRun.ui.appLayoutInfo.getWindowLayoutType
import com.eco.virtuRun.ui.appLayoutInfo.getWindowSizeClasses
import com.eco.virtuRun.ui.theme.AppComposeTheme
import com.eco.virtuRun.ui.theme.createColorSchemeFromBitmap
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    private val appSettings : AppSettings = get()  //appSettingsRepository.appSettings
    private var randomImage: Int = (0..9).random()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("RANDOM_IMAGE", randomImage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        randomImage = savedInstanceState.getInt("RANDOM_IMAGE", randomImage)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("RestrictedApi", "InternalInsetResource")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("VRUN", "VirtuRunActivity onCreate")
        super.onCreate(savedInstanceState)
        lifecycleScope.launch() {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.setDecorFitsSystemWindows(false)
            window.navigationBarColor = Color.WHITE
            window.statusBarColor = Color.WHITE
        }
        val devicePostureFlow = getFoldableInfoFlow(this@MainActivity)

        setContent {
            //val randomImage by remember { mutableStateOf(if (randomImage == -1) (0..9).random() else randomImage) }
            val randomImage by remember { mutableStateOf(randomImage) }
            val windowSize = getWindowSizeClasses(this)
            val devicePosture by devicePostureFlow.collectAsStateWithLifecycle()
            val dynamicTheme = createColorSchemeFromBitmap(this, randomImage)
            val appLayoutInfo = getWindowLayoutType(this, windowSize, devicePosture, dynamicTheme)

            AppComposeTheme(dynamicTheme = dynamicTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val layoutInfo by remember (appLayoutInfo.appLayoutMode.isPortrait()) { mutableStateOf(appLayoutInfo) }
                    AppNavigation(layoutInfo)
                }
            }
        }
    }
}
