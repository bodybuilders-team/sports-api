package pt.isel.ls.unit.services

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.services.InvalidArgumentException
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
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 0.1, uid)

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

        assertFailsWith<InvalidArgumentException> {
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

        assertFailsWith<InvalidArgumentException> {
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

        assertFailsWith<NotFoundException> {
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

        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, 1)
        }
    }

    // deleteActivities

    @Test
    fun `deleteActivities deletes a set of activities successfully`(): Unit = db.execute { conn ->
        val mockId = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

        val aid1 =
            db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)
        val aid2 =
            db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)
        val aid3 =
            db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

        services.activities.deleteActivities(token, setOf(aid1, aid2, aid3))

        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, aid1)
        }
        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, aid2)
        }
        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, aid3)
        }
    }

    // searchActivities

    @Test
    fun `searchActivities returns the activities list`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        val activities =
            services.activities.searchActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                skip = 0,
                limit = 10
            ).activities

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // searchUsersByActivity

    @Test
    fun `searchUsersByActivity returns the list of users`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        val users =
            services.activities.searchUsersByActivity(
                sid = 1,
                rid = 1,
                skip = 0,
                limit = 10
            ).users

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
