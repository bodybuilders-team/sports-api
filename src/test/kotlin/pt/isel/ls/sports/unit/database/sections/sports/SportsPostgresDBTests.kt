package pt.isel.ls.sports.unit.database.sections.sports

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.tableAsserter
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SportsPostgresDBTests : AppPostgresDBTests(), SportsDBTests {

    // createNewSport

    @Test
    override fun `createNewSport creates sport correctly in the database`() {
        val sid = db.execute { conn ->
            db.sports.createNewSport(conn, 1, "Badminton", "idk")
        }

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM sports WHERE id = ?")
            stm.setInt(1, sid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Badminton", "idk", 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getString("description"))
                assertEquals(mockRow[3], row.getInt("uid"))
            }
        }
    }

    @Test
    override fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->
        val uid1 = db.sports.createNewSport(conn, 1, "Powerlifting", "Get big")
        val uid2 = db.sports.createNewSport(conn, 1, "Swimming", "Be like a fish")
        val uid3 = db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        assertEquals(4, uid1)
        assertEquals(5, uid2)
        assertEquals(6, uid3)
    }

    // getSport

    @Test
    override fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        val sport = db.sports.getSport(conn, 1)
        assertEquals(Sport(1, "Soccer", 1, "Kick a ball to score a goal"), sport)
    }

    @Test
    override fun `getSport throws NotFoundException if the sport with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.sports.getSport(conn, 0)
            }
        }

    // searchSports

    @Test
    override fun `searchSports returns list of all sport objects`(): Unit = db.execute { conn ->
        val sports = listOf(
            Sport(1, "Soccer", 1, "Kick a ball to score a goal"),
            Sport(2, "Powerlifting", 2, "Get big"),
            Sport(3, "Basketball", 3, "Shoot a ball through a hoop")
        )

        assertEquals(sports, db.sports.searchSports(conn, 0, 10).sports)
    }

    @Test
    override fun `searchSports with no created sports returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.sports.searchSports(conn, 0, 10).sports)
        }
    }

    @Test
    override fun `searchSports with skip works`(): Unit = db.execute { conn ->
        val sports = listOf(
            Sport(2, "Powerlifting", 2, "Get big"),
            Sport(3, "Basketball", 3, "Shoot a ball through a hoop")
        )

        assertEquals(sports, db.sports.searchSports(conn, 1, 10).sports)
    }

    @Test
    override fun `searchSports with limit works`(): Unit = db.execute { conn ->
        val sports = listOf(
            Sport(1, "Soccer", 1, "Kick a ball to score a goal"),
            Sport(2, "Powerlifting", 2, "Get big")
        )

        assertEquals(sports, db.sports.searchSports(conn, 0, 2).sports)
    }

    // hasSport

    @Test
    override fun `hasSport returns true if the sport exists`(): Unit = db.execute { conn ->
        assertTrue(db.sports.hasSport(conn, 1))
    }

    @Test
    override fun `hasSport returns false if the sport does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.sports.hasSport(conn, 999999))
    }
}
