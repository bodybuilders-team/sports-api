package pt.isel.ls.sports.unit.services.sections.routes

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.unit.services.AppServicesTests
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RoutesServicesTests : AppServicesTests() {
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

    // getRoute

    @Test
    fun `getRoute returns the route object`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(conn, rid))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`() {

        assertFailsWith<NotFoundException> {
            services.routes.getRoute(1)
        }
    }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)
        db.routes.createNewRoute(conn, "Chelas", "Odivelas", 0.15, 1)
        db.routes.createNewRoute(conn, "Lisboa", "Chelas", 0.15, 1)

        assertEquals(listOf(route0, route1, route2), services.routes.getAllRoutes(0, 10).routes)
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {

        assertEquals(emptyList(), services.routes.getAllRoutes(0, 10).routes)
    }
}
