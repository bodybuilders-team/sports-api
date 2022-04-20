package pt.isel.ls.unit.postgres

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.tableAsserter
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsPostgresDBTests : AppPostgresDBTests() {
// createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
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

// getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        val sport = db.sports.getSport(conn, 1)
        assertEquals(Sport(1, "Soccer", 1, "Kick a ball to score a goal"), sport)
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppException> {
                db.sports.getSport(conn, 0)
            }
        }

// getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`(): Unit = db.execute { conn ->
        val sports = listOf(
            Sport(1, "Soccer", 1, "Kick a ball to score a goal"),
            Sport(2, "Powerlifting", 2, "Get big"),
            Sport(3, "Basketball", 3, "Shoot a ball through a hoop")
        )

        assertEquals(sports, db.sports.getAllSports(conn))
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.sports.getAllSports(conn))
        }
    }

// getSportActivities

    @Test
    fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->
        val activities = db.activities.getSportActivities(conn, 1)
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }
}
