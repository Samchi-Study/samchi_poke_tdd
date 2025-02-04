package com.samchi.poke.kanghwi.data

import com.samchi.poke.model.Pokemon
import com.samchi.poke.model.PokemonInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class KanghwiRepositoryTest {

    @MockK
    private lateinit var kanghwiRepositoryImpl: KanghwiRepositoryImpl


    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `getPokemonInfo를 성공적으로 불러온다`() = runTest {
        coEvery {
            kanghwiRepositoryImpl.getPokemonInfo(any(), any())
        } returns flow {
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

        launch(UnconfinedTestDispatcher()) {
            kanghwiRepositoryImpl.getPokemonInfo()
                .collect {
                    assertEquals(it.count, 2)
                    assertEquals(it.results.size, 2)
                    assertEquals(it.results[0].name, "파이리")
                    assertEquals(it.results[1].name, "이상해씨")
                }
        }

        coVerify(exactly = 1) { kanghwiRepositoryImpl.getPokemonInfo(any(), any()) }
    }
}
