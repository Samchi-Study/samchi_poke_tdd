package com.samchi.feature.woosung.data.repository

import androidx.paging.PagingData
import com.samchi.poke.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface WoosungPokeRepository {
     fun getPokemonList(): Flow<PagingData<Pokemon>>
}
