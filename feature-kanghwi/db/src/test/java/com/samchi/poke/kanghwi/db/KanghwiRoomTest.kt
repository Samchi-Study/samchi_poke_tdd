package com.samchi.poke.kanghwi.db

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.samchi.poke.kanghwi.db.entity.FavoritePokemonEntity
import com.samchi.poke.kanghwi.db.entity.PokemonEntity
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
            previous = null,
            next = null,
            isFavorite = false
        )

        dao.insertPokemon(entity)

        val data = dao.getPokemon(entity.id)

        assertEquals(1, data?.id)
        assertEquals("피카츄", data?.name)
    }

    @Test
    fun `upsert는 기존에 데이터가 없으면 insert한다`() = runTest {
        val entity = PokemonEntity(
            name = "피카츄",
            url = "test",
            previous = null,
            next = null,
            isFavorite = false
        )

        dao.upsertPokemon(entity)

        val data = dao.getPokemon(1)

        assertEquals(1, data?.id)
        assertEquals("피카츄", data?.name)
        assertEquals(false, data?.isFavorite)
    }

    @Test
    fun `upsert는 기존에 데이터가 있으면 update한다`() = runTest {
        val entity = PokemonEntity(
            name = "피카츄",
            url = "test",
            previous = null,
            next = null,
            isFavorite = false
        )

        dao.upsertPokemon(entity)

        dao.getPokemon(1)?.copy(name = "이상해씨", isFavorite = true)?.let {
            dao.upsertPokemon(it)
        }

        val data = dao.getPokemon(1)

        assertEquals(1, data?.id)
        assertEquals("이상해씨", data?.name)
        assertEquals(true, data?.isFavorite)
    }

    @Test
    fun `한 개 이상의 PokemonEntity List가 insert 된다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                previous = null,
                next = null,
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
    fun `DB에 저장된 값이 없다면 emptyList를 리턴한다`() = runTest {
        val list = dao.getPokemonList()

        assertEquals(0, list.size)
    }

    @Test
    fun `모든 데이터를 Replace한다`() = runTest {
        val entities1 = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            )
        )

        val entities2 = listOf(
            PokemonEntity(
                id = 1,
                name = "망나뇽",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "뮤",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "라이츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities1)
        dao.insertPokemonList(entities2)

        val list = dao.getPokemonList()

        assertEquals(3, list.size)
        assertEquals("망나뇽", list[0].name)
        assertEquals("뮤", list[1].name)
        assertEquals("라이츄", list[2].name)
    }

    @Test
    fun `찾는 entity가 없는 경우 null을 리턴한다`() = runTest {
        val entity = dao.getPokemon(22)

        assertEquals(null, entity)
    }

    @Test
    fun `PrimaryKey id 기본값이 0이고 autoGenerated가 true라면 id는 증분하게 되어 중복이 발생하지 않는다`() = runTest {
        val entity = PokemonEntity(
            name = "라이츄",
            url = "test",
            previous = null,
            next = null,
            isFavorite = false
        )

        dao.insertPokemon(entity)
        dao.insertPokemon(entity)

        val data1 = dao.getPokemon(1)
        val data2 = dao.getPokemon(2)

        assertEquals(1, data1?.id)
        assertEquals("라이츄", data1?.name)
        assertEquals(2, data2?.id)
        assertEquals("라이츄", data2?.name)
    }

    @Test
    fun `PrimaryKey id가 0이 아닌 값으로 설정 시 autoGenerated가 true더라도 중복 에러가 발생한다`() = runTest {
        val entity = PokemonEntity(
            id = 2,
            name = "라이츄",
            url = "test",
            previous = null,
            next = null,
            isFavorite = false
        )

        try {
            dao.insertPokemon(entity)
            dao.insertPokemon(entity)

        } catch (e: Exception) {
            assertThrows(Exception::class.java) {
                throw SQLiteConstraintException("UNIQUE constraint failed: KanghwiPokemon.id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)")
            }
        }
    }

    @Test
    fun `entity_isFavorite 필드를 수정한다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities)

        dao.updatePokemon(entities[1].copy(isFavorite = true))

        val entity = dao.getPokemon(entities[1].id)

        assertEquals("갸라도스", entity?.name)
        assertEquals(true, entity?.isFavorite)
    }

    @Test
    fun `entities 중 한 개의 element를 제거한다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            )
        )

        dao.insertPokemonList(entities)

        dao.deletePokemon(entities[1].name)

        val list = dao.getPokemonList()

        assertEquals(2, list.size)
        assertEquals("피카츄", list[0].name)
        assertEquals("이상해씨", list[1].name)
    }

    @Test
    fun `Delete 시 제거할 대상이 DB에 없어도 error는 발생하지 않는다`() = runTest {
        val entity = PokemonEntity(
            name = "이상해씨",
            url = "test",
            previous = null,
            next = null,
            isFavorite = false
        )

        dao.deletePokemon(entity.name)

    }

    @Test
    fun `낱개로 저장한 후 entity를 모두 가져올 때 list로 받을 수 있다`() = runTest {
        val entities = listOf(
            PokemonEntity(
                id = 1,
                name = "피카츄",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 2,
                name = "갸라도스",
                url = "test",
                previous = null,
                next = null,
                isFavorite = false
            ),
            PokemonEntity(
                id = 3,
                name = "이상해씨",
                url = "test",
                previous = null,
                next = null,
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

    @Test
    fun `좋아요한 pokemon을 db에 저장한다`() = runTest(UnconfinedTestDispatcher()) {
        val pokemon = FavoritePokemonEntity(
            id = 1,
            isFavorite = true
        )

        dao.insertFavoritePokemon(pokemon)

        val result = dao.getFavoritePokemonList().first()

        assertEquals(true, result.first().isFavorite)
    }

    @Test
    fun `좋아요한 pokemon을 db에서 제거한다`() = runTest(UnconfinedTestDispatcher()) {
        val pokemon = FavoritePokemonEntity(
            id = 1,
            isFavorite = true
        )

        dao.insertFavoritePokemon(pokemon)

        val result = dao.getFavoritePokemonList().first()

        dao.deleteFavoritePokemon(result.first())

        val result2 = dao.getFavoritePokemonList().first()

        assertEquals(0, result2.size)
    }
}
