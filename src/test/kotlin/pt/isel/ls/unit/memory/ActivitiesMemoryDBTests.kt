package pt.isel.ls.unit.memory

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivitiesMemoryDBTests : AppMemoryDBTests() {
    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

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

    // getActivity

    @Test
    fun `getActivity returns the activity object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val activity = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity

        assertEquals(activity, db.activities.getActivity(conn, 1))
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppError> {
                db.activities.getActivity(conn, 1)
            }
        }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        db.activities.deleteActivity(conn, 1)

        assertEquals(null, source.activities[1])
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.getSportActivities(conn, 1)

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.getUserActivities(conn, 1)

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // searchActivities

    @Test
    fun `searchActivities returns the activities list`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.searchActivities(
            conn,
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = 1,
            0,
            10
        )

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // searchUsersByActivity

    @Test
    fun `searchUsersByActivity returns the list of users`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.users[2] = User(2, "André Jesus", "andrejesus@mail.com")
        source.users[3] = User(3, "André Páscoa", "andrepascoa@mail.com")

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
        )

        assertEquals(
            listOf(
                User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com"),
                User(2, "André Jesus", "andrejesus@mail.com"),
                User(3, "André Páscoa", "andrepascoa@mail.com"),
            ),
            users
        )
    }
}
