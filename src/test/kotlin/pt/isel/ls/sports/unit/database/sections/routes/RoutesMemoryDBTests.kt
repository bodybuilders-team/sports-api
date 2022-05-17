package pt.isel.ls.sports.unit.database.sections.routes

import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.database.AppMemoryDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RoutesMemoryDBTests : AppMemoryDBTests(), RoutesDBTests {

    // createNewRoute

    @Test
    override fun `createNewRoute creates route correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val rid = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)

        assertEquals(Route(rid, "Odivelas", "Chelas", 0.15, 1), source.routes[1])
    }

    @Test
    override fun `createNewRoute returns correct identifier`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val rid1 = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)
        val rid2 = db.routes.createNewRoute(conn, "Chelas", "Odivelas", 0.15, 1)
        val rid3 = db.routes.createNewRoute(conn, "Lisboa", "Chelas", 0.15, 1)

        assertEquals(1, rid1)
        assertEquals(2, rid2)
        assertEquals(3, rid3)
    }

    // updateRoute

    @Test
    override fun `updateRoute updates a route correctly`(): Unit = db.execute { conn ->
        val route1 = Route(1, "Odivelas", "Chelas", 150.0, 1)
        source.routes[1] = route1

        val newStartLocation = "new start location"
        val newEndLocation = "new end location"
        val newDistance = 124.0
        db.routes.updateRoute(conn, 1, newStartLocation, newEndLocation, newDistance)

        val updatedSport = db.routes.getRoute(conn, 1)

        assertEquals(newStartLocation, updatedSport.startLocation)
        assertEquals(newEndLocation, updatedSport.endLocation)
        assertEquals(newDistance, updatedSport.distance)
    }

    @Test
    override fun `updateRoute returns true if a route was modified`(): Unit = db.execute { conn ->
        val route1 = Route(1, "Odivelas", "Chelas", 150.0, 1)
        source.routes[1] = route1

        val newStartLocation = "new start location"
        val newEndLocation = "new end location"
        val newDistance = 124.0
        assertTrue(db.routes.updateRoute(conn, 1, newStartLocation, newEndLocation, newDistance))
    }

    @Test
    override fun `updateRoute returns false if a route was not modified`(): Unit = db.execute { conn ->
        val route1 = Route(1, "Odivelas", "Chelas", 150.0, 1)
        source.routes[1] = route1

        assertFalse(db.routes.updateRoute(conn, 1, route1.startLocation, route1.endLocation, route1.distance))
    }

    @Test
    override fun `updateRoute throws NotFoundException if there's no route with the rid`(): Unit = db.execute { conn ->
        assertFailsWith<NotFoundException> {
            db.routes.updateRoute(conn, 9999, "new start location", "new end location", 124.0)
        }
    }

    @Test
    override fun `throws InvalidArgumentException if startLocation, endLocation and distance are both null`(): Unit =
        db.execute { conn ->
            val route1 = Route(1, "Odivelas", "Chelas", 150.0, 1)
            source.routes[1] = route1

            assertFailsWith<InvalidArgumentException> {
                db.routes.updateRoute(conn, 1, null, null, null)
            }
        }

    // getRoute

    @Test
    override fun `getRoute returns the route object`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

        val route = Route(1, "Odivelas", "Chelas", 150.0, 1)

        source.routes[1] = route

        assertEquals(route, db.routes.getRoute(conn, 1))
    }

    @Test
    override fun `getRoute throws NotFoundException if there's no route with the rid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.routes.getRoute(conn, 1)
            }
        }

    // getAllRoutes

    @Test
    override fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        source.routes[1] = route0
        source.routes[2] = route1
        source.routes[3] = route2

        assertEquals(listOf(route0, route1, route2), db.routes.searchRoutes(conn, 0, 10).routes)
    }

    @Test
    override fun `getAllRoutes with no created routes returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.routes.searchRoutes(conn, 0, 10).routes)
    }

    @Test
    override fun `getAllRoutes with skip works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        source.routes[1] = route0
        source.routes[2] = route1
        source.routes[3] = route2

        assertEquals(listOf(route1, route2), db.routes.searchRoutes(conn, 1, 10).routes)
    }

    @Test
    override fun `getAllRoutes with limit works`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")

        val route0 = Route(1, "Odivelas", "Chelas", 0.15, 1)
        val route1 = Route(2, "Chelas", "Odivelas", 0.15, 1)
        val route2 = Route(3, "Lisboa", "Chelas", 0.15, 1)

        source.routes[1] = route0
        source.routes[2] = route1
        source.routes[3] = route2

        assertEquals(listOf(route0, route1), db.routes.searchRoutes(conn, 0, 2).routes)
    }

    // hasRoute

    @Test
    override fun `hasRoute returns true if the route exists`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "André Jesus", "andrejesus@mail.com", "H42xS")
        source.routes[1] = Route(1, "Odivelas", "Chelas", 0.15, 1)

        assertTrue(db.routes.hasRoute(conn, 1))
    }

    @Test
    override fun `hasRoute returns false if the route does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.routes.hasRoute(conn, 1))
    }
}
