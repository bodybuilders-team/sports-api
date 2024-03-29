package pt.isel.ls.sports.unit.database.sections.activities
/*
import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
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

    @Test
    override fun `createNewActivity returns correct identifier`(): Unit = db.execute { conn ->

        val aid4 =
            db.activities.createNewActivity(conn, 1, "2022-12-05".toLocalDate(), "14:16:27.903".toDuration(), 1, 1)

        val aid5 =
            db.activities.createNewActivity(conn, 1, "2022-01-05".toLocalDate(), "14:51:27.903".toDuration(), 1, 1)

        val aid6 =
            db.activities.createNewActivity(conn, 1, "2022-07-05".toLocalDate(), "14:30:27.903".toDuration(), 1, 1)

        assertEquals(4, aid4)
        assertEquals(5, aid5)
        assertEquals(6, aid6)
    }

    // updateActivity

    @Test
    override fun `updateActivity updates an activity correctly`(): Unit = db.execute { conn ->
        val newDate = "2022-04-20".toLocalDate()
        val newDuration = "20:00:00.000".toDuration()
        val newSid = 1
        val newRid = 1
        db.activities.updateActivity(conn, 1, newDate, newDuration, newSid, newRid)

        val updatedActivity = db.activities.getActivity(conn, 1)

        assertEquals(newDate, updatedActivity.date)
        assertEquals(newDuration, updatedActivity.duration)
        assertEquals(newSid, updatedActivity.sid)
        assertEquals(newRid, updatedActivity.rid)
    }

    @Test
    override fun `updateActivity returns true if an activity was modified`(): Unit = db.execute { conn ->
        val newDate = "2022-04-20".toLocalDate()
        val newDuration = "20:00:00.000".toDuration()
        val newSid = 1
        val newRid = 1
        assertTrue(db.activities.updateActivity(conn, 1, newDate, newDuration, newSid, newRid))
    }

    @Test
    override fun `updateActivity returns false if an activity was not modified`(): Unit = db.execute { conn ->
        val activity = db.activities.getActivity(conn, 1)
        assertFalse(
            db.activities.updateActivity(
                conn,
                1,
                activity.date,
                activity.duration,
                activity.sid,
                activity.rid
            )
        )
    }

    @Test
    override fun `updateActivity throws NotFoundException if there's no activity with the aid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.activities.updateActivity(conn, 9999, "2022-04-20".toLocalDate(), "20:00:00.000".toDuration(), 1, 1)
            }
        }

    @Test
    override fun `throws InvalidArgumentException if date, duration, sid and rid are both null`(): Unit =
        db.execute { conn ->
            assertFailsWith<InvalidArgumentException> {
                db.activities.updateActivity(conn, 1, null, null, null, null)
            }
        }

    // getActivity

    @Test
    override fun `getActivity returns the activity object`(): Unit = db.execute { conn ->
        val activity = db.activities.getActivity(conn, 1)
        assertEquals(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null), activity)
    }

    @Test
    override fun `getActivity throws NotFoundException if there's no activity with the sid`(): Unit =
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
            assertFailsWith<NotFoundException> {
                db.activities.deleteActivity(conn, 9999999)
            }
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
            ).activitiesUsers.map { it.user }

        val mockUsers = listOf(
            User(
                id = 2,
                name = "André Páscoa",
                email = "A48089@alunos.isel.pt",
                password = "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            User(
                id = 3,
                name = "Nyckollas Brandão",
                email = "A48287@alunos.isel.pt",
                password = "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
        )
        assertEquals(mockUsers, users)
    }

    @Test
    override fun `searchUsersByActivity with skip works`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 1, limit = 10
            ).activitiesUsers.map { it.user }

        val mockUsers = listOf(
            User(
                id = 3,
                name = "Nyckollas Brandão",
                email = "A48287@alunos.isel.pt",
                password = "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
        )
        assertEquals(mockUsers, users)
    }

    @Test
    override fun `searchUsersByActivity with limit works`(): Unit = db.execute { conn ->
        val users =
            db.activities.searchUsersByActivity(
                conn, sid = 2, rid = 1,
                skip = 0, limit = 1
            ).activitiesUsers.map { it.user }

        val mockUsers = listOf(
            User(
                id = 2,
                name = "André Páscoa",
                email = "A48089@alunos.isel.pt",
                password = "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
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
*/
