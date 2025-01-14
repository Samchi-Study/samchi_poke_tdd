package com.samchi.poke.kanghwi

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class KanghwiViewModel @Inject constructor() :ViewModel() {

    val flow = MutableStateFlow(1)

}
