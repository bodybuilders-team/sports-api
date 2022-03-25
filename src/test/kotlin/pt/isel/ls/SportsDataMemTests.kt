package pt.isel.ls

import pt.isel.ls.sports.NotFoundException
import pt.isel.ls.sports.data.SportsDataMem
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsDataMemTests {

    // createNewUser

    @Test
    fun `createNewUser creates user correctly in the database`() {
        val db = SportsDataMem()

        val uid = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"), db.users[uid])
    }

    @Test
    fun `createNewUser returns correct identifier`() {
        val db = SportsDataMem()

        val uid0 = db.createNewUser("Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid1 = db.createNewUser("André Jesus", "andrejesus@mail.com")
        val uid2 = db.createNewUser("André Páscoa", "andrepascoa@mail.com")

        assertEquals(0, uid0)
        assertEquals(1, uid1)
        assertEquals(2, uid2)
    }

    // getUser

    @Test
    fun `getUser returns the user object`() {
        val db = SportsDataMem()

        val user = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        db.users[0] = user

        assertEquals(user, db.getUser(0))
    }

    @Test
    fun `getUser throws NotFoundException if the user with the uid doesn't exist`() {
        val db = SportsDataMem()

        assertFailsWith<NotFoundException> {
            db.getUser(0)
        }
    }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`() {
        val db = SportsDataMem()

        val user0 = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user1 = User(1, "André Jesus", "andrejesus@mail.com")
        val user2 = User(2, "André Páscoa", "andrepascoa@mail.com")

        db.users[0] = user0
        db.users[1] = user1
        db.users[2] = user2

        assertEquals(listOf(user0, user1, user2), db.getAllUsers())
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        val db = SportsDataMem()

        assertEquals(emptyList(), db.getAllUsers())
    }

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.createNewRoute("Odivelas", "Chelas", 150, 0)

        assertEquals(Route(rid, "Odivelas", "Chelas", 150, 0), db.routes[0])
    }

    @Test
    fun `createNewRoute returns correct identifier`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid0 = db.createNewRoute("Odivelas", "Chelas", 150, 0)
        val uid1 = db.createNewRoute("Chelas", "Odivelas", 150, 0)
        val uid2 = db.createNewRoute("Lisboa", "Chelas", 150, 0)

        assertEquals(0, uid0)
        assertEquals(1, uid1)
        assertEquals(2, uid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val route = Route(0, "Odivelas", "Chelas", 150, 0)

        db.routes[0] = route

        assertEquals(route, db.getRoute(0))
    }

    @Test
    fun `getRoute throws NotFoundException if the route with the rid doesn't exist`() {
        val db = SportsDataMem()

        assertFailsWith<NotFoundException> {
            db.getRoute(0)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "André Jesus", "andrejesus@mail.com")

        val route0 = Route(0, "Odivelas", "Chelas", 150, 0)
        val route1 = Route(1, "Chelas", "Odivelas", 150, 0)
        val route2 = Route(2, "Lisboa", "Chelas", 150, 0)

        db.routes[0] = route0
        db.routes[1] = route1
        db.routes[2] = route2

        assertEquals(listOf(route0, route1, route2), db.getAllRoutes())
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        val db = SportsDataMem()

        assertEquals(emptyList(), db.getAllRoutes())
    }

    // createNewSport

    @Test
    fun `createNewSport creates sport correctly in the database`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.createNewSport("Powerlifting", "Get big", 0)

        assertEquals(Sport(rid, "Powerlifting", "Get big", 0), db.sports[0])
    }

    @Test
    fun `createNewSport returns correct identifier`() {
        val db = SportsDataMem()

        db.users[0] = User(0, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val uid0 = db.createNewSport("Powerlifting", "Get big", 0)
        val uid1 = db.createNewSport("Swimming", "Be like a fish", 0)
        val uid2 = db.createNewSport("Soccer", "Kick a ball to score a goal", 0)

        assertEquals(0, uid0)
        assertEquals(1, uid1)
        assertEquals(2, uid2)
    }

    // TODO: 24/03/2022 Finish rest of methods and implement SportsServicesTests and SportsPostgresTests
}
