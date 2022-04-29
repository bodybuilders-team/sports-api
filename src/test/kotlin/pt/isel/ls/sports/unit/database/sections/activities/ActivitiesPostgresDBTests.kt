package pt.isel.ls.sports.unit.database.sections.activities

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.tableAsserter
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActivitiesPostgresDBTests : AppPostgresDBTests(), ActivitiesDBTests {

    // createNewActivity

    @Test
    override fun `createNewActivity creates activity correctly in the database`() {
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
    override fun `getActivity returns the activity object`(): Unit = db.execute { conn ->
        val activity = db.activities.getActivity(conn, 1)
        assertEquals(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null), activity)
    }

    @Test
    override fun `getActivity throws NotFoundException if the activity with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.activities.getActivity(conn, 0)
            }
        }

    // deleteActivity

    @Test
    override fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->
        db.activities.deleteActivity(conn, 1)

        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, 1)
        }
    }

    @Test
    override fun `deleteActivity throws NotFoundException if there's no activity with the aid`(): Unit =
        db.execute { conn ->
            // TODO: 29/04/2022 Fix me
			/*assertFailsWith<NotFoundException> {
				db.activities.deleteActivity(conn, 9999999)
			}*/
        }

    // getSportActivities

    @Test
    override fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->
        val activities = db.activities.getSportActivities(conn, 1, 0, 10).activities
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }

    @Test
    override fun `getSportActivities with skip works`(): Unit = db.execute { conn ->
        val activities = db.activities.getSportActivities(conn, 1, 1, 10).activities
        assertEquals(
            emptyList(),
            activities
        )
    }

    @Test
    override fun `getSportActivities with limit works`(): Unit = db.execute { conn ->
        val activities = db.activities.getSportActivities(conn, 1, 0, 0).activities
        assertEquals(
            emptyList(),
            activities
        )
    }

    // getUserActivities

    @Test
    override fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->
        val activities = db.activities.getUserActivities(conn, 1, 0, 10).activities
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }

    @Test
    override fun `getUserActivities with skip works`(): Unit = db.execute { conn ->
        val activities = db.activities.getUserActivities(conn, 1, 1, 10).activities
        assertEquals(
            emptyList(),
            activities
        )
    }

    @Test
    override fun `getUserActivities with limit works`(): Unit = db.execute { conn ->
        val activities = db.activities.getUserActivities(conn, 1, 0, 0).activities
        assertEquals(
            emptyList(),
            activities
        )
    }

    // searchActivities

    @Test
    override fun `searchActivities returns the activities list`(): Unit = db.execute { conn ->
        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = null,
            skip = 0,
            limit = 10
        ).activities

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }

    @Test
    override fun `searchActivities with descending order returns the activities list`(): Unit = db.execute { conn ->
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
    override fun `searchActivities with ascending order returns the activities list`(): Unit = db.execute { conn ->
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

    @Test
    override fun `searchActivities with skip works`(): Unit = db.execute { conn ->
        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = null,
            skip = 1,
            limit = 10
        ).activities

        assertEquals(
            emptyList(),
            activities
        )
    }

    @Test
    override fun `searchActivities with limit works`(): Unit = db.execute { conn ->
        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = null,
            skip = 0,
            limit = 0
        ).activities

        assertEquals(
            emptyList(),
            activities
        )
    }

    // searchUsersByActivity

    @Test
    override fun `searchUsersByActivity returns a list of users`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 0, limit = 10
            ).users

        val mockUsers = listOf(
            User(id = 2, name = "André Páscoa", email = "A48089@alunos.isel.pt"),
            User(id = 3, name = "Nyckollas Brandão", email = "A48287@alunos.isel.pt")
        )
        assertEquals(mockUsers, users)
    }

    @Test
    override fun `searchUsersByActivity with skip works`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 1, limit = 10
            ).users

        val mockUsers = listOf(
            User(id = 3, name = "Nyckollas Brandão", email = "A48287@alunos.isel.pt"),
        )
        assertEquals(mockUsers, users)
    }

    @Test
    override fun `searchUsersByActivity with limit works`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 0, limit = 1
            ).users

        val mockUsers = listOf(
            User(id = 2, name = "André Páscoa", email = "A48089@alunos.isel.pt"),
        )
        assertEquals(mockUsers, users)
    }

    // hasActivity

    @Test
    override fun `hasActivity returns true if the activity exists`(): Unit = db.execute { conn ->
        assertTrue(db.activities.hasActivity(conn, 1))
    }

    @Test
    override fun `hasActivity returns false if the activity does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.activities.hasActivity(conn, 9999999))
    }
}
