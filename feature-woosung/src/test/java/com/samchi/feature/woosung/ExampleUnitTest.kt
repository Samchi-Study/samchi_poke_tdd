package com.samchi.feature.woosung

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.samchi.feature.woosung.data.entity.PokemonEntity
import com.samchi.feature.woosung.data.local.WoosungDao
import com.samchi.feature.woosung.data.local.WoosungDatabase
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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
    fun writeUserAndReadInList() {
        val user: User = TestUtil.createUser(3).apply {
            setName("george")
        }

        dao.insertPokemon(PokemonEntity())
        val byName = userDao.findUsersByName("george")
        assertThat(byName.get(0), equalTo(user))
    }
}
