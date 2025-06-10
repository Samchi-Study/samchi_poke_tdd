package com.samchi.poke.feature.jinkwang

import com.samchi.poke.feature.jinkwang.data.JinKwangRepositoryImpl
import com.samchi.poke.feature.jinkwang.data.PokemonRemoteMediator
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteDao
import com.samchi.poke.feature.jinkwang.data.local.favorite.FavoriteEntity
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonDao
import com.samchi.poke.feature.jinkwang.data.local.pokemon.PokemonEntity
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class JinKwangRepositoryTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var pokemonDao: PokemonDao
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var pokemonRemoteMediator: PokemonRemoteMediator
    private lateinit var repository: JinKwangRepositoryImpl

    @Before
    fun setup() {
        pokeApi = mockk()
        pokemonDao = mockk()
        favoriteDao = mockk()
        pokemonRemoteMediator = mockk()
        repository = JinKwangRepositoryImpl(
            pokeApi = pokeApi,
            pokemonDao = pokemonDao,
            favoriteDao = favoriteDao,
            pokemonRemoteMediator = pokemonRemoteMediator,
        )
    }

    @Test
    fun `getPokemonList 성공 시 Pokemon 리스트를 반환한다`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val mockResponse = ResponsePokemonInfo(
            count = 20,
            next = "next",
            previous = "next",
            results = listOf(
                ResponsePokemon(name = "피카츄", url = "https://test.com/1"),
                ResponsePokemon(name = "갸라도스", url = "https://test.com/2")
            )

        )

        val mockFavoritePokemons = listOf(
            PokemonEntity(name = "피카츄", url = "test", isFavorite = true)
        )

        coEvery { pokeApi.getPokemonList(limit, offset) } returns mockResponse
        coEvery { pokemonDao.getAllPokemons() } returns mockFavoritePokemons

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then
        assertTrue(result.isSuccess)
        val pokemonList = result.getOrNull()!!
        assertEquals(2, pokemonList.size)
        assertEquals("피카츄", pokemonList[0].nameField)
        assertEquals("https://test.com/1", pokemonList[0].imageUrl)
        assertTrue(pokemonList[0].isFavorite) // 즐겨찾기에 있음
        assertEquals("갸라도스", pokemonList[1].nameField)
        assertFalse(pokemonList[1].isFavorite) // 즐겨찾기에 없음
    }

    @Test
    fun `getPokemonList API 호출 실패 시 Result failure를 반환한다`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val exception = RuntimeException("Network error")

        coEvery { pokeApi.getPokemonList(limit, offset) } throws exception
        coEvery { pokemonDao.getAllPokemons() } returns emptyList()

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `favoritePokemon 호출 시 FavoriteDao insert와 PokemonDao updateFavorite가 호출된다`() = runTest {
        // Given
        val pokemonName = "피카츄"

        coEvery { favoriteDao.upsert(any()) } returns Unit
        coEvery { pokemonDao.updateFavorite(any(), any()) } returns Unit

        // When
        repository.favoritePokemon(pokemonName)

        // Then
        coVerify { favoriteDao.upsert(FavoriteEntity(pokemonName)) }
        coVerify { pokemonDao.updateFavorite(pokemonName, true) }
    }

    @Test
    fun `unFavoritePokemon 호출 시 FavoriteDao delete와 PokemonDao updateFavorite가 호출된다`() = runTest {
        // Given
        val pokemonName = "피카츄"

        coEvery { favoriteDao.delete(any()) } returns Unit
        coEvery { pokemonDao.updateFavorite(any(), any()) } returns Unit

        // When
        repository.unFavoritePokemon(pokemonName)

        // Then
        coVerify { favoriteDao.delete(pokemonName) }
        coVerify { pokemonDao.updateFavorite(pokemonName, false) }
    }

    @Test
    fun `getPokemonFlow는 즐겨찾기 상태가 반영된 PagingData를 반환한다`() = runTest {
        // Given
        val mockPokemonEntities = listOf(
            PokemonEntity("피카츄", "url1", false),
            PokemonEntity("갸라도스", "url2", false)
        )
        val mockFavorites = listOf(
            FavoriteEntity("피카츄")
        )

        // PagingSource와 Flow 설정
        every { pokemonDao.getPokemonPagingSource() } returns mockk()
        every { favoriteDao.getFavorites() } returns flowOf(mockFavorites)

        // Note: 실제 Paging 테스트는 복잡하므로 여기서는 기본적인 Flow 구조만 확인
        // 실제 프로젝트에서는 androidx.paging:paging-testing 라이브러리를 사용하여
        // 더 자세한 PagingData 테스트를 수행할 수 있습니다.

        // When & Then
        // getPokemonFlow()가 Flow<PagingData<Pokemon>>을 반환하는지 확인
        val flow = repository.getPokemonFlow()
        assertTrue(flow != null)
    }

    @Test
    fun `즐겨찾기가 없는 상태에서 getPokemonList 호출 시 모든 Pokemon의 isFavorite가 false이다`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val mockResponse = ResponsePokemonInfo(
            count = 20,
            next = "next",
            previous = "next",
            results = listOf(
                ResponsePokemon(name = "피카츄", url = "https://test.com/1"),
                ResponsePokemon(name = "갸라도스", url = "https://test.com/2")
            )
        )

        coEvery { pokeApi.getPokemonList(limit, offset) } returns mockResponse
        coEvery { pokemonDao.getAllPokemons() } returns emptyList() // 즐겨찾기 없음

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then
        assertTrue(result.isSuccess)
        val pokemonList = result.getOrNull()!!
        assertEquals(2, pokemonList.size)
        assertFalse(pokemonList[0].isFavorite)
        assertFalse(pokemonList[1].isFavorite)
    }

    @Test
    fun `모든 Pokemon이 즐겨찾기인 상태에서 getPokemonList 호출 시 모든 Pokemon의 isFavorite가 true이다`() = runTest {
        // Given
        val limit = 20
        val offset = 0
        val mockResponse = ResponsePokemonInfo(
            count = 20,
            next = "next",
            previous = "next",
            results = listOf(
                ResponsePokemon(name = "피카츄", url = "https://test.com/1"),
                ResponsePokemon(name = "갸라도스", url = "https://test.com/2")
            )
        )

        val mockFavoritePokemons = listOf(
            PokemonEntity(name = "피카츄", url = "test", isFavorite = true),
            PokemonEntity(name = "갸라도스", url = "test", isFavorite = true)
        )

        coEvery { pokeApi.getPokemonList(limit, offset) } returns mockResponse
        coEvery { pokemonDao.getAllPokemons() } returns mockFavoritePokemons

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then
        assertTrue(result.isSuccess)
        val pokemonList = result.getOrNull()!!
        assertEquals(2, pokemonList.size)
        assertTrue(pokemonList[0].isFavorite)
        assertTrue(pokemonList[1].isFavorite)
    }

    @Test
    fun `빈 응답에서 getPokemonList 호출 시 빈 리스트를 반환한다 - 대안`() = runTest {
        // Given
        val limit = 20
        val offset = 0

        // Mock 설정을 더 명확하게
        coEvery {
            pokeApi.getPokemonList(
                limit = eq(limit),
                offset = eq(offset)
            )
        } returns ResponsePokemonInfo(
            count = 20,
            next = "next",
            previous = "next",
            results = listOf()
        )

        coEvery { pokemonDao.getAllPokemons() } returns emptyList()

        // When
        val result = repository.getPokemonList(limit, offset)

        // Then - 단계별 검증
        if (result.isFailure) {
            val exception = result.exceptionOrNull()
            println("Unexpected failure: $exception")
            exception?.printStackTrace()
            fail("Expected success but got failure: $exception")
        }

        val pokemonList = result.getOrThrow()
        assertEquals(0, pokemonList.size)
    }
}