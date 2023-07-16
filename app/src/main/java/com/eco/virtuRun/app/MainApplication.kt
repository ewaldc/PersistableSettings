package com.eco.virtuRun.app

import android.app.Application
import com.eco.virtuRun.data.dataModules
import com.eco.virtuRun.ui.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(module { includes(appModules, dataModules, uiModules) })
        }
    }
}
