package com.eco.virtuRun.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModules: Module get() = module { includes(mainModule) }

val mainModule = module {
    factory(named("IODispatcher")) { Dispatchers.IO + Job() }
    factory(named("MainDispatcher")) { Dispatchers.Main + Job() }
    factory { CoroutineScope(get(named("IODispatcher"))) }
    factory { CoroutineScope(get(named("MainDispatcher"))) }
    factory { CoroutineScope(get<CoroutineDispatcher>(named("IODispatcher")) + Job()) }
    factory { CoroutineScope(get<CoroutineDispatcher>(named("MainDispatcher")) + Job()) }
}


