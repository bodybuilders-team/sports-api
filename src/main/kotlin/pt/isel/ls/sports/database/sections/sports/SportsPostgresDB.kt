package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.utils.setStringOrNull
import pt.isel.ls.sports.domain.Sport
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

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

    override fun getAllSports(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): SportsResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                SELECT *, count(*) OVER() AS totalCount
                FROM sports
                OFFSET ?
                LIMIT ?
                """.trimIndent()
            )

        stm.setInt(1, skip)
        stm.setInt(2, limit)

        val rs = stm.executeQuery()
        val sports = mutableListOf<Sport>()

        if (!rs.next())
            return SportsResponse(sports, 0)

        val totalCount = rs.getInt("totalCount")

        do {
            sports.add(getSportFromTable(rs))
        } while (rs.next())

        return SportsResponse(sports, totalCount)
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
