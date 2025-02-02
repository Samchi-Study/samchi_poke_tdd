package com.samchi.poke.feature.jungwon.presentation

import com.samchi.feature.jungwon.data.model.PokemonPage
import com.samchi.feature.jungwon.data.repository.PokemonRepository
import com.samchi.feature.jungwon.presentation.PokemonListUiState
import com.samchi.feature.jungwon.presentation.PokemonListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {
    private lateinit var viewModel: PokemonListViewModel
    private lateinit var repository: PokemonRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = PokemonListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 Initial 이어야 한다`() {
        // Then
        assertEquals(PokemonListUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `포켓몬 리스트 로딩 시작시 Loading 상태로 변경되어야 한다`() = runTest {
        // Given
        coEvery { repository.getPokemonPage(any(), any()) } coAnswers {
            delay(1000)
            Result.success(mockk())
        }

        // When
        viewModel.loadFirstPage()
        testDispatcher.scheduler.advanceTimeBy(100)

        // Then
        assertEquals(PokemonListUiState.Loading, viewModel.uiState.value)
        coVerify { repository.getPokemonPage(any(), any()) }
    }

    @Test
    fun `포켓몬 리스트 로딩 성공시 Success 상태로 변경되어야 한다`() = runTest {
        // Given
        val mockPage = mockk<PokemonPage>()
        coEvery { repository.getPokemonPage() } returns Result.success(mockPage)

        // When
        viewModel.loadFirstPage()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Success)
        assertEquals(mockPage, (viewModel.uiState.value as PokemonListUiState.Success).data)
        coVerify(exactly = 1) { repository.getPokemonPage() }
    }

    @Test
    fun `네트워크 에러 발생시 Error 상태로 변경되어야 한다`() = runTest {
        // Given
        coEvery { repository.getPokemonPage() } returns Result.failure(IOException("Network error"))

        // When
        viewModel.loadFirstPage()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Error)
        coVerify(exactly = 1) { repository.getPokemonPage() }
    }

    @Test
    fun `다음 페이지 요청시 offset이 증가해야 한다`() = runTest {
        // Given
        val firstPage = mockk<PokemonPage> {
            every { nextUrl } returns "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20"
            every { nextOffset } returns 20
        }
        val secondPage = mockk<PokemonPage> {
            every { nextUrl } returns "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"
            every { nextOffset } returns 40
        }

        // 첫 페이지 요청에 대한 mock
        coEvery { repository.getPokemonPage(offset = 0) } returns Result.success(firstPage)
        // 다음 페이지 요청에 대한 mock
        coEvery { repository.getPokemonPage(offset = 20) } returns Result.success(secondPage)

        // When
        viewModel.loadFirstPage()  // 첫 페이지 로드
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.loadNextPage()   // 다음 페이지 로드
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Success)
        coVerify(exactly = 1) { repository.getPokemonPage(offset = 0) }
        coVerify(exactly = 1) { repository.getPokemonPage(offset = 20) }
    }

    @Test
    fun `새로고침 요청시 offset이 0으로 초기화되어야 한다`() = runTest {
        // Given
        val firstPage = mockk<PokemonPage> {
            every { nextUrl } returns "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"
            every { nextOffset } returns 20
        }
        val secondPage = mockk<PokemonPage> {
            every { nextUrl } returns "https://pokeapi.co/api/v2/pokemon?offset=40&limit=20"
            every { nextOffset } returns 40
        }
        val refreshedPage = mockk<PokemonPage> {
            every { nextUrl } returns "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"
            every { nextOffset } returns 20
        }

        // 첫 페이지와 두 번째 페이지 요청에 대한 mock
        coEvery { repository.getPokemonPage(offset = 0) } returns Result.success(firstPage)
        coEvery { repository.getPokemonPage(offset = 20) } returns Result.success(secondPage)

        // 새로고침 요청에 대한 mock
        coEvery { repository.getPokemonPage(offset = 0) } returns Result.success(refreshedPage)

        // When
        viewModel.loadFirstPage()  // 첫 페이지 로드
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.loadNextPage()   // 다음 페이지 로드
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.refresh()        // 새로고침
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Success)
        coVerify(exactly = 2) { repository.getPokemonPage(offset = 0) }  // 초기 로드와 새로고침
        coVerify(exactly = 1) { repository.getPokemonPage(offset = 20) }
    }

    @Test
    fun `새로고침 시작시 Loading 상태로 변경되어야 한다`() = runTest {
        // Given
        coEvery { repository.getPokemonPage(any(), any()) } coAnswers {
            delay(1000)
            Result.success(mockk())
        }

        // When
        viewModel.refresh()
        testDispatcher.scheduler.advanceTimeBy(100)

        // Then
        assertEquals(PokemonListUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `새로고침 실패시 Error 상태로 변경되어야 한다`() = runTest {
        // Given
        coEvery { repository.getPokemonPage(offset = 0) } returns Result.failure(IOException("Network error"))

        // When
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonListUiState.Error)
        coVerify(exactly = 1) { repository.getPokemonPage(offset = 0) }
    }
}