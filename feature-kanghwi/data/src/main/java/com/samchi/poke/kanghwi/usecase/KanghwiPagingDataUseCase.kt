package com.samchi.poke.kanghwi.usecase

import androidx.paging.PagingData
import com.samchi.poke.kanghwi.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


interface KanghwiPagingDataUseCase {

    operator fun invoke(scope: CoroutineScope): Flow<PagingData<Pokemon>>

}
