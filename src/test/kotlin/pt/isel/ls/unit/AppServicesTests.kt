package pt.isel.ls.unit

import pt.isel.ls.sports.database.AppMemoryDB
import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.AppServices
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
    fun `createNewUser creates user correctly in the database`() {
        val createUserResponse = services.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(
            User(createUserResponse.uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"),
            db.users.getUser(createUserResponse.uid)
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

    // getUser

    @Test
    fun `getUser returns the user object`() {
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(user, services.users.getUser(1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`() {

        assertFailsWith<AppError> {
            services.users.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {

        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.users.createNewUser("André Jesus", "andrejesus@mail.com")
        db.users.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(listOf(user1, user2, user3), services.users.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {

        assertEquals(emptyList(), services.users.getAllUsers())
    }

    @Test
    fun `createNewRoute creates route correctly in the database`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(rid))
    }

    @Test
    fun `createNewRoute returns correct identifier`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val rid0 = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)
        val rid1 = services.routes.createNewRoute(token, "Chelas", "Odivelas", 0.150)
        val rid2 = services.routes.createNewRoute(token, "Lisboa", "Chelas", 0.150)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(rid))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {

        assertFailsWith<AppError> {
            services.routes.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.tokens.createUserToken(UUID.randomUUID(), uid)

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        db.routes.createNewRoute("Odivelas", "Chelas", 150, 1)
        db.routes.createNewRoute("Chelas", "Odivelas", 150, 1)
        db.routes.createNewRoute("Lisboa", "Chelas", 150, 1)

        assertEquals(listOf(route0, route1, route2), services.routes.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {

        assertEquals(emptyList(), services.routes.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val sid = services.sports.createNewSport(token, "Powerlifting", "Get big")

        assertEquals(Sport(sid, "Powerlifting", uid, "Get big"), db.sports.getSport(sid))
    }

    @Test
    fun `createNewSport returns correct identifier`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val uid1 = services.sports.createNewSport(token, "Powerlifting", "Get big")
        val uid2 = services.sports.createNewSport(token, "Swimming", "Be like a fish")
        val uid3 = services.sports.createNewSport(token, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getSport

    @Test
    fun `getSport returns the sport object`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        assertEquals(Sport(1, "Soccer", 1, "Kick a ball to score a goal"), services.sports.getSport(1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {

        assertFailsWith<AppError> {
            services.sports.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")
        db.sports.createNewSport(1, "Powerlifting", "Get big")
        db.sports.createNewSport(1, "Basketball", "Shoot a ball through a hoop")

        val sport1 = Sport(1, "Soccer", 1, "Kick a ball to score a goal")
        val sport2 = Sport(2, "Powerlifting", 1, "Get big")
        val sport3 = Sport(3, "Basketball", 1, "Shoot a ball through a hoop")

        assertEquals(listOf(sport1, sport2, sport3), services.sports.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {

        assertEquals(emptyList(), services.sports.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {

        val uid = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val aid = services.activities.createNewActivity(token, "2022-11-05", "14:59:27.903", 1, 1)

        assertEquals(Activity(aid, "2022-11-05", "14:59:27.903", 1, 1, 1), db.activities.getActivity(aid))
    }

    // getActivity

    @Test
    fun `getActivity returns the activity object`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.activities.createNewActivity(1, "2022-11-20", "20:23:55.263", 1, 1)

        assertEquals(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1), services.activities.getActivity(1))
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {

        assertFailsWith<AppError> {
            services.activities.getActivity(1)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`() {

        val mockId = db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), mockId)
        db.activities.createNewActivity(1, "2022-11-20", "23:44:59.903", 1, 1)

        services.activities.deleteActivity(token, 1)

        assertFailsWith<AppError> {
            db.activities.getActivity(1)
        }
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(1, "2022-11-20", "20:23:55.263", 1, 1)

        val activities = services.sports.getSportActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(1, "2022-11-20", "20:23:55.263", 1, 1)

        val activities = services.users.getUserActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`() {

        db.users.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.sports.createNewSport(1, "Soccer", "Kick a ball to score a goal")

        db.activities.createNewActivity(1, "2022-11-20", "20:23:55.263", 1, 1)

        val activities = services.activities.getActivities(sid = 1, "descending", "2022-11-20", rid = 1, limit = null, skip = null)

        assertEquals(listOf(Activity(1, "2022-11-20", "20:23:55.263", 1, 1, 1)), activities)
    }

    // TODO: 26/03/2022 Add more tests
}
