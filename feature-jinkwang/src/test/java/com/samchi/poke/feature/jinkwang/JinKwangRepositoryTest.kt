package com.samchi.poke.feature.jinkwang

import com.samchi.poke.feature.jinkwang.data.JinKwangRepositoryImpl
import com.samchi.poke.feature.jinkwang.data.PokemonDao
import com.samchi.poke.feature.jinkwang.data.PokemonEntity
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class JinKwangRepositoryTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var pokemonDao: PokemonDao
    private lateinit var repository: JinKwangRepositoryImpl

    @Before
    fun setup() {
        pokeApi = mockk()
        pokemonDao = mockk()
        repository =  JinKwangRepositoryImpl(pokeApi, pokemonDao)
    }

    @Test
    fun `getPockemonList함수를 호출했을 때 좋아요 한 상태가 있는 포켓몬 리스트를 리턴한다`() = runBlocking {
        // Given
        val limit = 20
        val offset = 0

        val mockResponsePokemonList = listOf(
            ResponsePokemon(name = "Pikachu", url = "https://pokeapi.co/pikachu"),
            ResponsePokemon(name = "Bulbasaur", url = "https://pokeapi.co/bulbasaur")
        )
        val mockApiResponse = ResponsePokemonInfo(
            count = 0,
            next = null,
            previous = null,
            results = mockResponsePokemonList
        )

        coEvery { pokeApi.getPokemonList(limit, offset) } returns mockApiResponse
        coEvery { pokemonDao.getAllPokemons() } returns listOf(PokemonEntity("Pikachu"))

        // When
        val result = repository.getPockemonList(limit, offset)

        // Then
        assertTrue(result.isSuccess)
        val pokemonList = result.getOrNull()
        assertEquals(2, pokemonList?.size)
        assertEquals("Pikachu", pokemonList?.get(0)?.nameField)
        assertTrue(pokemonList?.get(0)?.isFavorite == true)
        assertEquals("Bulbasaur", pokemonList?.get(1)?.nameField)
        assertTrue(pokemonList?.get(1)?.isFavorite == false)

        coVerify(exactly = 1) { pokeApi.getPokemonList(limit, offset) }
        coVerify(exactly = 1) { pokemonDao.getAllPokemons() }
    }

    @Test
    fun `favoritePokemon 함수를 호출했을 때 Dao의 insert가 호출되어 포켓몬 정보를 저장한다`() = runBlocking {
        // Given
        val pokemonName = "Pikachu"
        coEvery { pokemonDao.insert(PokemonEntity(pokemonName)) } just Runs

        // When
        repository.favoritePokemon(pokemonName)

        // Then
        coVerify(exactly = 1) { pokemonDao.insert(PokemonEntity(pokemonName)) }
    }

    @Test
    fun `unFavoritePokemon 함수를 호출했을 때 Dao의 delete가 호출되어 포켓몬 정보를 제거한다`() = runBlocking {
        // Given
        val pokemonName = "Pikachu"
        coEvery { pokemonDao.deletePokemonById(pokemonName) } just Runs

        // When
        repository.unFavoritePokemon(pokemonName)

        // Then
        coVerify(exactly = 1) { pokemonDao.deletePokemonById(pokemonName) }
    }
}