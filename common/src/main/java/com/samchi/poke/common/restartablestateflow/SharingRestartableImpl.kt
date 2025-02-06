package com.samchi.poke.common.restartablestateflow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge


internal interface SharingRestartable : SharingStarted {
    fun restart()
}


internal class SharingRestartableImpl(
    private val sharingStarted: SharingStarted
) : SharingRestartable {

    private val restartFlow = MutableSharedFlow<SharingCommand>(extraBufferCapacity = 2)


    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> =
        merge(restartFlow, sharingStarted.command(subscriptionCount))

    override fun restart() {
        restartFlow.tryEmit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
        restartFlow.tryEmit(SharingCommand.START)
    }
}
