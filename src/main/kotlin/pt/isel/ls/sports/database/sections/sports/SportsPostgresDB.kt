package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.utils.getPaginatedQuery
import pt.isel.ls.sports.database.utils.setStringOrNull
import pt.isel.ls.sports.domain.Sport
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * Sports database representation using Postgres.
 */
class SportsPostgresDB : SportsDB {

    override fun createNewSport(conn: ConnectionDB, uid: Int, name: String, description: String?): Int {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                INSERT INTO sports(name, description, uid)
                VALUES (?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )

        stm.setString(1, name)
        stm.setStringOrNull(2, description)
        stm.setInt(3, uid)

        if (stm.executeUpdate() == 0)
            throw SQLException("Creating sport failed, no rows affected.")

        val generatedKeys = stm.generatedKeys
        return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
    }

    override fun updateSport(conn: ConnectionDB, sid: Int, name: String?, description: String?): Boolean {
        if (!hasSport(conn, sid))
            throw NotFoundException("Sport not found.")

        if (name == null && description == null)
            throw InvalidArgumentException("Name or description must be specified.")

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                    UPDATE sports
                    SET name = COALESCE(?, name),
                        description= COALESCE(?, description)
                    WHERE id = ?
                """.trimIndent()
            )

        stm.setString(1, name)
        stm.setString(2, description)
        stm.setInt(3, sid)

        return stm.executeUpdate() == 1
    }

    override fun getSport(conn: ConnectionDB, sid: Int): Sport {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT *
                FROM sports
                WHERE id = ?
                """.trimIndent()
            )
        stm.setInt(1, sid)

        val rs = stm.executeQuery()

        if (rs.next())
            return getSportFromTable(rs)
        else
            throw NotFoundException("Sport with id $sid not found")
    }

    override fun searchSports(conn: ConnectionDB, skip: Int, limit: Int, name: String?): SportsResponse {
        val nameQuery = if (name != null) "WHERE name ILIKE ?" else ""
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                    SELECT *
                    FROM sports
                    $nameQuery
                    """.trimIndent()
                )
            )
        var counter = 1
        if (name != null)
            stm.setString(counter++, "%$name%")

        stm.setInt(counter++, skip)
        stm.setInt(counter, limit)

        return getSportsResponse(stm)
    }

    override fun hasSport(conn: ConnectionDB, sid: Int): Boolean {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT *
                FROM sports
                WHERE id = ?
                """.trimIndent()
            )
        stm.setInt(1, sid)

        val rs = stm.executeQuery()

        return rs.next()
    }

    companion object {
        /**
         * Gets a list of Sports returned from the execution of the statement [stm].
         *
         * @param stm statement
         * @return [SportsResponse] with the list of Sports
         */
        private fun getSportsResponse(stm: PreparedStatement): SportsResponse {
            val rs = stm.executeQuery()
            val sports = mutableListOf<Sport>()

            rs.next()
            val totalCount = rs.getInt("totalCount")

            if (rs.getObject("id") != null)
                do {
                    sports.add(getSportFromTable(rs))
                } while (rs.next())

            return SportsResponse(sports, totalCount)
        }

        /**
         * Gets a [Sport] from a ResultSet.
         *
         * @param rs table
         * @return sport object
         */
        private fun getSportFromTable(rs: ResultSet) = Sport(
            id = rs.getInt(1),
            name = rs.getString(2),
            description = rs.getString(3),
            uid = rs.getInt(4)
        )
    }
}
