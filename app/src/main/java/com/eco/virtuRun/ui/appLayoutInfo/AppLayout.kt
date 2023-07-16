package com.eco.virtuRun.ui.appLayoutInfo

import com.eco.virtuRun.ui.theme.DynamicTheme
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature

@Composable
fun getWindowLayoutType(context: Context, windowInfo: WindowClassWithSize,
                        foldableInfo: FoldableInfo?, dynamicTheme: DynamicTheme):
    AppLayoutInfo = with(windowInfo) {
    //val initialLetter: String = if (windowInfo.rotation.isPortrait()) "p" else "l"
    val bitmapStatePortrait = BitmapFactory.decodeStream(context.assets.open("backgrounds/p${dynamicTheme.id}.webp"))
    dynamicTheme.imageBitmapPortrait = bitmapStatePortrait?.asImageBitmap()
    val bitmapStateLandscape = BitmapFactory.decodeStream(context.assets.open("backgrounds/l${dynamicTheme.id}.webp"))
    dynamicTheme.imageBitmapLandscape = bitmapStateLandscape?.asImageBitmap()
    val bgImage = if (windowInfo.rotation.isPortrait()) dynamicTheme.imageBitmapPortrait!!
            else dynamicTheme.imageBitmapLandscape!!
    bgImage.prepareToDraw()

    Log.d("VRUN", "windowWH: " + windowWidth + ";" + windowHeight + ", " + sizeDP.width + ";" + sizeDP.height)
    Log.d("VRUN", "screenInfo: " + windowInfo.rotation + ";" + foldableInfo?.bounds)

    // First, I check to see if it's a foldable, with dual screen (isSeparating).
    if ((foldableInfo != null) && foldableInfo.showSeparateScreens) {
        getFoldableAppLayout(foldableInfo, windowInfo, bgImage, dynamicTheme)
    } else {
        // Check for a typical phone size, landscape mode.
        if (windowHeight == WindowHeightSizeClass.Compact)
            getLandscapeLayout(windowInfo, bgImage, dynamicTheme)
        else getPortraitLayout(windowWidth, windowInfo, bgImage, dynamicTheme)
    }
}

@Composable
fun getFoldableAppLayout(foldableInfo: FoldableInfo, windowInfo: WindowClassWithSize, bgImage: ImageBitmap, dynamicTheme: DynamicTheme): AppLayoutInfo {
    return if (foldableInfo.orientation ==  FoldingFeature.Orientation.VERTICAL)
        AppLayoutInfo(AppLayoutMode.PORTRAIT, windowInfo.sizeDP, bgImage, dynamicTheme, foldableInfo, windowInfo)
    else AppLayoutInfo(AppLayoutMode.LANDSCAPE_BIG, windowInfo.sizeDP, bgImage, dynamicTheme, foldableInfo, windowInfo)
}

fun getLandscapeLayout(windowInfo: WindowClassWithSize, bgImage: ImageBitmap, dynamicTheme: DynamicTheme): AppLayoutInfo =
    AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)

fun getPortraitLayout(windowWidth: WindowWidthSizeClass, windowInfo: WindowClassWithSize, bgImage: ImageBitmap, dynamicTheme: DynamicTheme): AppLayoutInfo =
// At this point, I know it's not a landscape/rotated phone size.
    // So let's check the width.
    when (windowWidth) {
        WindowWidthSizeClass.Compact -> AppLayoutInfo(AppLayoutMode.PORTRAIT, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)
        WindowWidthSizeClass.Medium -> {
            // some tablets measure 600.93896; just over 600;
            // let's give this some padding, and make a new cut-off.
            if (windowInfo.sizeDP.width <= 650.dp)
                AppLayoutInfo(AppLayoutMode.PORTRAIT_NARROW, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)
            else AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)
        }
        else -> {
            // override the expanded threshold. 800 vs 1000+ is big diff.
            if (windowInfo.sizeDP.width < 950.dp) AppLayoutInfo(AppLayoutMode.LANDSCAPE_NORMAL, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)
            else AppLayoutInfo(AppLayoutMode.LANDSCAPE_BIG, windowInfo.sizeDP, bgImage, dynamicTheme, null, windowInfo)
        }
    }
