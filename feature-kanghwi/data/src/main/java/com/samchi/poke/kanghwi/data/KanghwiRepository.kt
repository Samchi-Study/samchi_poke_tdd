package com.samchi.poke.kanghwi.data

import androidx.paging.PagingSource
import com.samchi.poke.model.Pokemon


interface KanghwiRepository {

    fun getPokemonPagingSource(
        pageSize: Int
    ): PagingSource<Int, Pokemon>

}
