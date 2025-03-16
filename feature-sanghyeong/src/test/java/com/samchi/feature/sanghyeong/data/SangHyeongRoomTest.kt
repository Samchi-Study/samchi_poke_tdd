package com.samchi.feature.sanghyeong.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.samchi.feature.sanghyeong.data.db.SangHyeongDao
import com.samchi.feature.sanghyeong.data.db.SangHyeongDatabase
import com.samchi.feature.sanghyeong.data.db.entity.SangHyeongPokemonEntity
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class SangHyeongRoomTest {

    private lateinit var database: SangHyeongDatabase
    private lateinit var dao: SangHyeongDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SangHyeongDatabase::class.java).build()
        dao = database.sangHyeongDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun `피카츄 entity를 database에 추가하기`() {
        runTest {
            val pikachuEntity = SangHyeongPokemonEntity(
                id = 1,
                name = "Pikachu",
                url = "",
                isFavorite = true,
            )
            dao.insertPokemon(entity = pikachuEntity)

            val pokemonList = dao.getPokemonList()
            assertEquals(pokemonList.size, 1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun `피카츄 entity를 database에서 삭제하기`() {
        runTest {
            val pikachuEntity = SangHyeongPokemonEntity(
                id = 1,
                name = "Pikachu",
                url = "",
                isFavorite = true,
            )
            // 피카츄 Entity 추가
            dao.insertPokemon(entity = pikachuEntity)

            assertEquals(dao.getPokemonList().size, 1)
            assertEquals(dao.getPokemon(id = 1).name, pikachuEntity.name)

            // 피카츄 Entity 삭제
            dao.deletePokemon(entity = pikachuEntity)

            assertEquals(dao.getPokemonList().size, 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun `id로 database에서 특정 entity 삭제하기`() {
        runTest {
            val pikachuEntity = SangHyeongPokemonEntity(
                id = 1,
                name = "Pikachu",
                url = "",
                isFavorite = true,
            )
            // 피카츄 Entity 추가
            dao.insertPokemon(entity = pikachuEntity)

            assertEquals(dao.getPokemonList().size, 1)
            assertEquals(dao.getPokemon(id = 1).name, pikachuEntity.name)

            // 피카츄 Entity 삭제
            dao.deletePokemonById(id = pikachuEntity.id)

            assertEquals(dao.getPokemonList().size, 0)
        }
    }
}