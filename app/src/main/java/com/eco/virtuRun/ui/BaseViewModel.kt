package com.eco.virtuRun.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewEvent
interface ViewState
interface ViewEffect
//const val VIEW_EFFECTS_KEY = "view_effects_key"

/* each ViewModel class will have one or more:
 - Events, implemented as a shared flow (so multiple interested parties can listen to them
 - States, which will trigger recompositions
 - Effects, (aka side-effects) which are the outcomesof events or actions that be passed on to new screens/view/models or in the case of navigation effect, will determine the next (screen) destination
 */
abstract class BaseViewModel<UIEvent: ViewEvent, UiState: ViewState> : ViewModel() {  //, UIEffect: ViewEffect
    //abstract fun setInitialState(): UiState
    abstract fun handleEvents(event: UIEvent)
    //protected lateinit var initialState: UiState //by lazy { setInitialState() }

    //private val _viewState: MutableState<UiState> by lazy { mutableStateOf(initialState) }
    protected fun setInitialState(initialState: UiState) { internalViewState = mutableStateOf(initialState) }

    private lateinit var internalViewState: MutableState<UiState>

    //private val _viewState: MutableStateFlow<UiState> get() { TODO() }
    val viewState: State<UiState> by lazy { internalViewState }
    protected fun setState(transformer: UiState.() -> UiState) {
        internalViewState.value = viewState.value.transformer()
    }

    private val _event: MutableSharedFlow<UIEvent> = MutableSharedFlow()
    fun emitEvent(event: UIEvent) { viewModelScope.launch { _event.emit(event) }}

    /*
    private val _effect: Channel<UIEffect> = Channel()
    val effect = _effect.receiveAsFlow()
    protected fun setEffect(builder: () -> UIEffect) {
        viewModelScope.launch { _effect.send(builder()) }
    }
    */

    init {
        viewModelScope.launch { _event.collect { handleEvents(it) }} // Subscribe to flow of events
    }
}