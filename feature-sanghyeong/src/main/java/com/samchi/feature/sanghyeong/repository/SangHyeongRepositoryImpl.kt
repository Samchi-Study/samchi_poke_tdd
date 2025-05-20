package com.samchi.feature.sanghyeong.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SangHyeongRepositoryImpl @Inject constructor(
    private val localDataSource: SangHyeongLocalDataSource,
    private val pagingSource: SangHyeongPagingSource,
) : SangHyeongRepository {
    private var offset: String? = null

    override fun getPokemonListFlow(): Flow<PagingData<SangHyeongPokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { pagingSource },
        ).flow
    }

    override fun getFavoritePokemonsFlow(): Flow<List<SangHyeongPokemonEntity>> {
        return localDataSource.getFavoritePokemons()
    }

    override fun hasMoreData(): Boolean {
        return offset?.isNotEmpty() == true
    }

    override suspend fun toggleFavorite(pokemon: SangHyeongPokemon) {
        if (pokemon.isFavorite) {
            localDataSource.deleteFavoritePokemon(pokemon = pokemon)
        } else {
            localDataSource.insertFavoritePokemon(pokemon = pokemon)
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}