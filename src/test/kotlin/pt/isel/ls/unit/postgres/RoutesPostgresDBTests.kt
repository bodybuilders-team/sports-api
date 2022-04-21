package pt.isel.ls.unit.postgres

import org.junit.Test
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.tableAsserter
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RoutesPostgresDBTests : AppPostgresDBTests() {
// createNewRoute

    @Test
    fun `createNewRoute creates route correctly in the database`() {
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

// getRoute

    @Test
    fun `getRoute returns the route object`(): Unit = db.execute { conn ->
        val route = db.routes.getRoute(conn, 1)
        assertEquals(Route(1, "Odivelas", "Chelas", 0.15, 1), route)
    }

    @Test
    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppException> {
                db.routes.getRoute(conn, 0)
            }
        }

// getAllRoutes

    @Test
    fun `getAllRoutes returns list of all route objects`(): Unit = db.execute { conn ->
        val routes = listOf(
            Route(1, "Odivelas", "Chelas", 0.15, 1),
            Route(2, "Chelas", "Odivelas", 0.15, 2),
            Route(3, "Lisboa", "Porto", 1.5, 3)
        )

        assertEquals(routes, db.routes.getAllRoutes(conn, 0, 10).routes)
    }

    @Test
    fun `getAllRoutes with no created routes returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.routes.getAllRoutes(conn, 0, 10).routes)
        }
    }
}
