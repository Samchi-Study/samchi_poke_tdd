package com.samchi.poke.feature.jungwon

import com.samchi.feature.jungwon.data.model.toPokemonPage
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.feature.jungwon.data.repository.PokemonRepositoryImpl
import com.samchi.poke.feature.jungwon.util.JsonLoader
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class PokemonRepositoryTest {
    private companion object {
        const val DEFAULT_LIMIT = 20
        const val DEFAULT_OFFSET = 0
    }

    private lateinit var api: PokeApi
    private lateinit var repository: PokemonRepository
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        // PokemonApi의 모의 객체 생성
        api = mockk()
        repository = PokemonRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        // 모든 모의 객체의 호출이 verify되었는지 확인
        // 명시적으로 검증되지 않은 호출이 있다면 테스트 실패
        confirmVerified(api)
    }

    @Test
    fun `포켓몬 리스트 조회 성공시 Result Success를 반환한다`() = runTest {
        // Given
        val jsonString = JsonLoader.loadJson("pokemon_list_success")
        val mockResponse: ResponsePokemonInfo = json.decodeFromString<ResponsePokemonInfo>(jsonString)
        val mockPage = mockResponse.toPokemonPage()
        // api.getPokemonList 호출 시 mockResponse를 반환하도록 설정
        // any()는 어떤 파라미터가 들어와도 매칭되도록 함
        coEvery { api.getPokemonList(any(), any()) } returns mockResponse

        // When
        val result = repository.getPokemonPage()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(mockPage, result.getOrNull())
        // api.getPokemonList가 정확히 1번 호출되었는지,
        // 그리고 파라미터가 DEFAULT_LIMIT, DEFAULT_OFFSET으로 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }

    @Test
    fun `네트워크 에러 발생시 Result Failure를 반환한다`() = runTest {
        // Given
        // api.getPokemonList 호출 시 IOException을 발생시키도록 설정
        coEvery { api.getPokemonList(any(), any()) } throws IOException("Network error")

        // When
        val result = repository.getPokemonPage()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
        // 에러 상황에서도 API가 정확히 1번 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }

    @Test
    fun `첫 페이지 요청시 offset은 0이어야 한다`() = runTest {
        // Given
        val jsonString = JsonLoader.loadJson("pokemon_list_first_page")
        val mockResponse = json.decodeFromString<ResponsePokemonInfo>(jsonString)
        // 정확한 limit, offset 값으로 호출될 때만 응답을 반환하도록 설정
        coEvery {
            api.getPokemonList(
                limit = DEFAULT_LIMIT,
                offset = DEFAULT_OFFSET
            )
        } returns mockResponse

        // When
        repository.getPokemonPage()

        // Then
        // API가 정확한 파라미터로 1번 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(limit = DEFAULT_LIMIT, offset = DEFAULT_OFFSET) }
    }

    @Test
    fun `다음 페이지 요청시 offset이 limit만큼 증가해야 한다`() = runTest {
        // Given
        val nextOffset = DEFAULT_LIMIT  // 20
        val jsonString = JsonLoader.loadJson("pokemon_list_success")
        val mockResponse = json.decodeFromString<ResponsePokemonInfo>(jsonString)
        // 다음 페이지 요청을 위한 offset 값으로 호출될 때의 응답 설정
        coEvery {
            api.getPokemonList(
                limit = DEFAULT_LIMIT,
                offset = nextOffset
            )
        } returns mockResponse

        // When
        repository.getPokemonPage(offset = nextOffset)

        // Then
        // 증가된 offset 값으로 API가 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(limit = DEFAULT_LIMIT, offset = nextOffset) }
    }

    @Test
    fun `마지막 페이지 요청시 next는 null이어야 한다`() = runTest {
        // Given
        val jsonString = JsonLoader.loadJson("pokemon_list_last_page")
        val mockResponse = json.decodeFromString<ResponsePokemonInfo>(jsonString)
        // 마지막 페이지 응답을 시뮬레이션
        coEvery { api.getPokemonList(any(), any()) } returns mockResponse

        // When
        val result = repository.getPokemonPage()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull()?.nextUrl)
        // 기본 파라미터로 API가 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }

    @Test
    fun `빈 결과 리스트 응답시 빈 리스트를 반환한다`() = runTest {
        // Given
        val jsonString = JsonLoader.loadJson("pokemon_list_empty")
        val mockResponse = json.decodeFromString<ResponsePokemonInfo>(jsonString)
        // 빈 리스트 응답을 시뮬레이션
        coEvery { api.getPokemonList(any(), any()) } returns mockResponse

        // When
        val result = repository.getPokemonPage()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.dataList?.size)
        // 기본 파라미터로 API가 호출되었는지 검증
        coVerify(exactly = 1) { api.getPokemonList(DEFAULT_LIMIT, DEFAULT_OFFSET) }
    }
}