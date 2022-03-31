package pt.isel.ls.sports.data.sports

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.Postgres
import pt.isel.ls.sports.data.setStringOrNull
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppError
import java.sql.SQLException
import java.sql.Statement

class SportsPostgres(dataSource: PGSimpleDataSource) : Postgres(dataSource), SportsDatabase {
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
                throw AppError.notFound("Sport with id $sid not found")
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

    override fun getSportActivities(sid: Int): List<Activity> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE sid = ?
                """.trimIndent()
            )
            stm.setInt(1, sid)

            return getActivities(stm)
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
}
