package com.eco.virtuRun.ui.appLayoutInfo

import com.eco.virtuRun.ui.theme.DynamicTheme
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.DpSize
import android.graphics.Rect

enum class AppLayoutMode { LANDSCAPE_NORMAL, LANDSCAPE_BIG, PORTRAIT, PORTRAIT_NARROW;
    fun isLandscape(): Boolean = (this == LANDSCAPE_BIG || this == LANDSCAPE_NORMAL)
    fun isPortrait(): Boolean = (this == PORTRAIT || this == PORTRAIT_NARROW)
}

data class AppLayoutInfo(
    val appLayoutMode: AppLayoutMode,
    val windowDpSize: DpSize,
    var backgroundImage: ImageBitmap,
    var dynamicTheme: DynamicTheme? = null,
    val foldableInfo: FoldableInfo? = null,
    val windowClassWithSize: WindowClassWithSize? = null)

enum class CurrentRotation() {
    ROTATION_0,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270;
    fun isPortrait(): Boolean = (this == ROTATION_0) or (this == ROTATION_180)
}

data class WindowClassWithSize(
    val windowWidth: WindowWidthSizeClass,
    val windowHeight: WindowHeightSizeClass,
    val sizeDP: DpSize,
    val rotation: CurrentRotation = CurrentRotation.ROTATION_0,
    //val statusBarHeight: Int = 0
    val sizePX: Rect = Rect(0,0,1080,0)
)
