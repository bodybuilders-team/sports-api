package pt.isel.ls.unit.postgres

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.tableAsserter
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivitiesPostgresDBTests : AppPostgresDBTests() {
// createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {
        val aid = db.execute { conn ->
            db.activities.createNewActivity(conn, 1, "2022-11-05".toLocalDate(), "14:59:27.903".toDuration(), 1, 1)
        }
        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM activities WHERE id = ?")
            stm.setInt(1, aid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "2022-11-05", "14:59:27.903", 1, 1, 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getDate("date").toString())
                assertEquals(mockRow[2], row.getString("duration"))
                assertEquals(mockRow[3], row.getInt("uid"))
                assertEquals(mockRow[4], row.getInt("sid"))
                assertEquals(mockRow[5], row.getInt("rid"))
            }
        }
    }

// getActivity

    @Test
    fun `getActivity returns the activity object`(): Unit = db.execute { conn ->
        val activity = db.activities.getActivity(conn, 1)
        assertEquals(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null), activity)
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.activities.getActivity(conn, 0)
            }
        }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->
        db.activities.deleteActivity(conn, 1)

        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, 1)
        }
    }

    // getActivities

    @Test
    fun `getActivities with descending order returns the activities list`(): Unit = db.execute { conn ->
        val activities =
            db.activities.searchActivities(
                conn, sid = 2, SortOrder.DESCENDING, "2022-11-21".toLocalDate(), rid = 1,
                0, 10
            ).activities

        val mockActivities = listOf(
            Activity(
                id = 3,
                date = "2022-11-21".toLocalDate(),
                duration = "20:23:55.263".toDuration(),
                uid = 3,
                sid = 2,
                rid = 1
            ),
            Activity(
                id = 2,
                date = "2022-11-21".toLocalDate(),
                duration = "10:10:10.100".toDuration(),
                uid = 2,
                sid = 2,
                rid = 1
            )
        )
        assertEquals(mockActivities, activities)
    }

    @Test
    fun `getActivities with ascending order returns the activities list`(): Unit = db.execute { conn ->
        val activities =
            db.activities.searchActivities(
                conn, sid = 2, SortOrder.ASCENDING, "2022-11-21".toLocalDate(), rid = 1,
                0, 10
            ).activities

        val mockActivities = listOf(
            Activity(
                id = 2,
                date = "2022-11-21".toLocalDate(),
                duration = "10:10:10.100".toDuration(),
                uid = 2,
                sid = 2,
                rid = 1
            ),
            Activity(
                id = 3,
                date = "2022-11-21".toLocalDate(),
                duration = "20:23:55.263".toDuration(),
                uid = 3,
                sid = 2,
                rid = 1
            )

        )
        assertEquals(mockActivities, activities)
    }

    // searchUsersByActivity

    @Test
    fun `searchUsersByActivity returns a list of users`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 0, limit = 10
            ).users

        val mockUsers = listOf(
            User(id = 2, name = "André Páscoa", email = "A48089@alunos.isel.pt"),
            User(id = 3, name = "Nyckollas Brandão", email = "A48287@alunos.isel.pt"),
        )
        assertEquals(mockUsers, users)
    }

    // TODO: 16/04/2022 Skip and limit tests
}
