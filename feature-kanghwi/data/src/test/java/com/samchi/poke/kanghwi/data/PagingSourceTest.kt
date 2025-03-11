package com.samchi.poke.kanghwi.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.samchi.poke.kanghwi.pagingsource.PokemonPagingSource
import com.samchi.poke.network.PokeApi
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class PagingSourceTest {

    @Rule
    @JvmField
    val injectMocks = TestRule { statement, _ ->
        MockKAnnotations.init(this, relaxUnitFun = true)
        statement
    }

    @MockK
    private lateinit var pokeApi: PokeApi

    private val pageSize = 2

    private val config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = true,
        maxSize = pageSize * 4,
        initialLoadSize = pageSize * 3
    )


    @Test
    fun `refresh()로 초기 데이터를 받는다`() = runTest {
        val mockList = listOf(
            ResponsePokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/1/"
            ),
            ResponsePokemon(
                name = "ivysaur",
                url = "https://pokeapi.co/api/v2/pokemon/2/"
            ),
            ResponsePokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/3/"
            ),
            ResponsePokemon(
                name = "ivysaur",
                url = "https://pokeapi.co/api/v2/pokemon/4/"
            ),
            ResponsePokemon(
                name = "bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/5/"
            ),
            ResponsePokemon(
                name = "ivysaur",
                url = "https://pokeapi.co/api/v2/pokemon/6/"
            )
        )

        coEvery {
            pokeApi.getPokemonList(6, 0)
        } answers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=1",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=3&limit=1",
                results = mockList
            )
        }

        val source = PokemonPagingSource(api = pokeApi)

        val pager = TestPager(config = config, pagingSource = source)

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(result.data.size, 6)
        assertEquals(result.prevKey, null)
        assertEquals(result.nextKey, 6)

        coVerify(exactly = 1) {
            pokeApi.getPokemonList(any(), any())
        }
    }


    @Test
    fun `append()로 다음 페이지를 불러온다`() = runTest {
        coEvery {
            pokeApi.getPokemonList(6, 0)
        } coAnswers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=1",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=3&limit=1",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/2/"
                    ),
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/3/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/4/"
                    ),
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/5/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/6/"
                    )
                )
            )
        }

        coEvery {
            pokeApi.getPokemonList(2, more(0))
        } answers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=1",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=3&limit=1",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/6/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/7/"
                    )
                )
            )
        }

        val source = PokemonPagingSource(api = pokeApi)

        val pager = TestPager(config = config, pagingSource = source)

        val p1 = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(p1.data.size, 6)
        assertEquals(p1.prevKey, null)
        assertEquals(p1.nextKey, 6)

        val p2 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(p2.data.size, 2)
        assertEquals(p2.prevKey, 0)
        assertEquals(p2.nextKey, 8)

        val p3 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(p3.data.size, 2)
        assertEquals(p3.prevKey, 6)
        assertEquals(p3.nextKey, 10)

        val p4 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(p4.data.size, 2)
        assertEquals(p4.prevKey, 8)
        assertEquals(p4.nextKey, 12)

        coVerify {
            pokeApi.getPokemonList(any(), any())
        }
    }

    @Test
    fun `getRefreshKey함수가 anchorPosition 기준으로 가장 가까운 페이지로 refresh되는지 확인한다`() = runTest {
        coEvery {
            pokeApi.getPokemonList(6, 0)
        } coAnswers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=1",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=3&limit=1",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/2/"
                    ),
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/3/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/4/"
                    ),
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/5/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/6/"
                    )
                )
            )
        }

        coEvery {
            pokeApi.getPokemonList(2, more(0))
        } answers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=1",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=3&limit=1",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/6/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/7/"
                    )
                )
            )
        }

        val source = PokemonPagingSource(api = pokeApi)

        val pager = TestPager(config = config, pagingSource = source)

        val p1 = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(p1.data.size, 6)
        assertEquals(p1.prevKey, null)
        assertEquals(p1.nextKey, 6)

        val p2 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(p2.data.size, 2)
        assertEquals(p2.prevKey, 0)
        assertEquals(p2.nextKey, 8)

        val key = source.getRefreshKey(pager.getPagingState(7))

        assertEquals(key, 6)
    }


    @Test
    fun `페이지 호출 시, error로 떨어진다`() = runTest {
        coEvery {
            pokeApi.getPokemonList(any(), any())
        } answers {
            throw NoSuchElementException()
        }

        val source = PokemonPagingSource(api = pokeApi)

        val pager = TestPager(config = config, pagingSource = source)

        val result = pager.refresh() as PagingSource.LoadResult.Error

        assertTrue(result.throwable is NoSuchElementException)
    }
}
