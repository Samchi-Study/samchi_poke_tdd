package com.samchi.poke.kanghwi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.samchi.poke.kanghwi.data.KanghwiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
internal class KanghwiViewModel @Inject constructor(
    private val kanghwiRepository: KanghwiRepository
) : ViewModel() {

    val pager = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { kanghwiRepository.getPokemonPagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)


}
