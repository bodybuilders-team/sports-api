package pt.isel.ls.sports.unit.database.sections.routes

import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.tableAsserter
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RoutesPostgresDBTests : AppPostgresDBTests(), RoutesDBTests {

    // createNewRoute

    @Test
    override fun `createNewRoute creates route correctly in the database`() {
        val rid = db.execute { conn ->
            db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)
        }

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM routes WHERE id = ?")
            stm.setInt(1, rid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Odivelas", "Chelas", 0.15, 1)
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("start_location"))
                assertEquals(mockRow[2], row.getString("end_location"))
                assertEquals(mockRow[3], row.getDouble("distance"))
                assertEquals(mockRow[4], row.getInt("uid"))
            }
        }
    }

    @Test
    override fun `createNewRoute returns correct identifier`(): Unit = db.execute { conn ->

        val rid0 = db.routes.createNewRoute(conn, "Odivelas", "Chelas", 0.15, 1)
        val rid1 = db.routes.createNewRoute(conn, "Chelas", "Odivelas", 0.15, 1)
        val rid2 = db.routes.createNewRoute(conn, "Lisboa", "Chelas", 0.15, 1)

        assertEquals(4, rid0)
        assertEquals(5, rid1)
        assertEquals(6, rid2)
    }

    // getRoute

    @Test
    override fun `getRoute returns the route object`(): Unit = db.execute { conn ->
        val route = db.routes.getRoute(conn, 1)
        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), route)
    }

    @Test
    override fun `getRoute throws NotFoundException if there's no route with the rid`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.routes.getRoute(conn, 0)
            }
        }

    // getAllRoutes

    @Test
    override fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->
        val routes = listOf(
            Route(1, "Odivelas", "Chelas", 0.15, 1),
            Route(2, "Chelas", "Odivelas", 0.15, 2),
            Route(3, "Lisboa", "Porto", 1.5, 3)
        )

        assertEquals(routes, db.routes.searchRoutes(conn, 0, 10).routes)
    }

    @Test
    override fun `getAllRoutes with no created routes returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.routes.searchRoutes(conn, 0, 10).routes)
        }
    }

    @Test
    override fun `getAllRoutes with skip works`(): Unit = db.execute { conn ->
        val routes = listOf(
            Route(2, "Chelas", "Odivelas", 0.15, 2),
            Route(3, "Lisboa", "Porto", 1.5, 3)
        )

        assertEquals(routes, db.routes.searchRoutes(conn, 1, 10).routes)
    }

    @Test
    override fun `getAllRoutes with limit works`(): Unit = db.execute { conn ->
        val routes = listOf(
            Route(1, "Odivelas", "Chelas", 0.15, 1),
            Route(2, "Chelas", "Odivelas", 0.15, 2)
        )

        assertEquals(routes, db.routes.searchRoutes(conn, 0, 2).routes)
    }

    // hasRoute

    @Test
    override fun `hasRoute returns true if the route exists`(): Unit = db.execute { conn ->
        assertTrue(db.routes.hasRoute(conn, 1))
    }

    @Test
    override fun `hasRoute returns false if the route does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.routes.hasRoute(conn, 9999999))
    }
}
