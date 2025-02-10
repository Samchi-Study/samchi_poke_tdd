package com.samchi.poke.common.restartablestateflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


interface RestartableStateFlow<T> : StateFlow<T> {

    fun restart()
}


fun <T> Flow<T>.restartableStateIn(
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
