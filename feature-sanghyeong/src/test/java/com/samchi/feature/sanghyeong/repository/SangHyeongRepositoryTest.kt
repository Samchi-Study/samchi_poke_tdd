package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.model.PokemonInfo
import com.samchi.poke.network.PokeApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SangHyeongRepositoryTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var repository: SangHyeongRepository

    @Before
    fun setup() {
        pokeApi = mockk()
        repository = SangHyeongRepositoryImpl(pokeApi = pokeApi)
    }

    @Test
    fun `getPokemonPage Success 불러오기`() = runTest {
        val mockPokemonInfo = mockk<PokemonInfo>()
        coEvery { repository.getPokemonPage() } returns flow {
            emit(Result.success(value = mockPokemonInfo))
        }

        launch {
            repository.getPokemonPage().collect { result ->
                assertTrue(result.isSuccess)
            }
        }
    }
}