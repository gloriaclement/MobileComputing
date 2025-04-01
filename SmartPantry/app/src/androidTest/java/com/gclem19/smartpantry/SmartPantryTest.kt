package com.gclem19.smartpantry

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gclem19.smartpantry.data.SmartPantryDB
import com.gclem19.smartpantry.data.SmartPantryDao
//import com.gclem19.smartpantry.data.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SmartPantryTest {
    private lateinit var db: SmartPantryDB
    private lateinit var smartPantryDao: SmartPantryDao

    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Before

    fun createDb() {
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(appContext, SmartPantryDB::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        smartPantryDao = db.smartPantryDao()
    }

    @After

    fun closeDb() {
        db.close()
    }
    @Test
    fun useAppContext() {
        assertEquals("com.gclem19.smartpantry", appContext.packageName)


    fun insertAndGetUser(): Unit = runBlocking {
        val user = User(username = "Tony", score = 100, duration = 50, date = 10000L)
        smartPantryDao.insert(user)
        val fetchedUser = smartPantryDao.getUserById(1)
        assertEquals("Tony", fetchedUser?.username)
    }

    }
}






