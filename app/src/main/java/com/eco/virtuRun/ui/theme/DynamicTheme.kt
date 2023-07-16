package com.eco.virtuRun.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.net.toUri
import com.eco.virtuRun.app.hexToColor
import com.eco.virtuRun.app.hexToInt
import com.eco.virtuRun.app.toHex
import com.google.android.material.color.DynamicColorsOptions
import com.google.android.material.color.utilities.Scheme
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import java.io.InputStream

@Serializable
data class DynamicTheme @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val id: Int,
    @EncodeDefault @Serializable(with = ColorHexSerializer::class) val seedColor: Int = -1,
    @EncodeDefault @Serializable(with = ColorSchemeSerializer::class) val darkColorScheme: ColorScheme = darkColorScheme(),
    @EncodeDefault @Serializable(with = ColorSchemeSerializer::class) val lightColorScheme: ColorScheme = lightColorScheme(),
    @Transient var imageBitmapPortrait: ImageBitmap? = null,
    @Transient var imageBitmapLandscape: ImageBitmap? = null
)

@SuppressLint("RestrictedApi")
private fun toColorScheme(scheme: Scheme) = ColorScheme (
    primary = Color(scheme.primary),
    onPrimary = Color(scheme.onPrimary),
    primaryContainer = Color(scheme.primaryContainer),
    onPrimaryContainer = Color(scheme.onPrimaryContainer),
    secondary = Color(scheme.secondary),
    onSecondary = Color(scheme.onSecondary),
    secondaryContainer = Color(scheme.secondaryContainer),
    onSecondaryContainer = Color(scheme.onSecondaryContainer),
    tertiary = Color(scheme.tertiary),
    onTertiary = Color(scheme.onTertiary),
    tertiaryContainer = Color(scheme.tertiaryContainer),
    onTertiaryContainer = Color(scheme.onTertiaryContainer),
    background = Color(scheme.background),
    onBackground = Color(scheme.onBackground),
    surface = Color(scheme.surface),
    onSurface = Color(scheme.onSurface),
    surfaceVariant = Color(scheme.surfaceVariant),
    onSurfaceVariant = Color(scheme.onSurfaceVariant),
    surfaceTint = Color(scheme.shadow),
    inversePrimary = Color(scheme.inversePrimary),
    inverseSurface = Color(scheme.inverseSurface),
    inverseOnSurface = Color(scheme.inverseOnSurface),
    error = Color(scheme.error),
    onError = Color(scheme.onError),
    errorContainer = Color(scheme.errorContainer),
    onErrorContainer = Color(scheme.onErrorContainer),
    outline = Color(scheme.outline),
    outlineVariant = Color(scheme.outlineVariant),
    scrim = Color(scheme.scrim)
)

@SuppressLint("RestrictedApi")
fun createColorSchemeFromBitmap(context: Context, uri: String, isAsset: Boolean = false, id: Int = 0): DynamicTheme {
    val inputStream: InputStream? = if (isAsset) context.assets.open(uri)
        else context.contentResolver.openInputStream(uri.toUri())
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()
    if (bitmap != null) {
        //val imageBitmap = ImageBitmap.imageResource(imageResource)
        val dco = DynamicColorsOptions.Builder().setContentBasedSource(bitmap).build()
        val seedColor = dco.contentBasedSeedColor!!
        Log.d("VRUN", "DynamicTheme - createColorSchemeFromBitmap - contentBasedSeedColor: ${seedColor.toHex()}")
        return DynamicTheme(id, seedColor, toColorScheme(Scheme.lightContent(seedColor)),
            toColorScheme(Scheme.darkContent(seedColor)))
    }
    return DynamicTheme(id)
}
       
@SuppressLint("RestrictedApi")
fun createColorSchemeFromBitmap(context: Context, id: Int): DynamicTheme =
    createColorSchemeFromBitmap(context, "backgrounds/p${id}.webp", true, id)

    object ColorHexSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ColorHex")  {
        element<String>("seedColor")
    }
    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toHex())
    }
    override fun deserialize(decoder: Decoder): Int {
        return hexToInt(decoder.decodeString())
        //return decodeFromHexString<Int>(decodeStringElement(descriptor, 0))
    }
}

object ColorSchemeSerializer : KSerializer<ColorScheme> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ColorScheme") {
        element<String>("primary")
        element<String>("onPrimary")
        element<String>("primaryContainer")
        element<String>("onPrimaryContainer")
        element<String>("inversePrimary")
        element<String>("secondary")
        element<String>("onSecondary")
        element<String>("secondaryContainer")
        element<String>("onSecondaryContainer")
        element<String>("tertiary")
        element<String>("onTertiary")
        element<String>("tertiaryContainer")
        element<String>("onTertiaryContainer")
        element<String>("background")
        element<String>("onBackground")
        element<String>("surface")
        element<String>("onSurface")
        element<String>("surfaceVariant")
        element<String>("onSurfaceVariant")
        element<String>("surfaceTint")
        element<String>("inverseSurface")
        element<String>("inverseOnSurface")
        element<String>("error")
        element<String>("onError")
        element<String>("errorContainer")
        element<String>("onErrorContainer")
        element<String>("outline")
        element<String>("outlineVariant")
        element<String>("scrim")
    }

    @SuppressLint("RestrictedApi")
    override fun serialize(encoder: Encoder, value: ColorScheme) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.primary.toHex())
            encodeStringElement(descriptor, 1, value.onPrimary.toHex())
            encodeStringElement(descriptor, 2, value.primaryContainer.toHex())
            encodeStringElement(descriptor, 3, value.onPrimaryContainer.toHex())
            encodeStringElement(descriptor, 4, value.inversePrimary.toHex())
            encodeStringElement(descriptor, 5, value.secondary.toHex())
            encodeStringElement(descriptor, 6, value.onSecondary.toHex())
            encodeStringElement(descriptor, 7, value.secondaryContainer.toHex())
            encodeStringElement(descriptor, 8, value.onSecondaryContainer.toHex())
            encodeStringElement(descriptor, 9, value.tertiary.toHex())
            encodeStringElement(descriptor, 10, value.onTertiary.toHex())
            encodeStringElement(descriptor, 11, value.tertiaryContainer.toHex())
            encodeStringElement(descriptor, 12, value.onTertiaryContainer.toHex())
            encodeStringElement(descriptor, 13, value.background.toHex())
            encodeStringElement(descriptor, 14, value.onBackground.toHex())
            encodeStringElement(descriptor, 15, value.surface.toHex())
            encodeStringElement(descriptor, 16, value.onSurface.toHex())
            encodeStringElement(descriptor, 17, value.surfaceVariant.toHex())
            encodeStringElement(descriptor, 18, value.onSurfaceVariant.toHex())
            encodeStringElement(descriptor, 19, value.surfaceTint.toHex())
            encodeStringElement(descriptor, 20, value.inverseSurface.toHex())
            encodeStringElement(descriptor, 21, value.inverseOnSurface.toHex())
            encodeStringElement(descriptor, 22, value.error.toHex())
            encodeStringElement(descriptor, 23, value.onError.toHex())
            encodeStringElement(descriptor, 24, value.errorContainer.toHex())
            encodeStringElement(descriptor, 25, value.onErrorContainer.toHex())
            encodeStringElement(descriptor, 26, value.outline.toHex())
            encodeStringElement(descriptor, 27, value.outlineVariant.toHex())
            encodeStringElement(descriptor, 28, value.scrim.toHex())
        }
    }
    
    @SuppressLint("RestrictedApi")
    override fun deserialize(decoder: Decoder): ColorScheme {
        return decoder.decodeStructure(descriptor) {
            val elements = Array(29) { Color(0,0,0,0) }
            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop
                    in 0..28 -> elements[index] = hexToColor(decodeStringElement(descriptor, index))
                    else -> error("Unexpected index: $index")
                }
            }
            ColorScheme(elements[0], elements[1], elements[2], elements[3], elements[4], elements[5], elements[6], elements[7],
                elements[8], elements[9], elements[10], elements[11], elements[12], elements[13], elements[14],
                elements[15], elements[16], elements[17], elements[18], elements[19], elements[20], elements[21],
                elements[22], elements[23], elements[24], elements[25], elements[26], elements[27], elements[28])
        }
    }
}