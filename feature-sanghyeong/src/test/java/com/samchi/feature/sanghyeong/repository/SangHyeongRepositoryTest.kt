package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun `getPokemonList 불러오기`() {
        runTest {
            every {
                repository.getPokemonList(index = any())
            } answers {
                flow {
                    emit(
                        listOf(
                            Pokemon(name = "피카츄", url = ""),
                            Pokemon(name = "라이츄", url = ""),
                        )
                    )
                }
            }

            repository.getPokemonList(index = 0).collect { result ->
                assertEquals(result.size, 2)
                assertEquals(result[0].name, "피카츄")
                assertEquals(result[1].name, "라이츄")
            }

            verify {
                repository.getPokemonList(index = 0)
            }
        }
    }
}