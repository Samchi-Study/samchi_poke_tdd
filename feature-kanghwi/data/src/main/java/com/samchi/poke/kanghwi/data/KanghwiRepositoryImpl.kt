package com.samchi.poke.kanghwi.data

import com.samchi.poke.kanghwi.toModel
import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.PokeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class KanghwiRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : KanghwiRepository {

    override fun getPokemonInfo(limit: Int, offset: Int): Flow<PokemonInfo> = flow {
        emit(
            pokeApi.getPokemonList(limit, offset)
        )
    }
        .map { it.toModel() }
}
