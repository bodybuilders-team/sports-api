package pt.isel.ls.unit.memory

import org.junit.Test
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RoutesMemoryDBTests : AppMemoryDBTests() {
    // createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), source.routes[1])
    }

    @Test
    fun `createNewRoute returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val rid0 = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)
        val rid1 = db.routes.createNewRoute(conn, "Chelas", "Odivelas", 0.15, 1)
        val rid2 = db.routes.createNewRoute(conn, "Lisboa", "Chelas", 0.15, 1)

        assertEquals(1, rid0)
        assertEquals(2, rid1)
        assertEquals(3, rid2)
    }

    // getRoute

    @Test
    fun `getRoute returns the route object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val route = Route(1, "Odivelas", "Chelas", 150.0, 1)

        source.routes[1] = route

        assertEquals(route, db.routes.getRoute(conn, 1))
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppException> {
                db.routes.getRoute(conn, 1)
            }
        }

    // getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com")

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        source.routes[1] = route0
        source.routes[2] = route1
        source.routes[3] = route2

        assertEquals(listOf(route0, route1, route2), db.routes.getAllRoutes(conn, 0, 10).routes)
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.routes.getAllRoutes(conn, 0, 10).routes)
    }
}
