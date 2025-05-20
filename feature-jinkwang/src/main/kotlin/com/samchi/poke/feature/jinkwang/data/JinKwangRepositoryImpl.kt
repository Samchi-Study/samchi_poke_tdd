package com.samchi.poke.feature.jinkwang.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteDao
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteEntity
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonDao
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class JinKwangRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val pokemonDao: PokemonDao,
    private val favoriteDao: FavoriteDao,
    private val pokemonRemoteMediator: PokemonRemoteMediator,
) : JinKwangRepository {

    override fun getPokemonFlow(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PokemonRemoteMediator.PAGING_SIZE,
                enablePlaceholders = false
            ),
            initialKey = 0,
            remoteMediator = pokemonRemoteMediator,
            pagingSourceFactory = { pokemonDao.getPokemonPagingSource() }
        ).flow.combine(favoriteDao.getFavorites()) { pagingData, favorites ->
            pagingData.map { entity ->
                entity.copy(isFavorite = favorites.any { it.name == entity.name }).toItem()
            }
        }
    }

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): Result<List<Pokemon>> = runCatching {
        val favoritePokemonNames = pokemonDao.getAllPokemons().map { it.name }
        pokeApi.getPokemonList(
            limit = limit,
            offset = offset
        ).results.map {
            it.toPokemon(
                isFavorite = { favoritePokemonNames.contains(it.name) }
            )
        }
    }

    override suspend fun favoritePokemon(name: String) {
        favoriteDao.insert(FavoriteEntity(name))
        pokemonDao.updateFavorite(name, true)
    }

    override suspend fun unFavoritePokemon(name: String) {
        favoriteDao.delete(name)
        pokemonDao.updateFavorite(name, false)
    }

    private inline fun ResponsePokemon.toPokemon(
        isFavorite: () -> Boolean,
    ): Pokemon {
        return Pokemon(
            nameField = name,
            imageUrl = url,
            isFavorite = isFavorite(),
        )
    }

    private fun PokemonEntity.toItem(): Pokemon {
        return Pokemon(
            nameField = name,
            imageUrl = url,
            isFavorite = isFavorite,
        )
    }
}