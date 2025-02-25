package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SangHyeongRepositoryTest {

    @MockK
    private lateinit var repository: SangHyeongRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getPokemonList 불러오기`() = runTest {
        coEvery {
            repository.getPokemonList(
                index = any(),
                onStart = any(),
                onCompletion = any(),
                onError = any(),
            )
        } coAnswers {
            flow {
                emit(
                    listOf(
                        Pokemon(name = "피카츄", url = ""),
                    )
                )
            }
            }

        launch(UnconfinedTestDispatcher()) {
            repository.getPokemonList(
                index = 0,
                onStart = { },
                onCompletion = { },
                onError = { }
            ).collect { result ->
                assertEquals(result.size, 1)
                assertEquals(result[0].name, "피카츄")
            }
        }

        coVerify {
            repository.getPokemonList(
                index = 0,
                onStart = { },
                onCompletion = { },
                onError = { },
            )
        }
    }
}