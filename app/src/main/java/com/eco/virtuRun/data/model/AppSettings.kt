package com.eco.virtuRun.data.model

import com.eco.virtuRun.data.repository.AppSettingsRepository
import com.eco.virtuRun.data.repository.Persistable
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

typealias HeartRateRange = Pair<Float, Float>

@Serializable
data class AppSettings @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault var userName: String = "System",
    @EncodeDefault var vRunName: String = "System Run") {
    @Transient private var appSettingsRepository: AppSettingsRepository? = null
    fun setRepository (repository: AppSettingsRepository): AppSettings { appSettingsRepository = repository; return this }
    fun save() { appSettingsRepository?.save(this) }
    // Calculates the heart rate range for the user given the desired workout intensity
}

@Serializable
data class PersisableSettings @OptIn(ExperimentalSerializationApi::class) constructor(
    // Active Setting
    @EncodeDefault var userName: String = "System",
    @EncodeDefault var vRunName: String = "System Run",
) : Persistable<PersisableSettings>()

fun initAppSettings() = AppSettings(
    userName = "System",
    vRunName = "System Run",
)

fun previewAppSettings() = initAppSettings()

