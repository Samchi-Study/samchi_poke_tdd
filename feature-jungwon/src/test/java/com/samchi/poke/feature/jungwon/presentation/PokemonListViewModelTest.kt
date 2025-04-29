package com.samchi.poke.feature.jungwon.presentation

import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.feature.jungwon.presentation.list.PokemonListAction
import com.samchi.feature.jungwon.presentation.list.PokemonListUiState
import com.samchi.feature.jungwon.presentation.list.PokemonListViewModel
import com.samchi.poke.model.Pokemon
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {
    private lateinit var viewModel: PokemonListViewModel
    private lateinit var repository: PokemonRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 Initial 이어야 한다`() {
        // Given
        coEvery { repository.getPokemonListFlow() } returns flow { /* Empty flow */ }
        coJustRun { repository.loadNextPage() }

        // When
        viewModel = PokemonListViewModel(repository)

        // Then
        assertEquals(PokemonListUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `포켓몬 리스트 로딩 시작시 Loading 상태로 변경되어야 한다`() = runTest {
        // Given
        val states = mutableListOf<PokemonListUiState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
                println("State[${states.size}]: $state")
                if (states.size == 2) { // Initial과 Loading 상태를 수집한 후
                    cancel() // 컬렉션 종료
                }
            }
        }

        coEvery { repository.getPokemonListFlow() } returns flow {
            delay(100)
            emit(emptyList())
        }
        coJustRun { repository.loadNextPage() }

        // When
        viewModel = PokemonListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        job.join() // 컬렉션이 완료될 때까지 대기

        // Then
        assertEquals(2, states.size)
        assertEquals(PokemonListUiState.Initial, states[0])
        assertEquals(PokemonListUiState.Loading, states[1])
    }

    @Test
    fun `새로고침했을때 Success 상태가 변경되어야 한다`() = runTest {
        // Given
        val pokemonList = listOf(
            Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
        )
        coEvery { repository.getPokemonListFlow() } returns flowOf(pokemonList)
        coJustRun { repository.loadNextPage() }
        viewModel = PokemonListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.dispatch(PokemonListAction.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Success)
        assertEquals(pokemonList, (viewModel.uiState.value as PokemonListUiState.Success).data)
    }
}