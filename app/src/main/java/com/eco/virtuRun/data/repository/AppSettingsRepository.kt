package com.eco.virtuRun.data.repository

import android.content.Context
import android.util.Log
import com.eco.virtuRun.data.model.AppSettings
import com.eco.virtuRun.data.model.initAppSettings
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

interface AppSettingsRepository {
    fun save(appSettings: AppSettings): AppSettings
    fun load(): AppSettings
}


class AppSettingsFileRepository (private val context: Context,
    //private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): AppSettingsRepository {
    private val settingsFile = "app_settings.json"
    private val path = context.getExternalFilesDir(null).toString() + "/$settingsFile"
    private val file = File(path)
    private val cachedImagesDirectory = File(context.getExternalFilesDir("/"),"imageCache")
    //private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        Log.d("VRUN", "AppSettingsFileRepository")
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadInitialSettings(): AppSettings {
        if (!cachedImagesDirectory.mkdirs()) Log.e("VRUN", "unable to create image cache directory")
        val file = context.assets.open(settingsFile);
        return try {
            Json.decodeFromStream<AppSettings>(file).setRepository(this@AppSettingsFileRepository)
        } catch (e: SerializationException) { //JsonDecodingException
            Log.e("VRUN", "initial app settings corrupt")
            save(initAppSettings())
        }
    }


    @OptIn(ExperimentalSerializationApi::class)
    override fun load(): AppSettings {
        Log.d("VRUN", "loadAppSettings")
        if (file.exists()) {
            //setAppSettings(initAppSettings())
            try {
                return Json.decodeFromStream<AppSettings>(FileInputStream(file)).setRepository(this)
            } catch (e: SerializationException) { //JsonDecodingException
                Log.w("VRUN", "saved app settings corrupt")
                val json = Json { ignoreUnknownKeys = true }
                try {
                    return json.decodeFromStream<AppSettings>(FileInputStream(file)).setRepository(this)
                    // try to upgrade
                } catch (e: Exception) { //JsonDecodingException
                    Log.e("VRUN", "saved app settings can not be recovered, re-initializing")
                }
            }
        }
        return loadInitialSettings()
    }
    //override fun load(): AppSettings = runBlocking { return@runBlocking loadSettings() }

    @OptIn(ExperimentalSerializationApi::class)
    override fun save(appSettings: AppSettings): AppSettings {
        Json.encodeToStream(appSettings, FileOutputStream(file))
        return appSettings.setRepository(this@AppSettingsFileRepository)
    }
}
    /*
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getAppSettings(): Result<AppSettings> = coroutineCall(dispatcher) {
        Log.d("VRUN", "getAppSettings")
        if (!file.exists()) {
            val appSettings = AppSettings()
            setAppSettings(appSettings)
            //val f = context.openFileOutput(path, Context.MODE_PRIVATE)
            //f.apply { write("{}".toByteArray()) close() }
            return@coroutineCall AppSettings()
        }
        return@coroutineCall Json.decodeFromStream<AppSettings>(FileInputStream(file))
    }

// normal returns are not allowed here

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun setAppSettings(appSettings: AppSettings): Result<Boolean> = coroutineCall(dispatcher) {
        Json.encodeToStream(appSettings, FileOutputStream(file))
        return@coroutineCall true
    }

}

suspend inline fun <T> coroutineCall(
    dispatcher: CoroutineDispatcher,
    crossinline call: suspend () -> T
): Result<T> = runCatching { withContext(dispatcher) { call.invoke() }}
 */
