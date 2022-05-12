package pt.isel.ls.sports.database.sections.routes

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.utils.getPaginatedQuery
import pt.isel.ls.sports.domain.Route
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * Routes database representation using Postgres.
 */
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

    override fun updateRoute(conn: ConnectionDB, rid: Int, startLocation: String?, endLocation: String?): Boolean {
        if (!hasRoute(conn, rid))
            throw NotFoundException("Route not found.")

        if (startLocation == null && endLocation == null)
            throw InvalidArgumentException("Start or end location must be specified.")

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                    UPDATE routes
                    SET start_location = COALESCE($1, start_location),
                    end_location= COALESCE($2, end_location)
                    where id = ?
                """.trimIndent()
            )

        stm.setString(1, startLocation)
        stm.setString(2, endLocation)
        stm.setInt(3, rid)

        return stm.executeUpdate() == 1
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

    override fun searchRoutes(
        conn: ConnectionDB,
        skip: Int,
        limit: Int,
        startLocation: String?,
        endLocation: String?
    ): RoutesResponse {

        val where = if (startLocation != null || endLocation != null) "WHERE" else ""
        val startLocationQuery = if (startLocation != null) "start_location ILIKE ?" else ""
        val and = if (startLocation != null) "AND" else ""
        val endLocationQuery = if (endLocation != null) "end_location ILIKE ?" else ""

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM routes
                $where $startLocationQuery $and $endLocationQuery
                    """.trimIndent()
                )
            )

        var counter = 1
        if (startLocation != null)
            stm.setString(counter++, "%$startLocation%")
        if (endLocation != null)
            stm.setString(counter++, "%$endLocation%")

        stm.setInt(counter++, skip)
        stm.setInt(counter, limit)

        return getRoutesResponse(stm)
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
         * Gets a [Route] from a ResultSet.
         *
         * @param rs table
         * @return route object
         */
        private fun getRouteFromTable(rs: ResultSet) = Route(
            id = rs.getInt(1),
            startLocation = rs.getString(2),
            endLocation = rs.getString(3),
            distance = rs.getDouble(4),
            uid = rs.getInt(5)
        )

        /**
         * Executes a query on the "routes" table given the [rid].
         *
         * @param conn connection
         * @param rid route's unique identifier
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

        /**
         * Gets a list of routes returned from the execution of the statement [stm].
         *
         * @param stm statement
         * @return [RoutesResponse] with the list of Routes
         */
        private fun getRoutesResponse(stm: PreparedStatement): RoutesResponse {
            val rs = stm.executeQuery()
            val routes = mutableListOf<Route>()

            rs.next()
            val totalCount = rs.getInt("totalCount")

            if (rs.getObject("id") != null)
                do {
                    routes.add(getRouteFromTable(rs))
                } while (rs.next())

            return RoutesResponse(routes, totalCount)
        }
    }
}
