package com.samchi.poke.kanghwi.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class KanghwiRoomTest {

    private lateinit var db: KanghwiDatabase
    private lateinit var dao: KanghwiDao


    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, KanghwiDatabase::class.java)
            .build()

        dao = db.kanghwiDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `한 개 entity가 db에 삽입된다`() = runTest {
        val entity = PokemonEntity(
            id = 1,
            name = "피카츄",
            url = "test",
            isFavorite = false
        )

        dao.insertPokemon(entity)

        val data = dao.getPokemon(entity.id)

        assertEquals(1, data.id)
        assertEquals("피카츄", data.name)
    }

    @Test
    fun `기존에 데이터가 없으면 insert하고 존재한다면 update한다`() = runTest {
        val entity = PokemonEntity(
            id = 1,
            name = "피카츄",
            url = "test",
            isFavorite = false
        )

        dao.insertPokemon(entity)

        val newEntity = entity.copy(name = "이상해씨", isFavorite = true)

        dao.upsertPokemon(newEntity)

        val data = dao.getPokemon(newEntity.id)

        assertEquals(1, data.id)
        assertEquals("이상해씨", data.name)
        assertEquals(true, data.isFavorite)
    }

    @Test
    fun `한 개 이상의 PokemonEntity List가 insert 된다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities)
        val list = dao.getPokemonList()

        assertEquals(3, list.size)
        assertEquals("피카츄", list[0].name)
        assertEquals(1, list[0].id)
        assertEquals("갸라도스", list[1].name)
        assertEquals(2, list[1].id)
        assertEquals("이상해씨", list[2].name)
        assertEquals(3, list[2].id)
    }

    @Test
    fun `entity_isFavorite 필드를 수정한다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities)

        dao.updatePokemon(entities[1].copy(isFavorite = true))

        val entity = dao.getPokemon(entities[1].id)

        assertEquals("갸라도스", entity.name)
        assertEquals(true, entity.isFavorite)
    }

    @Test
    fun `entities 중 한 개의 element를 제거한다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities)

        dao.deletePokemon(entities[1])

        val list = dao.getPokemonList()

        assertEquals(2, list.size)
        assertEquals("피카츄", list[0].name)
        assertEquals("이상해씨", list[1].name)
    }

    @Test
    fun `낱개로 저장한 후 entity를 모두 가져올 때 list로 받을 수 있다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                isFavorite = false
            )
        )

        entities.forEach {
            dao.insertPokemon(it)
        }

        val list = dao.getPokemonList()

        assertEquals(3, list.size)
        assertEquals("피카츄", list[0].name)
        assertEquals(1, list[0].id)
        assertEquals("갸라도스", list[1].name)
        assertEquals(2, list[1].id)
        assertEquals("이상해씨", list[2].name)
        assertEquals(3, list[2].id)
    }
}
