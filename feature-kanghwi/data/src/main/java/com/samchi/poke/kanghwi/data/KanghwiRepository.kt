package com.samchi.poke.kanghwi.data

import com.samchi.poke.model.PokemonInfo
import kotlinx.coroutines.flow.Flow


interface KanghwiRepository {

    fun getPokemonInfo(
        limit: Int = 30,
        offset: Int = 1
    ): Flow<PokemonInfo>

}
