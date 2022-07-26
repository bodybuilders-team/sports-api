package pt.isel.ls.sports.unit.database.sections.activities

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.database.AppMemoryDBTests
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActivitiesMemoryDBTests : AppMemoryDBTests(), ActivitiesDBTests {

    // createNewActivity

    @Test
    override fun `createNewActivity creates activity correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val aid =
            db.activities.createNewActivity(
                conn,
                1,
                "2022-11-05".toLocalDate(),
                "14:56:27.903".toDuration(),
                1,
                1
            )
        assertEquals(
            Activity(aid, "2022-11-05".toLocalDate(), "14:56:27.903".toDuration(), 1, 1, 1),
            source.activities[1]
        )
    }

    @Test
    override fun `createNewActivity returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val aid1 =
            db.activities.createNewActivity(conn, 1, "2022-12-05".toLocalDate(), "14:16:27.903".toDuration(), 1, 1)

        val aid2 =
            db.activities.createNewActivity(conn, 1, "2022-01-05".toLocalDate(), "14:51:27.903".toDuration(), 1, 1)

        val aid3 =
            db.activities.createNewActivity(conn, 1, "2022-07-05".toLocalDate(), "14:30:27.903".toDuration(), 1, 1)

        assertEquals(1, aid1)
        assertEquals(2, aid2)
        assertEquals(3, aid3)
    }

    // updateActivity

    @Test
    override fun `updateActivity updates an activity correctly`(): Unit = db.execute { conn ->
        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[1] = activity1

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
        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[1] = activity1

        val newDate = "2022-04-20".toLocalDate()
        val newDuration = "20:00:00.000".toDuration()
        val newSid = 1
        val newRid = 1
        assertTrue(db.activities.updateActivity(conn, 1, newDate, newDuration, newSid, newRid))
    }

    @Test
    override fun `updateActivity returns false if an activity was not modified`(): Unit = db.execute { conn ->
        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[1] = activity1

        assertFalse(
            db.activities.updateActivity(
                conn,
                1,
                activity1.date,
                activity1.duration,
                activity1.sid,
                activity1.rid
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
            val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
            source.activities[1] = activity1

            assertFailsWith<InvalidArgumentException> {
                db.activities.updateActivity(conn, 1, null, null, null, null)
            }
        }

    // getActivity

    @Test
    override fun `getActivity returns the activity object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val activity = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity

        assertEquals(activity, db.activities.getActivity(conn, 1))
    }

    @Test
    override fun `getActivity throws NotFoundException if there's no activity with the sid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.activities.getActivity(conn, 1)
            }
        }

    // deleteActivity

    @Test
    override fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        db.activities.deleteActivity(conn, 1)

        assertEquals(null, source.activities[1])
    }

    @Test
    override fun `deleteActivity throws NotFoundException if there's no activity with the aid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.activities.deleteActivity(conn, 1)
            }
        }

    // getSportActivities

    @Test
    override fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity1, activity2, activity3),
            db.activities.getSportActivities(conn, 1, 0, 10).activities
        )
    }

    @Test
    override fun `getSportActivities with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity2, activity3),
            db.activities.getSportActivities(conn, 1, 1, 10).activities
        )
    }

    @Test
    override fun `getSportActivities with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity1, activity2),
            db.activities.getSportActivities(conn, 1, 0, 2).activities
        )
    }

    // getUserActivities

    @Test
    override fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity1, activity2, activity3),
            db.activities.getUserActivities(conn, 1, 0, 10).activities
        )
    }

    @Test
    override fun `getUserActivities with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity2, activity3),
            db.activities.getUserActivities(conn, 1, 1, 10).activities
        )
    }

    @Test
    override fun `getUserActivities with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        assertEquals(
            listOf(activity1, activity2),
            db.activities.getUserActivities(conn, 1, 0, 2).activities
        )
    }

    // searchActivities

    @Test
    override fun `searchActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = 1,
            0,
            10
        ).activities

        assertEquals(
            listOf(activity1, activity2, activity3),
            activities
        )
    }

    @Test
    override fun `searchActivities with descending order returns the activities list`(): Unit = db.execute { conn ->
        val activity1 = Activity(
            id = 3,
            date = "2022-11-21".toLocalDate(),
            duration = "20:23:55.263".toDuration(),
            uid = 3,
            sid = 2,
            rid = 1
        )

        val activity2 = Activity(
            id = 2,
            date = "2022-11-21".toLocalDate(),
            duration = "10:10:10.100".toDuration(),
            uid = 2,
            sid = 2,
            rid = 1
        )

        source.activities[3] = activity1
        source.activities[2] = activity2

        val activities =
            db.activities.searchActivities(
                conn, sid = 2, SortOrder.DESCENDING, "2022-11-21".toLocalDate(), rid = 1,
                0, 10
            ).activities

        val mockActivities = listOf(activity1, activity2)
        assertEquals(mockActivities, activities)
    }

    @Test
    override fun `searchActivities with ascending order returns the activities list`(): Unit = db.execute { conn ->
        val activity1 = Activity(
            id = 3,
            date = "2022-11-21".toLocalDate(),
            duration = "20:23:55.263".toDuration(),
            uid = 3,
            sid = 2,
            rid = 1
        )

        val activity2 = Activity(
            id = 2,
            date = "2022-11-21".toLocalDate(),
            duration = "10:10:10.100".toDuration(),
            uid = 2,
            sid = 2,
            rid = 1
        )

        source.activities[3] = activity1
        source.activities[2] = activity2

        val activities =
            db.activities.searchActivities(
                conn, sid = 2, SortOrder.ASCENDING, "2022-11-21".toLocalDate(), rid = 1,
                0, 10
            ).activities

        val mockActivities = listOf(activity2, activity1)
        assertEquals(mockActivities, activities)
    }

    @Test
    override fun `searchActivities with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = 1,
            1,
            10
        ).activities

        assertEquals(
            listOf(activity2, activity3),
            activities
        )
    }

    @Test
    override fun `searchActivities with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        val activity1 = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity2 = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        val activity3 = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity1
        source.activities[2] = activity2
        source.activities[3] = activity3

        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = 1,
            0,
            2
        ).activities

        assertEquals(
            listOf(activity1, activity2),
            activities
        )
    }

    // searchUsersByActivity

    @Test
    override fun `searchUsersByActivity returns a list of users`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.users[2] = User(2, "André Jesus", "andrejesus@mail.com", "H42xS")
        source.users[3] = User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[2] = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 2, 1, 1)
        source.activities[3] = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 3, 1, 1)

        val users = db.activities.searchUsersByActivity(
            conn,
            sid = 1,
            rid = 1,
            skip = 0,
            limit = 10
        ).activitiesUsers.map { it.user }

        assertEquals(
            listOf(
                User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS"),
                User(2, "André Jesus", "andrejesus@mail.com", "H42xS"),
                User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS"),
            ),
            users
        )
    }

    @Test
    override fun `searchUsersByActivity with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.users[2] = User(2, "André Jesus", "andrejesus@mail.com", "H42xS")
        source.users[3] = User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[2] = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 2, 1, 1)
        source.activities[3] = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 3, 1, 1)

        val users = db.activities.searchUsersByActivity(
            conn,
            sid = 1,
            rid = 1,
            skip = 1,
            limit = 10
        ).activitiesUsers.map { it.user }

        assertEquals(
            listOf(
                User(2, "André Jesus", "andrejesus@mail.com", "H42xS"),
                User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS"),
            ),
            users
        )
    }

    @Test
    override fun `searchUsersByActivity with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.users[2] = User(2, "André Jesus", "andrejesus@mail.com", "H42xS")
        source.users[3] = User(3, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)
        source.activities[2] = Activity(2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 2, 1, 1)
        source.activities[3] = Activity(3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 3, 1, 1)

        val users = db.activities.searchUsersByActivity(
            conn,
            sid = 1,
            rid = 1,
            skip = 0,
            limit = 2
        ).activitiesUsers.map { it.user }

        assertEquals(
            listOf(
                User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS"),
                User(2, "André Jesus", "andrejesus@mail.com", "H42xS")
            ),
            users
        )
    }

    // hasActivity

    @Test
    override fun `hasActivity returns true if the activity exists`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        assertTrue(db.activities.hasActivity(conn, 1))
    }

    @Test
    override fun `hasActivity returns false if the activity does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.activities.hasActivity(conn, 1))
    }
}
