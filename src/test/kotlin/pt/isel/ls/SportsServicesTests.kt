package pt.isel.ls

import org.junit.Test
import pt.isel.ls.sports.SportsServices
import pt.isel.ls.sports.data.SportsDataMem
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsServicesTests {

    // createNewUser

    @Test
    fun `createNewUser creates user correctly in the database`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val createUserResponse = services.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(
            User(createUserResponse.uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"),
            db.getUser(createUserResponse.uid)
        )
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val createUserResponse1 = services.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val createUserResponse2 = services.createNewUser("André Jesus", "andrejesus@mail.com")
        val createUserResponse3 = services.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, createUserResponse1.uid)
        assertEquals(2, createUserResponse2.uid)
        assertEquals(3, createUserResponse3.uid)
    }

    // getUser

    @Test
    fun `getUser returns the user object`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(user, services.getUser(1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertFailsWith<SportsError> {
            services.getUser(1)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.createNewUser("André Jesus", "andrejesus@mail.com")
        db.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(listOf(user1, user2, user3), services.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertEquals(emptyList(), services.getAllUsers())
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val rid = services.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(rid, "Odivelas", "Chelas", 150, 1), db.getRoute(rid))
    }

    @Test
    fun `createNewRoute returns correct identifier`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val rid0 = services.createNewRoute(token, "Odivelas", "Chelas", 0.150)
        val rid1 = services.createNewRoute(token, "Chelas", "Odivelas", 0.150)
        val rid2 = services.createNewRoute(token, "Lisboa", "Chelas", 0.150)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val rid = services.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(1, "Odivelas", "Chelas", 150, 1), db.getRoute(rid))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertFailsWith<SportsError> {
            services.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val route0 = Route(1, "Odivelas", "Chelas", 150, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 150, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 150, 1)

        db.createNewRoute("Odivelas", "Chelas", 150, 1)
        db.createNewRoute("Chelas", "Odivelas", 150, 1)
        db.createNewRoute("Lisboa", "Chelas", 150, 1)

        assertEquals(listOf(route0, route1, route2), services.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertEquals(emptyList(), services.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val sid = services.createNewSport(token, "Powerlifting", "Get big")

        assertEquals(Sport(sid, "Powerlifting", "Get big", uid), db.getSport(sid))
    }

    @Test
    fun `createNewSport returns correct identifier`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val uid1 = services.createNewSport(token, "Powerlifting", "Get big")
        val uid2 = services.createNewSport(token, "Swimming", "Be like a fish")
        val uid3 = services.createNewSport(token, "Soccer", "Kick a ball to score a goal")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getSport

    @Test
    fun `getSport returns the sport object`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.createNewSport("Soccer", "Kick a ball to score a goal", 1)

        assertEquals(Sport(1, "Soccer", "Kick a ball to score a goal", 1), services.getSport(1))
    }

    @Test
    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertFailsWith<SportsError> {
            services.getSport(1)
        }
    }

    // getAllSports

    @Test
    fun `getAllSports returns list of all sport objects`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.createNewSport("Soccer", "Kick a ball to score a goal", 1)
        db.createNewSport("Powerlifting", "Get big", 1)
        db.createNewSport("Basketball", "Shoot a ball through a hoop", 1)

        val sport1 = Sport(1, "Soccer", "Kick a ball to score a goal", 1)
        val sport2 = Sport(2, "Powerlifting", "Get big", 1)
        val sport3 = Sport(3, "Basketball", "Shoot a ball through a hoop", 1)

        assertEquals(listOf(sport1, sport2, sport3), services.getAllSports())
    }

    @Test
    fun `getAllSports with no created sports returns empty list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertEquals(emptyList(), services.getAllSports())
    }

    // createNewActivity

    @Test
    fun `createNewActivity creates activity correctly in the database`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.createUserToken(uid)

        val aid = services.createNewActivity(token, "2022-11-05", "14:66:27.903", 1, 1)

        assertEquals(Activity(aid, "2022-11-05", "14:66:27.903", 1, 1, 1), db.getActivity(aid))
    }

    // getActivity

    @Test
    fun `getActivity returns the activity object`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.createNewActivity("2022-11-20", "72:44:63.903", 1, 1, 1)

        assertEquals(Activity(1, "2022-11-20", "72:44:63.903", 1, 1, 1), services.getActivity(1))
    }

    @Test
    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        assertFailsWith<SportsError> {
            services.getActivity(1)
        }
    }

    // deleteActivity

    @Test
    fun `deleteActivity deletes an activity successfully`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.createNewActivity("2022-11-20", "72:44:63.903", 1, 1, 1)

        services.deleteActivity(1)

        assertFailsWith<SportsError> {
            db.getActivity(1)
        }
    }

    // getSportActivities

    @Test
    fun `getSportActivities returns the activities list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.createNewSport("Soccer", "Kick a ball to score a goal", 1)

        db.createNewActivity("2022-11-20", "72:44:63.903", 1, 1, 1)

        val activities = services.getSportActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 1, 1)), activities)
    }

    // getUserActivities

    @Test
    fun `getUserActivities returns the activities list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.createNewSport("Soccer", "Kick a ball to score a goal", 1)

        db.createNewActivity("2022-11-20", "72:44:63.903", 1, 1, 1)

        val activities = services.getUserActivities(1)

        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 1, 1)), activities)
    }

    // getActivities

    @Test
    fun `getActivities returns the activities list`() {
        val db = SportsDataMem()
        val services = SportsServices(db)

        db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.createNewSport("Soccer", "Kick a ball to score a goal", 1)

        db.createNewActivity("2022-11-20", "72:44:63.903", 1, 1, 1)

        val activities = services.getActivities(sid = 1, "descending", "2022-11-20", rid = 1, limit = null, skip = null)

        assertEquals(listOf(Activity(1, "2022-11-20", "72:44:63.903", 1, 1, 1)), activities)
    }

    // TODO: 26/03/2022 Add more tests
}
