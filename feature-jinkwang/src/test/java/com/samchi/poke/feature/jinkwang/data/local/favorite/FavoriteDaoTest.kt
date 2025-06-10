package com.samchi.poke.feature.jinkwang.data.local.favorite

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.samchi.poke.feature.jinkwang.data.PokemonDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.jvm.Throws

@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {
    private lateinit var db: PokemonDatabase // 또는 해당하는 Database 클래스명
    private lateinit var dao: FavoriteDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .build()

        dao = db.favoriteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `FavoriteEntity가 DB에 삽입된다`() = runTest {
        val entity = FavoriteEntity(
            name = "피카츄"
        )
        dao.upsert(entity)

        val data = dao.getFavorites().first()

        assertEquals(1, data.size)
        assertEquals("피카츄", data[0].name)
    }

    @Test
    fun `여러 개의 FavoriteEntity가 삽입된다`() = runTest {
        val entities = listOf("피카츄", "갸라도스", "이상해씨")

        entities.forEach { name ->
            dao.upsert(FavoriteEntity(name = name))
        }

        val data = dao.getFavorites().first()

        assertEquals(3, data.size)
        assertTrue(data.any { it.name == "피카츄" })
        assertTrue(data.any { it.name == "갸라도스" })
        assertTrue(data.any { it.name == "이상해씨" })
    }

    @Test
    fun `name으로 특정 entity를 삭제한다`() = runTest {
        // Given
        val entities = listOf("피카츄", "갸라도스", "이상해씨")
        entities.forEach { name ->
            dao.upsert(FavoriteEntity(name = name))
        }

        // When
        dao.delete("갸라도스")

        // Then
        val data = dao.getFavorites().first()
        assertEquals(2, data.size)
        assertTrue(data.any { it.name == "피카츄" })
        assertTrue(data.any { it.name == "이상해씨" })
        assertFalse(data.any { it.name == "갸라도스" })
    }

    @Test
    fun `존재하지 않는 name으로 delete 시 에러가 발생하지 않는다`() = runTest {
        // Given - 빈 DB

        // When & Then - 예외가 발생하지 않아야 함
        dao.delete("존재하지않는이름")

        val data = dao.getFavorites().first()
        assertEquals(0, data.size)
    }

    @Test
    fun `DB에 데이터가 없으면 빈 리스트를 반환한다`() = runTest {
        val data = dao.getFavorites().first()

        assertEquals(0, data.size)
        assertTrue(data.isEmpty())
    }

    @Test
    fun `getFavorites는 Flow를 반환하여 실시간 데이터 변경을 감지한다`() = runTest {
        // 초기 상태 확인
        val initialData = dao.getFavorites().first()
        assertEquals(0, initialData.size)

        // 첫 번째 데이터 추가
        dao.upsert(FavoriteEntity(name = "피카츄"))
        val afterFirstInsert = dao.getFavorites().first()
        assertEquals(1, afterFirstInsert.size)
        assertEquals("피카츄", afterFirstInsert[0].name)

        // 두 번째 데이터 추가
        dao.upsert(FavoriteEntity(name = "갸라도스"))
        val afterSecondInsert = dao.getFavorites().first()
        assertEquals(2, afterSecondInsert.size)
        assertTrue(afterSecondInsert.any { it.name == "피카츄" })
        assertTrue(afterSecondInsert.any { it.name == "갸라도스" })
    }

    @Test
    fun `모든 데이터를 삭제한다`() = runTest {
        // Given
        val entities = listOf("피카츄", "갸라도스", "이상해씨")
        entities.forEach { name ->
            dao.upsert(FavoriteEntity(name = name))
        }

        // When
        entities.forEach { name ->
            dao.delete(name)
        }

        // Then
        val data = dao.getFavorites().first()
        assertEquals(0, data.size)
        assertTrue(data.isEmpty())
    }
}