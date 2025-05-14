package com.samchi.poke.kanghwi.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.samchi.poke.kanghwi.model.Pokemon
import com.samchi.poke.kanghwi.repository.LocalDataSource
import com.samchi.poke.kanghwi.repository.RemoteDataSource
import com.samchi.poke.kanghwi.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


internal class KanghwiPagingDataUseCaseImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : KanghwiPagingDataUseCase {

    override fun invoke(scope: CoroutineScope): Flow<PagingData<Pokemon>> = combine(
        localDataSource.getFavoritePokemonList(),
        remoteDataSource.getPokemonPagingFlow().cachedIn(scope)
    ) { favoritePokemonEntities, pagingData ->
        pagingData.map { entity ->
            favoritePokemonEntities.find { it.id == entity.id }?.let {
                entity.toModel().copy(isFavorite = true)
            } ?: entity.toModel().copy(isFavorite = false)
        }
    }
}
