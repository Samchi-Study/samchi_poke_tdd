package com.samchi.poke.kanghwi.data

import androidx.paging.PagingSource
import com.samchi.poke.kanghwi.pagingsource.PokemonPagingSource
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import javax.inject.Inject


internal class KanghwiRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi
) : KanghwiRepository {

    override fun getPokemonPagingSource(pageSize: Int): PagingSource<Int, Pokemon> =
        PokemonPagingSource(
            pageSize = pageSize,
            onRequestPage = { page, size -> pokeApi.getPokemonList(size, page) }
        )

}
