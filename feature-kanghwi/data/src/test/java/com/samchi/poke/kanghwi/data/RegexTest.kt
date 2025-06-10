package com.samchi.poke.kanghwi.data

import org.junit.Test
import kotlin.test.assertEquals


class RegexTest {

    @Test
    fun `offset=숫자로 된 것들이 있는지 찾는다`(){
        val regex = Regex("offset=\\d+")

        val url = "https://pokeapi.co/api/v2/pokemon?offset=21&limit=20"

        val result = regex.find(url)

        assertEquals(1,result?.groupValues?.size)
        assertEquals("offset=21",result?.value)
        assertEquals("21",result?.value?.split("=")?.get(1))
    }
}