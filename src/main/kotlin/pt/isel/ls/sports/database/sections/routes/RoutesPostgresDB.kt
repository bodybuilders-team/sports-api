package pt.isel.ls.sports.database.sections.routes

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.postgres.AbstractPostgresDB
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppError
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class RoutesPostgresDB(dataSource: PGSimpleDataSource) : AbstractPostgresDB(dataSource), RoutesDB {

    override fun createNewRoute(startLocation: String, endLocation: String, distance: Int, uid: Int): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO routes(uid, start_location, end_location, distance)
                VALUES (?, ?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setInt(1, uid)
            stm.setString(2, startLocation)
            stm.setString(3, endLocation)
            stm.setInt(4, distance)

            if (stm.executeUpdate() == 0)
                throw SQLException("Creating route failed, no rows affected.")

            val generatedKeys = stm.generatedKeys
            return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
        }

    override fun getRoute(rid: Int): Route =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM routes
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, rid)

            val rs = stm.executeQuery()

            if (rs.next())
                return getRouteFromTable(rs)
            else
                throw AppError.NotFound("Route with id $rid not found")
        }

    override fun getAllRoutes(): List<Route> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM routes
                """.trimIndent()
            )

            val rs = stm.executeQuery()
            val routes = mutableListOf<Route>()

            while (rs.next())
                routes.add(
                    getRouteFromTable(rs)
                )

            return routes
        }

    override fun hasRoute(rid: Int): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM routes
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, rid)

            val rs = stm.executeQuery()

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
            start_location = rs.getString(2),
            end_location = rs.getString(3),
            distance = rs.getInt(4) / 1000.0,
            uid = rs.getInt(5)
        )
    }
}
