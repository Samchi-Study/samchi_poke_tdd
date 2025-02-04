package com.samchi.poke.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


internal interface RestartableStateFlow<T> : StateFlow<T> {
    fun restart()
}


internal fun <T> Flow<T>.restartableStateIn(
    scope: CoroutineScope,
    sharingStarted: SharingStarted,
    initialValue: T
): RestartableStateFlow<T> {
    val sharingRestartableImpl = SharingRestartableImpl(sharingStarted)
    val stateFlow = stateIn(
        scope = scope,
        started = sharingRestartableImpl,
        initialValue = initialValue
    )

    return object : RestartableStateFlow<T>, StateFlow<T> by stateFlow {
        override fun restart() = sharingRestartableImpl.restart()
    }
}
