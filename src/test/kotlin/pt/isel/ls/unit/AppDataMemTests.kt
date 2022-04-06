package pt.isel.ls.unit

import kotlinx.datetime.toLocalDate
import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.toDuration
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppDataMemTests {
    private var source = AppMemoryDBSource()
    private var db: AppMemoryDB = AppMemoryDB(source)

    @BeforeTest
    fun initializeDataMem() {
        source = AppMemoryDBSource()
        db = AppMemoryDB(source)
    }

    @Test
    fun `createNewUser creates user correctly in the database`() {
        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"), source.users[uid])
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val uid1 = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser("André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getUser

    @Test
    fun `getUser returns the user object`() {
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        source.users[1] = user

        assertEquals(user, db.users.getUser(1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`() {
        assertFailsWith<AppError> {
            db.users.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        source.users[1] = user1
        source.users[2] = user2
        source.users[3] = user3

        assertEquals(listOf(user1, user2, user3), db.users.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        assertEquals(emptyList(), db.users.getAllUsers())
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = db.tokens.createUserToken(UUID.randomUUID(), 1)

        assertEquals(1, source.tokens[token])
    }

    @Test
    fun `Having multiple tokens for the same user is allowed`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token0 = db.tokens.createUserToken(UUID.randomUUID(), 1)
        val token1 = db.tokens.createUserToken(UUID.randomUUID(), 1)
        val token2 = db.tokens.createUserToken(UUID.randomUUID(), 1)
        val token3 = db.tokens.createUserToken(UUID.randomUUID(), 1)

        assertEquals(1, source.tokens[token0])
        assertEquals(1, source.tokens[token1])
        assertEquals(1, source.tokens[token2])
        assertEquals(1, source.tokens[token3])
    }

    // getUID

    @Test
    fun `getUID returns the uid correctly`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = UUID.randomUUID().toString()
        source.tokens[token] = 1

        assertEquals(1, db.tokens.getUID(token))
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`() {
        assertFailsWith<AppError> {
            db.tokens.getUID("T-o-k-e-n")
        }
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.routes.createNewRoute("Odivelas", "Chelas", 150, 1)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), source.routes[1])
    }

    @Test
    fun `createNewRoute returns correct identifier`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid0 = db.routes.createNewRoute("Odivelas", "Chelas", 150, 1)
        val rid1 = db.routes.createNewRoute("Chelas", "Odivelas", 150, 1)
        val rid2 = db.routes.createNewRoute("Lisboa", "Chelas", 150, 1)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val route = Route(1, "Odivelas", "Chelas", 150.0, 1)

        source.routes[1] = route

        assertEquals(route, db.routes.getRoute(1))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {
        assertFailsWith<AppError> {
            db.routes.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val route0 = Route(1, "Odivelas", "Chelas", 150.0, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 150.0, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 150.0, 1)

        source.routes[1] = route0
        source.routes[2] = route1
        source.routes[3] = route2

        assertEquals(listOf(route0, route1, route2), db.routes.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        assertEquals(emptyList(), db.routes.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.sports.createNewSport(1, "Powerlifting", "Get big")

        assertEquals(Sport(rid, "Powerlifting", 1, "Get big"), source.sports[1])
    }

    @Test
    fun `createNewSport returns correct identifier`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid1 = db.sports.createNewSport(1, "Powerlifting", "Get big")
        val uid2 = db.sports.createNewSport(1, "Swimming", "Be like a fish")
        val uid3 = db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getSport

    @Test
    fun `getSport returns the sport object`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sport = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.sports[1] = sport

        assertEquals(sport, db.sports.getSport(1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {
        assertFailsWith<AppError> {
            db.sports.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`() {
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        source.sports[1] = sport1
        source.sports[2] = sport2
        source.sports[3] = sport3

        assertEquals(listOf(sport1, sport2, sport3), db.sports.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        assertEquals(emptyList(), db.sports.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val aid =
            db.activities.createNewActivity(1, "2022-11-05".toLocalDate(), "14:56:27.903".toDuration(), 1, 1)
        assertEquals(
            Activity(aid, "2022-11-05".toLocalDate(), "14:56:27.903".toDuration(), 1, 1, 1),
            source.activities[1]
        )
    }

    // getActivity

    @Test
    fun `getActivity returns the activity object`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val activity = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        source.activities[1] = activity

        assertEquals(activity, db.activities.getActivity(1))
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {
        assertFailsWith<AppError> {
            db.activities.getActivity(1)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        db.activities.deleteActivity(1)

        assertEquals(null, source.activities[1])
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.getSportActivities(1)

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.getUserActivities(1)

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`() {
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        source.activities[1] = Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)

        val activities = db.activities.getActivities(
            sid = 1,
            SortOrder.ASCENDING,
            "2022-11-20".toLocalDate(),
            rid = 1,
            null,
            null
        )

        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "20:23:55.263".toDuration(), 1, 1, 1)),
            activities
        )
    }

    // TODO: 26/03/2022 Add more tests (synchronize with SportPostgresTests?)
}
