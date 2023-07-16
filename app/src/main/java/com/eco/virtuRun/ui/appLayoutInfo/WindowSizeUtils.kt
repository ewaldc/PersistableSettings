package com.eco.virtuRun.ui.appLayoutInfo

import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.view.Surface
import android.view.WindowMetrics
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize

@RequiresApi(Build.VERSION_CODES.R)
@ExperimentalMaterial3WindowSizeClassApi
@Composable
fun getWindowSizeClasses(activity: ComponentActivity): WindowClassWithSize {
    //val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    val metrics: WindowMetrics = activity.windowManager.maximumWindowMetrics
    val size = getWindowDpSizeFromRect(metrics.bounds)
    //val bounds = activity.windowManager.currentWindowMetrics.bounds

    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    //val resources: Resources = Resources.getSystem()
    //val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")

    return WindowClassWithSize(
        windowWidth = windowSizeClass.widthSizeClass,
        windowHeight = windowSizeClass.heightSizeClass,
        sizeDP = size,
        rotation = getRotation(activity),
        sizePX = metrics.bounds
        //statusBarHeight = resources.getDimensionPixelSize(resourceId)
    )
}

fun getRotation(context: ComponentActivity): CurrentRotation {
    val display = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) context.display
        else @Suppress("DEPRECATION") context.windowManager.defaultDisplay
    //windowWidth = display.() .getMetrics() .widthSizeClass
    //windowHeight = windowSizeClass.heightSizeClass
    return when (display?.rotation) {
        Surface.ROTATION_0 -> CurrentRotation.ROTATION_0
        Surface.ROTATION_90 -> CurrentRotation.ROTATION_90
        Surface.ROTATION_180 -> CurrentRotation.ROTATION_180
        Surface.ROTATION_270 -> CurrentRotation.ROTATION_270
        else -> CurrentRotation.ROTATION_0
    }
}

@Composable
fun getWindowDpSizeFromRect(rect: Rect): DpSize {
    val density = LocalDensity.current
    return with(density) { rect.toComposeRect().size.toDpSize() }
}

@Composable
fun Rect.toDpRect(): RectF {
    LocalConfiguration.current
    val density = LocalDensity.current
    return with(density) {
        RectF(this@toDpRect.left.toDp().value,
            this@toDpRect.top.toDp().value,
            this@toDpRect.right.toDp().value,
            this@toDpRect.bottom.toDp().value)
    }
}