package pt.isel.ls.sports.unit.services.sections.activities

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.InvalidArgumentException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.AuthorizationException
import pt.isel.ls.sports.unit.services.AbstractServicesTests
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActivitiesServicesTests : AbstractServicesTests() {

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 0.1, uid)

        val aid = services.activities.createNewActivity(
            token,
            "2022-11-05".toLocalDate(),
            "14:59:27.903".toDuration(),
            sid,
            rid
        )

        assertEquals(
            Activity(aid, "2022-11-05".toLocalDate(), "14:59:27.903".toDuration(), uid, sid, rid),
            db.activities.getActivity(conn, aid)
        )
    }

    @Test
    fun `createNewActivity throws InvalidArgumentException if sid is not positive`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 0.1, uid)

        assertFailsWith<InvalidArgumentException> {
            services.activities.createNewActivity(
                token,
                "2022-11-05".toLocalDate(),
                "14:59:27.903".toDuration(),
                -5,
                rid
            )
        }
    }

    @Test
    fun `createNewActivity throws InvalidArgumentException if rid is not positive`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        assertFailsWith<InvalidArgumentException> {
            services.activities.createNewActivity(
                token,
                "2022-11-05".toLocalDate(),
                "14:59:27.903".toDuration(),
                sid,
                -5
            )
        }
    }

    @Test
    fun `createNewActivity throws AuthenticationException if a user with the token doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")
            val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 0.1, uid)

            val token = "Lalala"

            assertFailsWith<AuthenticationException> {
                services.activities.createNewActivity(
                    token,
                    "2022-11-05".toLocalDate(),
                    "14:59:27.903".toDuration(),
                    sid,
                    rid
                )
            }
        }

    @Test
    fun `createNewActivity throws NotFoundException if there's no sport with the sid`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 0.1, uid)

            assertFailsWith<NotFoundException> {
                services.activities.createNewActivity(
                    token,
                    "2022-11-05".toLocalDate(),
                    "14:59:27.903".toDuration(),
                    1,
                    rid
                )
            }
        }

    @Test
    fun `createNewActivity throws NotFoundException if there's no route with the rid`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

            assertFailsWith<NotFoundException> {
                services.activities.createNewActivity(
                    token,
                    "2022-11-05".toLocalDate(),
                    "14:59:27.903".toDuration(),
                    sid,
                    1
                )
            }
        }

    // getActivity

    @Test
    fun `getActivity returns the activity object`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val aid =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertEquals(
            Activity(aid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid, 1, 1),
            services.activities.getActivity(1)
        )
    }

    @Test
    fun `getActivity throws InvalidArgumentException if aid is not positive`() {

        assertFailsWith<InvalidArgumentException> {
            services.activities.getActivity(-5)
        }
    }

    @Test
    fun `getActivity throws NotFoundException if there's no activity with the aid`() {

        assertFailsWith<NotFoundException> {
            services.activities.getActivity(1)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val aid =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

        services.activities.deleteActivity(token, aid)

        assertFailsWith<NotFoundException> {
            db.activities.getActivity(conn, aid)
        }
    }

    @Test
    fun `deleteActivity throws InvalidArgumentException if aid is not positive`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.deleteActivity(token, -5)
        }
    }

    @Test
    fun `deleteActivity throws AuthenticationException if a user with the token doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val aid =
                db.activities.createNewActivity(
                    conn,
                    uid,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            val token = "Lalala"

            assertFailsWith<AuthenticationException> {
                services.activities.deleteActivity(token, aid)
            }
        }

    @Test
    fun `deleteActivity throws NotFoundException if there's no activity with the aid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<NotFoundException> {
            services.activities.deleteActivity(token, 1)
        }
    }

    @Test
    fun `deleteActivity throws AuthorizationException if the user is not the owner of the activity`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val aid =
                db.activities.createNewActivity(
                    conn,
                    2,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            assertFailsWith<AuthorizationException> {
                services.activities.deleteActivity(token, aid)
            }
        }

    // deleteActivities

    @Test
    fun `deleteActivities deletes a set of activities successfully`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val aid1 =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)
        val aid2 =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)
        val aid3 =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

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

    @Test
    fun `deleteActivities throws InvalidArgumentException if the set of identifiers is empty`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<InvalidArgumentException> {
                services.activities.deleteActivities(token, emptySet())
            }
        }

    @Test
    fun `deleteActivities throws InvalidArgumentException if the set of identifiers contains negative values`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            val aid1 =
                db.activities.createNewActivity(
                    conn,
                    uid,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            assertFailsWith<InvalidArgumentException> {
                services.activities.deleteActivities(token, setOf(aid1, -3))
            }
        }

    @Test
    fun `deleteActivities throws AuthenticationException if a user with the token doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = "Lalala"

            val aid1 =
                db.activities.createNewActivity(
                    conn,
                    uid,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            assertFailsWith<AuthenticationException> {
                services.activities.deleteActivities(token, setOf(aid1))
            }
        }

    @Test
    fun `deleteActivities throws NotFoundException if an identifier doesn't match any activity`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<NotFoundException> {
                services.activities.deleteActivities(token, setOf(2))
            }
        }

    @Test
    fun `deleteActivities throws AuthorizationException if the user isn't the owner of some activity`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            val aid1 =
                db.activities.createNewActivity(
                    conn,
                    2,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            assertFailsWith<AuthorizationException> {
                services.activities.deleteActivities(token, setOf(aid1))
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

    @Test
    fun `searchActivities throws InvalidArgumentException if sid is not positive`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchActivities(
                sid = -5,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                skip = 0,
                limit = 10
            )
        }
    }

    @Test
    fun `searchActivities throws InvalidArgumentException if rid is not positive`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = -5,
                skip = 0,
                limit = 10
            )
        }
    }

    @Test
    fun `searchActivities throws InvalidArgumentException if skip is invalid`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                skip = -4,
                limit = 10
            )
        }
    }

    @Test
    fun `searchActivities throws InvalidArgumentException if limit is invalid`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                skip = 0,
                limit = -4
            )
        }
    }

    @Test
    fun `searchActivities throws InvalidArgumentException if orderBy is invalid`(): Unit = db.execute { conn ->
        db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, 1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchActivities(
                sid = 1,
                "up",
                "2022-11-20".toLocalDate(),
                rid = 1,
                skip = 0,
                limit = 10
            )
        }
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

    @Test
    fun `searchUsersByActivity throws InvalidArgumentException if sid is not positive`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchUsersByActivity(
                sid = -5,
                rid = 1,
                skip = 0,
                limit = 10
            )
        }
    }

    @Test
    fun `searchUsersByActivity throws InvalidArgumentException if rid is not positive`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchUsersByActivity(
                sid = 1,
                rid = -5,
                skip = 0,
                limit = 10
            )
        }
    }

    @Test
    fun `searchUsersByActivity throws InvalidArgumentException if skip is invalid`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchUsersByActivity(
                sid = 1,
                rid = 1,
                skip = -5,
                limit = 10
            )
        }
    }

    @Test
    fun `searchUsersByActivity throws InvalidArgumentException if limit is invalid`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        db.sports.createNewSport(conn, 1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(conn, uid1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid2, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)
        db.activities.createNewActivity(conn, uid3, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1)

        assertFailsWith<InvalidArgumentException> {
            services.activities.searchUsersByActivity(
                sid = 1,
                rid = 1,
                skip = 0,
                limit = -5
            )
        }
    }
}
