package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.connection.getPostgresConnection
import pt.isel.ls.sports.domain.Route
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class RoutesPostgresDB : RoutesDB {

    override fun createNewRoute(
        conn: ConnectionDB,
        startLocation: String,
        endLocation: String,
        distance: Double,
        uid: Int
    ): Int {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                INSERT INTO routes(uid, start_location, end_location, distance)
                VALUES (?, ?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
        stm.setInt(1, uid)
        stm.setString(2, startLocation)
        stm.setString(3, endLocation)
        stm.setDouble(4, distance)

        if (stm.executeUpdate() == 0)
            throw SQLException("Creating route failed, no rows affected.")

        val generatedKeys = stm.generatedKeys
        return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
    }

    override fun getRoute(
        conn: ConnectionDB,
        rid: Int
    ): Route {
        val pgConn = conn
            .getPostgresConnection()

        val rs = doRouteQuery(pgConn, rid)

        if (rs.next())
            return getRouteFromTable(rs)
        else
            throw NotFoundException("Route with id $rid not found")
    }

    override fun getAllRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): RoutesResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT *, count(*) OVER() AS totalCount
                FROM routes
                OFFSET ?
                LIMIT ?
                """.trimIndent()
            )

        stm.setInt(1, skip)
        stm.setInt(2, limit)

        val rs = stm.executeQuery()
        val routes = mutableListOf<Route>()

        if (!rs.next())
            return RoutesResponse(routes, 0)

        val totalCount = rs.getInt("totalCount")

        do {
            routes.add(getRouteFromTable(rs))
        } while (rs.next())

        return RoutesResponse(routes, totalCount)
    }

    override fun hasRoute(
        conn: ConnectionDB,
        rid: Int
    ): Boolean {
        val pgConn = conn
            .getPostgresConnection()
        val rs = doRouteQuery(pgConn, rid)

        return rs.next()
    }

    companion object {

        /**
         * Gets a Route object from a ResultSet.
         * @param rs table
         * @return route
         */
        private fun getRouteFromTable(rs: ResultSet) = Route(
            id = rs.getInt(1),
            startLocation = rs.getString(2),
            endLocation = rs.getString(3),
            distance = rs.getDouble(4),
            uid = rs.getInt(5)
        )

        /**
         * Executes a query on the routes table given the [rid].
         *
         * @param conn connection
         * @param rid route id
         * @return result set
         */
        private fun doRouteQuery(conn: Connection, rid: Int): ResultSet {
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM routes
                WHERE id = ?
                """.trimIndent()
            )

            stm.setInt(1, rid)
            return stm.executeQuery()
        }
    }
}
