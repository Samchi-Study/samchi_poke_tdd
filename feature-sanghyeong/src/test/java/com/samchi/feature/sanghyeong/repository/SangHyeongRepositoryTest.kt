package com.samchi.feature.sanghyeong.repository

import com.samchi.poke.network.PokeApi
import io.mockk.mockk
import org.junit.Before

class SangHyeongRepositoryTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var repository: SangHyeongRepository

    @Before
    fun setup() {
        pokeApi = mockk()
        repository = SangHyeongRepositoryImpl(pokeApi = pokeApi)
    }
}