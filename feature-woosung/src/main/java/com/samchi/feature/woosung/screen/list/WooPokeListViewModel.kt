package com.samchi.feature.woosung.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.samchi.feature.woosung.data.repository.WoosungPokeRepository
import com.samchi.feature.woosung.data.repository.WoosungPokeRepositoryImp
import com.samchi.poke.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WooPokeListViewModel @Inject constructor(
    private val pokeRepository: WoosungPokeRepository
) : ViewModel() {


    val pagingList = pokeRepository.getPokemonList().cachedIn(viewModelScope)

}
