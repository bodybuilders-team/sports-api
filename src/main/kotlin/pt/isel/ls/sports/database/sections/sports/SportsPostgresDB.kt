package pt.isel.ls.sports.database.sections.sports

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.postgres.AbstractPostgresDB
import pt.isel.ls.sports.database.utils.setStringOrNull
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppError
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class SportsPostgresDB(dataSource: PGSimpleDataSource) : AbstractPostgresDB(dataSource), SportsDB {
    /**
     * Create a new sport.
     *
     * @param name the sport's name
     * @param description the sport's description
     * @param uid user's unique identifier
     *
     * @return the sport's unique identifier
     */
    override fun createNewSport(uid: Int, name: String, description: String?): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
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

    /**
     * Get a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    override fun getSport(sid: Int): Sport =
        useConnection { conn ->
            val stm = conn.prepareStatement(
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
                throw AppError.NotFound("Sport with id $sid not found")
        }

    /**
     * Get the list of all sports.
     *
     * @return list of identifiers of all sports
     */
    override fun getAllSports(): List<Sport> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM sports
                """.trimIndent()
            )

            val rs = stm.executeQuery()
            val sports = mutableListOf<Sport>()

            while (rs.next())
                sports.add(
                    getSportFromTable(rs)
                )

            return sports
        }

    override fun hasSport(sid: Int): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
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
         * Gets a Sport object from a ResultSet.
         * @param rs table
         * @return sport
         */
        private fun getSportFromTable(rs: ResultSet) = Sport(
            id = rs.getInt(1),
            name = rs.getString(2),
            description = rs.getString(3),
            uid = rs.getInt(4)
        )
    }
}
