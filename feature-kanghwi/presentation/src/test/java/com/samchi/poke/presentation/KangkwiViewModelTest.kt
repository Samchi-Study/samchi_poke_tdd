package com.samchi.poke.presentation

import com.samchi.poke.kanghwi.data.KanghwiRepository
import com.samchi.poke.kanghwi.presentation.KanghwiViewModel
import com.samchi.poke.kanghwi.presentation.UiState
import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.SocketTimeoutException


class KangkwiViewModelTest {

    private val kanghwiRepository: KanghwiRepository = mockk()

    private lateinit var kanghwiViewModel: KanghwiViewModel


    @Test
    fun `UiState가 Success인 경우`() = runTest {
        kanghwiViewModel = KanghwiViewModel(kanghwiRepository)

        val channel = Channel<UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            kanghwiViewModel.uiState.collect {
                channel.send(it)
            }
        }

        val result = channel.receive()
        assertEquals(true, result is UiState.Loading)

        val result2 = channel.receive()
        assertEquals(true, result2 is UiState.Success)

        coVerify(exactly = 1) { kanghwiRepository.getPokemonInfo(any(), any()) }
    }

    @Test
    fun `UiState가 Error 경우`() = runTest {
        coEvery {
            kanghwiRepository.getPokemonInfo(any(), any())
        } answers {
            flow {
                throw SocketTimeoutException("Read Timed out.")
            }
        }

        kanghwiViewModel = KanghwiViewModel(kanghwiRepository)

        val channel = Channel<UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            kanghwiViewModel.uiState.collect {
                channel.send(it)
            }
        }

        val result = channel.receive()
        assertEquals(true, result is UiState.Loading)

        val result2 = channel.receive()
        assertEquals(true, result2 is UiState.Error)

        coVerify(exactly = 1) { kanghwiRepository.getPokemonInfo(any(), any()) }
    }

    @Test
    fun `retry를 통해 다시 데이터를 불러와 Success한 경우`() = runTest {
        coEvery {
            kanghwiRepository.getPokemonInfo(any(), any())
        } answers {
            flow {
                emit(
                    PokemonInfo(
                        count = 2,
                        results = listOf(
                            Pokemon("파이리", ""),
                            Pokemon("이상해씨", ""),
                        )
                    )
                )
            }
        }

        kanghwiViewModel = KanghwiViewModel(kanghwiRepository)

        val channel = Channel<UiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            kanghwiViewModel.uiState.collect {
                channel.send(it)
            }
        }

        val result = channel.receive()
        assertEquals(true, result is UiState.Loading)

        val result2 = channel.receive()
        assertEquals(true, result2 is UiState.Success)

        kanghwiViewModel.retry()

        val result3 = channel.receive()
        assertEquals(true, result3 is UiState.Loading)

        val result4 = channel.receive()
        assertEquals(true, result4 is UiState.Success)

        coVerify { kanghwiRepository.getPokemonInfo(any(), any()) }
    }


    @Test
    fun `andthen 테스트`() = runTest(UnconfinedTestDispatcher()) {
        every {
            kanghwiRepository.getPokemonInfo(any(), any())
        } answers {
            flow {
                emit(
                    PokemonInfo(
                        count = 1,
                        results = listOf(
                            Pokemon("파이리", ""),
                        )
                    )
                )
            }
        } andThen flow {
            emit(
                PokemonInfo(
                    count = 1,
                    results = listOf(
                        Pokemon("이상해씨", ""),
                    )
                )
            )
        }

        val channel = Channel<PokemonInfo>()

        backgroundScope.launch {
            kanghwiRepository.getPokemonInfo().collect {
                channel.send(it)
            }
        }

        backgroundScope.launch {
            kanghwiRepository.getPokemonInfo().collect {
                channel.send(it)
            }
        }

        val result = channel.receive()
        val result2 = channel.receive()

        assertEquals("파이리", result.results[0].name)
        assertEquals("이상해씨", result2.results[0].name)
    }
}
