package com.eco.virtuRun.ui

import com.eco.virtuRun.ui.views.main.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModules: Module get() = module { includes(mainViewModelModule) }
val mainViewModelModule = module {
    factory(named("IODispatcher")) { Dispatchers.IO }
    factory { CoroutineScope(get(named("IODispatcher"))) }
    factory { CoroutineScope(get<CoroutineDispatcher>(named("IODispatcher")) + Job()) }
    viewModel { MainViewModel(dispatcher = get(named("IODispatcher"))) }
}
