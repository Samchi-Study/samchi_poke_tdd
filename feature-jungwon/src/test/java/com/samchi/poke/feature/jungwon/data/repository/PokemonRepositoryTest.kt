package com.samchi.poke.feature.jungwon.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.samchi.feature.jungwon.data.model.toPokemonPage
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.feature.jungwon.data.repository.PokemonRepositoryImpl
import com.samchi.poke.feature.jungwon.util.JsonLoader
import com.samchi.poke.model.Pokemon
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryTest {
    private companion object {
        const val DEFAULT_LIMIT = 20
        const val DEFAULT_OFFSET = 0
        const val TEST_DATASTORE_FILE = "build/test_datastore.preferences_pb"
    }

    private lateinit var api: PokeApi
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: PokemonRepository
    private val json = Json { ignoreUnknownKeys = true }
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        api = mockk()
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(TEST_DATASTORE_FILE) }
        )
        repository = PokemonRepositoryImpl(api, dataStore)
    }

    @After
    fun tearDown() {
        confirmVerified(api)
        File(TEST_DATASTORE_FILE).delete()
    }

    // API 관련 테스트
    @Test
    fun `getPokemonPage 성공시 PokemonPage를 반환해야 한다`() = runTest {
        // Given
        val responseJson = JsonLoader.loadJson("pokemon_list_success")
        val response = json.decodeFromString<ResponsePokemonInfo>(responseJson)
        
        coEvery { 
            api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET)
        } returns response

        // When
        val result = repository.getPokemonPage(DEFAULT_LIMIT, DEFAULT_OFFSET)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(response.toPokemonPage(), result.getOrNull())
        coVerify { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }

    @Test
    fun `getPokemonPage 실패시 예외를 반환해야 한다`() = runTest {
        // Given
        coEvery { 
            api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET)
        } throws IOException()

        // When
        val result = repository.getPokemonPage(DEFAULT_LIMIT, DEFAULT_OFFSET)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
        coVerify { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }

    // 좋아요 관련 테스트
    @Test
    fun `getFavoritePokemonIds는 초기에 빈 Set을 반환해야 한다`() = runTest(testDispatcher) {
        // When
        val favorites = repository.getFavoritePokemonIds().first()
        
        // Then
        assertTrue(favorites.isEmpty())
    }

    @Test
    fun `toggleFavorite로 좋아요 추가시 getFavoritePokemonIds에서 해당 ID가 포함되어야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

        // When
        repository.toggleFavorite(pokemon)
        
        // Then
        val favorites = repository.getFavoritePokemonIds().first()
        assertTrue(favorites.contains(pokemon.id))
    }

    @Test
    fun `toggleFavorite로 좋아요 취소시 getFavoritePokemonIds에서 해당 ID가 제거되어야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
        repository.toggleFavorite(pokemon) // 먼저 추가

        // When
        repository.toggleFavorite(pokemon) // 취소
        
        // Then
        val favorites = repository.getFavoritePokemonIds().first()
        assertFalse(favorites.contains(pokemon.id))
    }

    @Test
    fun `여러 포켓몬 좋아요 추가시 모두 저장되어야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon1 = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
        val pokemon2 = Pokemon(name = "ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/")

        // When
        repository.toggleFavorite(pokemon1)
        repository.toggleFavorite(pokemon2)

        // Then
        val favorites = repository.getFavoritePokemonIds().first()
        assertTrue(favorites.containsAll(listOf(pokemon1.id, pokemon2.id)))
        assertEquals(2, favorites.size)
    }

    @Test
    fun `isFavorite는 좋아요 상태를 정확히 반환해야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

        // When & Then - 초기 상태
        assertFalse(repository.isFavorite(pokemon))

        // When & Then - 좋아요 추가 후
        repository.toggleFavorite(pokemon)
        assertTrue(repository.isFavorite(pokemon))

        // When & Then - 좋아요 취소 후
        repository.toggleFavorite(pokemon)
        assertFalse(repository.isFavorite(pokemon))
    }

    @Test
    fun `같은 포켓몬을 여러 번 토글해도 정상적으로 동작해야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

        // When & Then
        repository.toggleFavorite(pokemon) // 추가
        assertTrue(repository.isFavorite(pokemon))
        assertEquals(1, repository.getFavoritePokemonIds().first().size)

        repository.toggleFavorite(pokemon) // 제거
        assertFalse(repository.isFavorite(pokemon))
        assertEquals(0, repository.getFavoritePokemonIds().first().size)

        repository.toggleFavorite(pokemon) // 다시 추가
        assertTrue(repository.isFavorite(pokemon))
        assertEquals(1, repository.getFavoritePokemonIds().first().size)
    }

    @Test
    fun `다른 ID를 가진 같은 이름의 포켓몬은 별도로 처리되어야 한다`() = runTest(testDispatcher) {
        // Given
        val pokemon1 = Pokemon(name = "test", url = "https://pokeapi.co/api/v2/pokemon/1/")
        val pokemon2 = Pokemon(name = "test", url = "https://pokeapi.co/api/v2/pokemon/2/")

        // When
        repository.toggleFavorite(pokemon1)
        repository.toggleFavorite(pokemon2)

        // Then
        val favorites = repository.getFavoritePokemonIds().first()
        assertTrue(favorites.contains(pokemon1.id))
        assertTrue(favorites.contains(pokemon2.id))
        assertEquals(2, favorites.size)
    }
}