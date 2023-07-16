package com.eco.virtuRun.ui.views.main

import android.util.Log
import com.eco.virtuRun.data.model.AppSettings
import com.eco.virtuRun.ui.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainViewModel (private val dispatcher: CoroutineDispatcher = Dispatchers.IO): MainViewModelInterface, KoinComponent,
    BaseViewModel<MainViewModelInterface.Event, MainViewModelInterface.State>() { //, MainViewModelInterface.Effect
    private val _appSettings = get<AppSettings>()
    override val appSettings = _appSettings

    init {
        setInitialState(MainViewModelInterface.State())
        Log.d("VRUN", "MainViewModel Init")

    }

        //override fun setInitialState() = MainViewModelInterface.State()

    override fun handleEvents(event: MainViewModelInterface.Event) {}

}
