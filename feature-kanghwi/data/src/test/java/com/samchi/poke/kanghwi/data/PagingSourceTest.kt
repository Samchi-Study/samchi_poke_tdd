package com.samchi.poke.kanghwi.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.samchi.poke.kanghwi.LocalDataSource
import com.samchi.poke.kanghwi.model.Pokemon
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

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private val pageSize = 2

    private val config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = true
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
                name = "피카츄",
                url = "https://pokeapi.co/api/v2/pokemon/6/"
            )
        )

        coEvery {
            pokeApi.getPokemonList(6, 0)
        } answers {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=6&limit=6",
                previous = null,
                results = mockList
            )
        }

        coEvery {
            localDataSource.getPokemonList()
        } answers {
            listOf(
                Pokemon(
                    name = "피카츄",
                    url = "test",
                    isFavorite = true
                )
            )
        }

        val source = PokemonPagingSource(pokeApi, localDataSource)

        val pager = TestPager(config = config, pagingSource = source)

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(6, result.data.size)
        assertEquals(null, result.prevKey)
        assertEquals(6, result.nextKey)
        assertEquals(true, result.data[5].isFavorite)

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
                next = "https://pokeapi.co/api/v2/pokemon?offset=6&limit=6",
                previous = null,
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
                next = "https://pokeapi.co/api/v2/pokemon?offset=8&limit=$pageSize",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=$pageSize",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/7/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/8/"
                    )
                )
            )
        } andThenAnswer {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=10&limit=$pageSize",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=6&limit=$pageSize",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/9/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/10/"
                    )
                )
            )
        } andThenAnswer {
            ResponsePokemonInfo(
                count = 1000,
                next = "https://pokeapi.co/api/v2/pokemon?offset=12&limit=$pageSize",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=8&limit=$pageSize",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/11/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/12/"
                    )
                )
            )
        }


        coEvery {
            localDataSource.getPokemonList()
        } answers {
            listOf(
                Pokemon(
                    name = "피카츄",
                    url = "test",
                    isFavorite = true
                )
            )
        }

        val source = PokemonPagingSource(pokeApi, localDataSource)

        val pager = TestPager(config = config, pagingSource = source)

        val p1 = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(6, p1.data.size)
        assertEquals(null, p1.prevKey)
        assertEquals(6, p1.nextKey)

        val p2 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(2, p2.data.size)
        assertEquals(4, p2.prevKey)
        assertEquals(8, p2.nextKey)

        val p3 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(2, p3.data.size)
        assertEquals(6, p3.prevKey)
        assertEquals(10, p3.nextKey)

        val p4 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(2, p4.data.size)
        assertEquals(8, p4.prevKey)
        assertEquals(12, p4.nextKey)

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
                next = "https://pokeapi.co/api/v2/pokemon?offset=6&limit=6",
                previous = null,
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
                next = "https://pokeapi.co/api/v2/pokemon?offset=8&limit=$pageSize",
                previous = "https://pokeapi.co/api/v2/pokemon?offset=4&limit=$pageSize",
                results = listOf(
                    ResponsePokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/7/"
                    ),
                    ResponsePokemon(
                        name = "ivysaur",
                        url = "https://pokeapi.co/api/v2/pokemon/8/"
                    )
                )
            )
        }

        coEvery {
            localDataSource.getPokemonList()
        } answers {
            listOf(
                Pokemon(
                    name = "피카츄",
                    url = "test",
                    isFavorite = true
                )
            )
        }

        val source = PokemonPagingSource(pokeApi, localDataSource)

        val pager = TestPager(config = config, pagingSource = source)

        val p1 = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(6, p1.data.size)
        assertEquals(null, p1.prevKey)
        assertEquals(6, p1.nextKey)

        val p2 = pager.append() as PagingSource.LoadResult.Page

        assertEquals(2, p2.data.size)
        assertEquals(4, p2.prevKey)
        assertEquals(8, p2.nextKey)

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

        coEvery {
            localDataSource.getPokemonList()
        } answers {
            listOf(
                Pokemon(
                    name = "피카츄",
                    url = "test",
                    isFavorite = true
                )
            )
        }

        val source = PokemonPagingSource(pokeApi, localDataSource)

        val pager = TestPager(config = config, pagingSource = source)

        val result = pager.refresh() as PagingSource.LoadResult.Error

        assertTrue(result.throwable is NoSuchElementException)
    }
}
