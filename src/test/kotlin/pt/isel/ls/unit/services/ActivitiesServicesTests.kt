package pt.isel.ls.unit.services

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivitiesServicesTests : AppServicesTests() {

// createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val sid = db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 100, uid)

        val aid = services.activities.createNewActivity(
            token,
            "2022-11-05".toLocalDate(),
            "14:59:27.903".toDuration(),
            sid,
            rid
        )

        assertEquals(
            Activity(aid, "2022-11-05".toLocalDate(), "14:59:27.903".toDuration(), 1, 1, 1),
            db.activities.getActivity(conn, aid)
        )
    }

    @Test
    fun `createNewActivity throws InvalidArgument if sid is not positive`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<AppError.InvalidArgument> {
            services.activities.createNewActivity(
                token,
                "2022-11-05".toLocalDate(),
                "14:59:27.903".toDuration(),
                -5,
                1
            )
        }
    }

    @Test
    fun `createNewActivity throws InvalidArgument if rid is not positive`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<AppError.InvalidArgument> {
            services.activities.createNewActivity(
                token,
                "2022-11-05".toLocalDate(),
                "14:59:27.903".toDuration(),
                -5,
                1
            )
        }
    }

// getActivity

    @Test
    fun `getActivity returns the activity object`(): Unit = db.execute { conn ->

        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertEquals(
            Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1),
            services.activities.getActivity(1)
        )
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {

        assertFailsWith<AppError> {
            services.activities.getActivity(1)
        }
    }

// deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->

        val mockId = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)
        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

        services.activities.deleteActivity(token, 1)

        assertFailsWith<AppError> {
            db.activities.getActivity(conn, 1)
        }
    }

// getActivities

    @Test
    fun `getActivities returns the activities list`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        val activities =
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = null
            )

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }
}
