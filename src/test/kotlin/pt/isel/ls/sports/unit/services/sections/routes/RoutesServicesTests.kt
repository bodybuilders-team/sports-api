package pt.isel.ls.sports.unit.services.sections.routes

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.InvalidArgumentException
import pt.isel.ls.sports.unit.services.AbstractServicesTests
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RoutesServicesTests : AbstractServicesTests() {

    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)

        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), db.routes.getRoute(conn, rid))
    }

    @Test
    fun `createNewRoute returns correct identifier`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val rid1 = services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)
        val rid2 = services.routes.createNewRoute(token, "Chelas", "Odivelas", 0.150)
        val rid3 = services.routes.createNewRoute(token, "Lisboa", "Chelas", 0.150)

        assertEquals(1, rid1)
        assertEquals(2, rid2)
        assertEquals(3, rid3)
    }

    @Test
    fun `createNewRoute throws InvalidArgumentException if the distance is negative`(): Unit = db.execute { conn ->

        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        assertFailsWith<InvalidArgumentException> {
            services.routes.createNewRoute(token, "Odivelas", "Chelas", -0.150)
        }
    }

    @Test
    fun `createNewRoute throws AuthenticationException if a user with the token was not found`(): Unit =
        db.execute { conn ->
            db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
            val token = "Lalala"

            assertFailsWith<AuthenticationException> {
                services.routes.createNewRoute(token, "Odivelas", "Chelas", 0.150)
            }
        }

    // getRoute

    @Test
    fun `getRoute returns route object`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val rid = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.150, uid)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.150, uid), services.routes.getRoute(rid))
    }

    @Test
    fun `getRoute throws InvalidArgumentException if the rid is not positive`() {

        assertFailsWith<InvalidArgumentException> {
            services.routes.getRoute(-5)
        }
    }

    @Test
    fun `getRoute throws NotFoundException if there's no route with the rid`() {

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

    @Test
    fun `getAllRoutes throws InvalidArgumentException if the skip is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.routes.getAllRoutes(-5, 10)
        }
    }

    @Test
    fun `getAllRoutes throws InvalidArgumentException if the limit is invalid`() {
        assertFailsWith<InvalidArgumentException> {
            services.routes.getAllRoutes(0, -5)
        }
    }
}
