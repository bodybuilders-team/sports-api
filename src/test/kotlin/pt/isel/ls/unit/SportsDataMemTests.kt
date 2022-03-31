package pt.isel.ls.unit

import pt.isel.ls.sports.data.SortOrder
import pt.isel.ls.sports.data.SportsDataMem
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsDataMemTests {
    private var db: SportsDataMem = SportsDataMem()

    @BeforeTest
    fun initializeDataMem() {
        db = SportsDataMem()
    }

    @Test
    fun `createNewUser creates user correctly in the database`() {
        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"), db.users[uid])
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val uid1 = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.createNewUser("André Jesus", "andrejesus@mail.com")
        val uid3 = db.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getUser

    @Test
    fun `getUser returns the user object`() {
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.users[1] = user

        assertEquals(user, db.getUser(1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`() {
        assertFailsWith<SportsError> {
            db.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        db.users[1] = user1
        db.users[2] = user2
        db.users[3] = user3

        assertEquals(listOf(user1, user2, user3), db.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        assertEquals(emptyList(), db.getAllUsers())
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = db.createUserToken(1)

        assertEquals(1, db.tokens[token])
    }

    @Test
    fun `Having multiple tokens for the same user is allowed`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token0 = db.createUserToken(1)
        val token1 = db.createUserToken(1)
        val token2 = db.createUserToken(1)
        val token3 = db.createUserToken(1)

        assertEquals(1, db.tokens[token0])
        assertEquals(1, db.tokens[token1])
        assertEquals(1, db.tokens[token2])
        assertEquals(1, db.tokens[token3])
    }

    // getUID

    @Test
    fun `getUID returns the uid correctly`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = UUID.randomUUID().toString()
        db.tokens[token] = 1

        assertEquals(1, db.getUID(token))
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`() {
        assertFailsWith<SportsError> {
            db.getUID("T-o-k-e-n")
        }
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.createNewRoute("Odivelas", "Chelas", 150, 1)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), db.routes[1])
    }

    @Test
    fun `createNewRoute returns correct identifier`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid0 = db.createNewRoute("Odivelas", "Chelas", 150, 1)
        val rid1 = db.createNewRoute("Chelas", "Odivelas", 150, 1)
        val rid2 = db.createNewRoute("Lisboa", "Chelas", 150, 1)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val route = Route(1, "Odivelas", "Chelas", 150.0, 1)

        db.routes[1] = route

        assertEquals(route, db.getRoute(1))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {
        assertFailsWith<SportsError> {
            db.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {
        db.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val route0 = Route(1, "Odivelas", "Chelas", 150.0, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 150.0, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 150.0, 1)

        db.routes[1] = route0
        db.routes[2] = route1
        db.routes[3] = route2

        assertEquals(listOf(route0, route1, route2), db.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        assertEquals(emptyList(), db.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.createNewSport(1, "Powerlifting", "Get big")

        assertEquals(Sport(rid, "Powerlifting", 1, "Get big"), db.sports[1])
    }

    @Test
    fun `createNewSport returns correct identifier`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid1 = db.createNewSport(1, "Powerlifting", "Get big")
        val uid2 = db.createNewSport(1, "Swimming", "Be like a fish")
        val uid3 = db.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getSport

    @Test
    fun `getSport returns the sport object`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val sport = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        db.sports[1] = sport

        assertEquals(sport, db.getSport(1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {
        assertFailsWith<SportsError> {
            db.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`() {
        db.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 2, "Get big")
        val sport3 = Sport(3, "Basketball", 3, "Shoot a ball through a hoop")

        db.sports[1] = sport1
        db.sports[2] = sport2
        db.sports[3] = sport3

        assertEquals(listOf(sport1, sport2, sport3), db.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        assertEquals(emptyList(), db.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val aid = db.createNewActivity(1, "2022-11-05", "14:56:27.903", 1, 1)
        assertEquals(Activity(aid, "2022-11-05", "14:56:27.903", 1, 1, 1), db.activities[1])
    }

    // getActivity

    @Test
    fun `getActivity returns the activity object`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val activity = Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)

        db.activities[1] = activity

        assertEquals(activity, db.getActivity(1))
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {
        assertFailsWith<SportsError> {
            db.getActivity(1)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.activities[1] = Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)

        db.deleteActivity(1)

        assertEquals(null, db.activities[1])
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        db.activities[1] = Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)

        val activities = db.getSportActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        db.activities[1] = Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)

        val activities = db.getUserActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`() {
        db.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports[1] = Sport(1, "Soccer", 1, "Kick a ball to score a goal")

        db.activities[1] = Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)

        val activities = db.getActivities(sid = 1, SortOrder.ASCENDING, "2022-11-20", rid = 1, null, null)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // TODO: 26/03/2022 Add more tests (synchronize with SportPostgresTests?)
}
