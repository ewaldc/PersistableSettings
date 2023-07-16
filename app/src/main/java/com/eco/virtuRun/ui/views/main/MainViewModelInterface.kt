package com.eco.virtuRun.ui.views.main

import com.eco.virtuRun.data.model.AppSettings
import com.eco.virtuRun.ui.ViewEvent
import com.eco.virtuRun.ui.ViewState

interface MainViewModelInterface {
    val appSettings: AppSettings
    sealed class Event: ViewEvent {
        data class UserChanged(val userName: String) : Event()
        data class ToMainDestination(val destination: String): Event()
    }

    data class State(
        var userMessage: String? = null,
    ) : ViewState

}
