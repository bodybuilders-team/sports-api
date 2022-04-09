package pt.isel.ls.unit

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.AppServices
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppServicesTests {

    private var source = AppMemoryDBSource()
    private var db = AppMemoryDB(source)
    private val services = AppServices(db)

    @BeforeTest
    fun initializeDataMem() {
        source = AppMemoryDBSource()
    }

    // createNewUser

    @Test
    fun `createNewUser creates user correctly in the database`(): Unit = db.execute { conn ->
        val createUserResponse = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(
            User(createUserResponse.uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"),
            db.users.getUser(conn, createUserResponse.uid)
        )
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val createUserResponse1 = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val createUserResponse2 = services.users.createNewUser("André Jesus", "andrejesus@mail.com")
        val createUserResponse3 = services.users.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, createUserResponse1.uid)
        assertEquals(2, createUserResponse2.uid)
        assertEquals(3, createUserResponse3.uid)
    }

    @Test
    fun `createNewUser throws AppError InvalidArgument if the name is invalid (name too short)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.users.createNewUser("Ny", "nyckollasbrandao@mail.com")
        }
    }

    @Test
    fun `createNewUser throws AppError InvalidArgument if the name is invalid (name too long)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.users.createNewUser("Nyckollas Brandão".repeat(10), "nyckollasbrandao@mail.com")
        }
    }

    @Test
    fun `createNewUser throws AppError InvalidArgument if the email is invalid`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.users.createNewUser("Nyckollas Brandão", "@@@@@mail.com")
        }
    }

    @Test
    fun `createNewUser throws AppError Conflict if a user already exists with that email`() {
        services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertFailsWith<AppError.Conflict> {
            services.users.createNewUser("Nyck Brandão", "nyckollasbrandao@mail.com")
        }
    }

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val user = User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(user, services.users.getUser(uid))
    }

    @Test
    fun `getUser throws AppError InvalidArgument if the uid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.users.getUser(-5)
        }
    }

    @Test
    fun `getUser throws AppError NotFound if the user with the uid doesn't exist`() {
        assertFailsWith<AppError.NotFound> {
            services.users.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        val user1 = User(uid1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(uid2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(uid3, "André Páscoa", "andrepascoa@mail.com")

        assertEquals(listOf(user1, user2, user3), services.users.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        assertEquals(emptyList(), services.users.getAllUsers())
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(conn, rid))
    }

    @Test
    fun `createNewRoute returns correct identifier`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid0 = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)
        val rid1 = services.routes.createNewRoute(token, "Chelas", "Odivelas", 0.150)
        val rid2 = services.routes.createNewRoute(token, "Lisboa", "Chelas", 0.150)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    @Test
    fun `createNewRoute throws AppError InvalidArgument if the distance isn't valid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<AppError.InvalidArgument> {
            services.routes.createNewRoute(token, "Odivelas", "Chelas", -0.150)
        }
    }

    @Test
    fun `createNewRoute throws AppError InvalidCredentials if the token isn't valid`() {
        val token = "lalala"

        assertFailsWith<AppError.InvalidCredentials> {
            services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)
        }
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.15)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, uid), db.routes.getRoute(conn, rid))
    }

    @Test
    fun `getRoute throws AppError InvalidArgument if the rid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.routes.getRoute(-5)
        }
    }

    @Test
    fun `getRoute throws AppError NotFound if the route with the rid doesn't exist`() {
        assertFailsWith<AppError.NotFound> {
            services.routes.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid1 = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 150, uid)
        val rid2 = db.routes.createNewRoute(conn, "Chelas", "Odivelas", 150, uid)
        val rid3 = db.routes.createNewRoute(conn, "Lisboa", "Chelas", 150, uid)

        val route0 = Route(rid1, "Odivelas", "Chelas", 0.15, uid)
        val route1 = Route(rid2, "Chelas", "Odivelas", 0.15, uid)
        val route2 = Route(rid3, "Lisboa", "Chelas", 0.15, uid)

        assertEquals(listOf(route0, route1, route2), services.routes.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        assertEquals(emptyList(), services.routes.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val sid = services.sports.createNewSport(token, "Powerlifting", "Get big")

        assertEquals(Sport(sid, "Powerlifting", uid, "Get big"), db.sports.getSport(conn, sid))
    }

    @Test
    fun `createNewSport returns correct identifier`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val uid1 = services.sports.createNewSport(token, "Powerlifting", "Get big")
        val uid2 = services.sports.createNewSport(token, "Swimming", "Be like a fish")
        val uid3 = services.sports.createNewSport(token, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    @Test
    fun `createNewSport throws AppError InvalidArgument if the name isn't valid (name too short)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<AppError.InvalidArgument> {
                services.sports.createNewSport(token, "Po", "Get big")
            }
        }

    @Test
    fun `createNewSport throws AppError InvalidArgument if the name isn't valid (name too long)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<AppError.InvalidArgument> {
                services.sports.createNewSport(token, "Powerlifting".repeat(5), "Get big")
            }
        }

    @Test
    fun `createNewSport throws AppError InvalidArgument if the description isn't valid (description too long)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<AppError.InvalidArgument> {
                services.sports.createNewSport(token, "Powerlifting", "lala".repeat(1000))
            }
        }

    @Test
    fun `createNewSport throws AppError InvalidCredentials if the token isn't valid`() {
        val token = "lalala"

        assertFailsWith<AppError.InvalidCredentials> {
            services.sports.createNewSport(token, "Powerlifting", "Get big")
        }
    }

    // getSport

    @Test
    fun `getSport returns the sport object`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        assertEquals(Sport(sid, "Soccer", uid, "Kick a ball to score a goal"), services.sports.getSport(sid))
    }

    @Test
    fun `getSport throws AppError InvalidArgument if the sid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.sports.getSport(-4)
        }
    }

    @Test
    fun `getSport throws AppError NotFound if the sport with the sid doesn't exist`() {
        assertFailsWith<AppError.NotFound> {
            services.sports.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sid1 = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")
        val sid2 = db.sports.createNewSport(conn, uid, "Powerlifting", "Get big")
        val sid3 = db.sports.createNewSport(conn, uid, "Basketball", "Shoot a ball through a hoop")

        val sport1 = Sport(sid1, "Soccer", uid, "Kick a ball to score a goal")
        val sport2 = Sport(sid2, "Powerlifting", uid, "Get big")
        val sport3 = Sport(sid3, "Basketball", uid, "Shoot a ball through a hoop")

        assertEquals(listOf(sport1, sport2, sport3), services.sports.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        assertEquals(emptyList(), services.sports.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 100, uid)

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
    fun `createNewActivity throws AppError InvalidArgument if the sid isn't valid (not positive)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 100, uid)

            assertFailsWith<AppError.InvalidArgument> {
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
    fun `createNewActivity throws AppError InvalidArgument if rid isn't valid (not positive)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

            assertFailsWith<AppError.InvalidArgument> {
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
    fun `createNewActivity throws AppError InvalidCredentials if the token isn't valid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = "lala"
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")
        val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 100, uid)

        assertFailsWith<AppError.InvalidCredentials> {
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
    fun `createNewActivity throws AppError NotFound if a sport with the sid doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val rid = db.routes.createNewRoute(conn, "Pontinha", "Chelas", 100, uid)

            assertFailsWith<AppError.NotFound> {
                services.activities.createNewActivity(
                    token,
                    "2022-11-05".toLocalDate(),
                    "14:59:27.903".toDuration(),
                    3,
                    rid
                )
            }
        }

    @Test
    fun `createNewActivity throws AppError NotFound if a route with the rid doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

            assertFailsWith<AppError.NotFound> {
                services.activities.createNewActivity(
                    token,
                    "2022-11-05".toLocalDate(),
                    "14:59:27.903".toDuration(),
                    sid,
                    3
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
            services.activities.getActivity(aid)
        )
    }

    @Test
    fun `getActivity throws AppError InvalidArgument if the aid isn't valid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivity(-5)
        }
    }

    @Test
    fun `getActivity throws AppError NotFound if an activity with the aid doesn't exist`() {
        assertFailsWith<AppError.NotFound> {
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

        assertFailsWith<AppError.NotFound> {
            db.activities.getActivity(conn, aid)
        }
    }

    @Test
    fun `deleteActivity throws AppError InvalidArgument if the aid isn't valid (not positive)`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

            assertFailsWith<AppError.InvalidArgument> {
                services.activities.deleteActivity(token, -4)
            }
        }

    @Test
    fun `deleteActivity throws AppError InvalidCredentials if the token isn't valid`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = "lalala"
        val aid =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1)

        assertFailsWith<AppError.InvalidCredentials> {
            services.activities.deleteActivity(token, aid)
        }
    }

    @Test
    fun `deleteActivity throws AppError NotFound if an activity with the aid doesn't exist`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            assertFailsWith<AppError.NotFound> {
                services.activities.deleteActivity(token, 1)
            }
        }

    @Test
    fun `deleteActivity throws AppError Forbidden if the user doesn't have permission to delete this activity`(): Unit =
        db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
            val aid =
                db.activities.createNewActivity(
                    conn,
                    uid2,
                    "2022-11-20".toLocalDate(),
                    "23:44:59.903".toDuration(),
                    1,
                    1
                )

            assertFailsWith<AppError.Forbidden> {
                services.activities.deleteActivity(token, aid)
            }
        }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list of the sport`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        val aid = db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        val activities = services.sports.getSportActivities(sid)

        assertEquals(
            listOf(Activity(aid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid, sid, 1)),
            activities
        )
    }

    @Test
    fun `getSportActivities throws AppError InvalidArgument if the sid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.sports.getSportActivities(-5)
        }
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list of a user`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        val aid = db.activities.createNewActivity(
            conn,
            uid,
            "2022-11-20".toLocalDate(),
            "20:23:55.263".toDuration(),
            sid,
            1
        )

        val activities = services.users.getUserActivities(uid)

        assertEquals(
            listOf(Activity(aid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid, sid, 1)),
            activities
        )
    }

    @Test
    fun `getUserActivities throws AppError InvalidArgument if the uid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.users.getUserActivities(-5)
        }
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val sid = db.sports.createNewSport(conn, uid, "Soccer", "Kick a ball to score a goal")

        val aid =
            db.activities.createNewActivity(conn, uid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), sid, 1)

        val activities =
            services.activities.getActivities(
                sid = sid,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = null
            )

        assertEquals(
            listOf(Activity(aid, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), uid, sid, 1)),
            activities
        )
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the sid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = -5,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = null
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the rid is invalid (not positive)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = -5,
                limit = null,
                skip = null
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the limit is invalid (not higher than 0)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = -2,
                skip = null
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the limit is invalid (equal to 0)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = 0,
                skip = null
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the skip is invalid (not higher than 0)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = -2
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the skip is invalid (equal to 0)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = 0
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the skip is invalid (less than limit)`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "descending",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = 3,
                skip = 2
            )
        }
    }

    @Test
    fun `getActivities throws AppError InvalidArgument if the orderBy is invalid (not 'ascending' or 'descending')`() {
        assertFailsWith<AppError.InvalidArgument> {
            services.activities.getActivities(
                sid = 1,
                "up",
                "2022-11-20".toLocalDate(),
                rid = 1,
                limit = null,
                skip = null
            )
        }
    }
}
