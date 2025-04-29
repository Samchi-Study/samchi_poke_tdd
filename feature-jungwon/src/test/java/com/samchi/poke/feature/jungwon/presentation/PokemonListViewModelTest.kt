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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
        coEvery { repository.getPokemonListFlow() } returns flow {
            delay(100)
            emit(emptyList())
        }
        coJustRun { repository.loadNextPage() }

        // When
        viewModel = PokemonListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(PokemonListUiState.Initial, viewModel.uiState.first())
        assertEquals(PokemonListUiState.Loading, viewModel.uiState.drop(1).first())
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