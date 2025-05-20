package com.samchi.feature.woosung

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samchi.feature.woosung.data.entity.PokemonEntity
import com.samchi.feature.woosung.data.local.WoosungDao
import com.samchi.feature.woosung.data.local.WoosungDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
internal class RoomTest {

    private lateinit var db : WoosungDatabase
    private lateinit var dao : WoosungDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, WoosungDatabase::class.java).build()
        dao = db.woosungDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }



    @Test
    @Throws(Exception::class)
    fun `db에 Entity가 정상적으로 삽입되는가?`() = runTest{
        dao.insertPokemon(pokemonEntity)

       val pokemonEntity =  dao.getPokemon(id = 1)

        Assert.assertEquals(pokemonEntity, pokemonEntity)
    }


    @Test
    @Throws(Exception::class)
    fun `db에 Entity가 정상적으로 삭제되는가?`() = runTest{
        dao.insertPokemon(pokemonEntity)
        dao.deletePokemon(pokemonEntity)

        val pokemonEntity =  dao.getPokemon(id = 1)

    }
}


val pokemonEntity = PokemonEntity(
    id = 1,
    name = "bulbasaur",
    url = "https://pokeapi.co/api/v2/pokemon/1/",
    isFavorite = true
)
