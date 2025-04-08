package com.samchi.feature.woosung

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.samchi.feature.woosung.data.paging.WoosungPagingSource
import com.samchi.feature.woosung.data.remote.WoosungPokeRemoteDataSource
import com.samchi.feature.woosung.mapper.toPokemon
import com.samchi.poke.network.dto.ResponsePokemon
import com.samchi.poke.network.dto.ResponsePokemonInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WoosungPagingSourceTest {
    private val fakePokemon = listOf(
        ResponsePokemon(
            name = "bulbasaur1",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur2",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur3",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur4",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur5",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur6",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        ResponsePokemon(
            name = "bulbasaur7",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        )

    )

    private lateinit var woosungRemoteDataSource: WoosungPokeRemoteDataSource

    @Before

    fun setUp() {
        woosungRemoteDataSource = mockk<WoosungPokeRemoteDataSource>()


    }


    @Test
    fun `Paging data가 정상적으로 호출되는가?`() = runTest {

        coEvery { woosungRemoteDataSource.getPokemonList(any()) } returns ResponsePokemonInfo(
            count = 1,
            next = null,
            previous = null,
            results = listOf(fakePokemon[0])
        )

        val woosungPagingSource = WoosungPagingSource(
            woosungRemoteDataSource
        )

        val pager = TestPager(
            config = PagingConfig(pageSize = 10),
            pagingSource = woosungPagingSource
        )

        val result = (pager.refresh() as PagingSource.LoadResult.Page)

        Assert.assertEquals(result.data, listOf(fakePokemon[0].toPokemon()))

    }

    @Test
    fun `Paging data를 여러번 갖고울수 있는가?`() = runTest {

        coEvery { woosungRemoteDataSource.getPokemonList(any()) } returns ResponsePokemonInfo(
            count = 1,
            next = null,
            previous = null,
            results = fakePokemon
        )

        val woosungPagingSource = WoosungPagingSource(
            woosungRemoteDataSource
        )

        val pager = TestPager(
            config = PagingConfig(pageSize = 1),
            pagingSource = woosungPagingSource
        )

        val result = with(pager) {
            refresh()
            append()
            append()
        } as PagingSource.LoadResult.Page


        val actualData = fakePokemon.map { it.toPokemon() }

        Assert.assertEquals(result.data, fakePokemon.map { it.toPokemon() })

    }


    @Test
    fun `Paging data가 에러를 정상적으로 반환 하는가?`() = runTest {

        coEvery { woosungRemoteDataSource.getPokemonList(any()) } throws Exception("페이징 에러")


        val woosungPagingSource = WoosungPagingSource(
            woosungRemoteDataSource
        )

        val pager = TestPager(
            config = PagingConfig(pageSize = 1),
            pagingSource = woosungPagingSource
        )

        val result = pager.refresh()



        Assert.assertTrue(result is PagingSource.LoadResult.Error)

        val page = pager.getLastLoadedPage()

        Assert.assertNull(page)

    }
}
