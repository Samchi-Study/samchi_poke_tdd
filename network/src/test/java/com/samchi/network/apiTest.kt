package com.samchi.network


import com.samchi.poke.network.PokeApi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.HttpURLConnection


class apiTest {

    private lateinit var mockServer: MockWebServer

    private lateinit var httpUrl: HttpUrl

    private lateinit var pokeApi: PokeApi

    private lateinit var responsePokemonInfo: String

    @Before
    fun init() {
        mockServer = MockWebServer().also { it.start() }
        httpUrl = mockServer.url("/")

        pokeApi = Retrofit.Builder()
            .baseUrl(httpUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokeApi::class.java)

        javaClass.classLoader!!.getResourceAsStream("pokemon_info.json")
            .bufferedReader().use {
                responsePokemonInfo = it.readText()
            }
    }

    @Test
    fun `getPokemonInfo()를 성공적으로 호출한 경우`() = runTest {
        mockServer.enqueue(
            MockResponse().apply {
                setResponseCode(HttpURLConnection.HTTP_OK)
                setBody(responsePokemonInfo)
            }
        )

        val pokemonInfo = pokeApi.getPokemonList()

        assertEquals(pokemonInfo.results.size, 3)

        assertEquals(pokemonInfo.results[0].name, "ivysaur")
        assertEquals(pokemonInfo.results[1].name, "venusaur")
        assertEquals(pokemonInfo.results[2].name, "charmander")
    }
}
