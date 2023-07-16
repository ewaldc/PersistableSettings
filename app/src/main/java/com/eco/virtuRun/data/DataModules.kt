package com.eco.virtuRun.data

import com.eco.virtuRun.data.model.PersisableSettings
import com.eco.virtuRun.data.repository.AppSettingsFileRepository
import com.eco.virtuRun.data.repository.PersistableFileRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModules: Module get() = module { includes(
    appSettingsRepositoryModule,
    persisableSettingsRepositoryModule
)}


val appSettingsRepositoryModule = module {
    single { AppSettingsFileRepository(androidContext()).load() }
}

val persisableSettingsRepositoryModule = module {
    single { PersisableSettings()(PersisableSettings(), PersistableFileRepository(androidContext(), "test_settings.json", PersisableSettings.serializer())) }
}
