package pt.isel.ls.sports.database.sections.activities

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.users.UsersPostgresDB.Companion.getUsersResponse
import pt.isel.ls.sports.database.sections.users.UsersResponse
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.database.utils.getPaginatedQuery
import pt.isel.ls.sports.database.utils.getSQLDate
import pt.isel.ls.sports.database.utils.setIntOrNull
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.utils.toDTOString
import pt.isel.ls.sports.utils.toDuration
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import kotlin.time.Duration

/**
 * Activities database representation using Postgres.
 */
class ActivitiesPostgresDB : ActivitiesDB {

    override fun createNewActivity(
        conn: ConnectionDB,
        uid: Int,
        date: LocalDate,
        duration: Duration,
        sid: Int,
        rid: Int?
    ): Int {
        val stm = conn.getPostgresConnection().prepareStatement(
            """
                INSERT INTO activities(date, duration, uid, sid, rid)
                VALUES (?, ?, ?, ?, ?)
            """.trimIndent(),
            Statement.RETURN_GENERATED_KEYS
        )

        stm.setDate(1, getSQLDate(date))
        stm.setString(2, duration.toDTOString())
        stm.setInt(3, uid)
        stm.setInt(4, sid)
        stm.setIntOrNull(5, rid)

        if (stm.executeUpdate() == 0)
            throw SQLException("Creating activity failed, no rows affected.")

        val generatedKeys = stm.generatedKeys
        return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
    }

    override fun updateActivity(
        conn: ConnectionDB,
        aid: Int,
        date: LocalDate?,
        duration: Duration?,
        sid: Int?,
        rid: Int?
    ): Boolean {
        if (!hasActivity(conn, aid))
            throw NotFoundException("Activity not found.")

        if (date == null && duration == null && sid == null && rid == null)
            throw InvalidArgumentException("Date, duration, sid or rid must be specified.")

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                    UPDATE activities
                    SET date = COALESCE(?, date),
                    duration = COALESCE(?, duration),
                    sid = COALESCE(?, sid),
                    rid = COALESCE(?, rid)
                    where id = ? AND (date != ? OR duration != ? OR sid != ? OR rid != ?)
                """.trimIndent()
            )

        stm.setDate(1, date?.let { getSQLDate(it) })
        stm.setString(2, duration?.toDTOString())
        stm.setIntOrNull(3, sid)
        stm.setIntOrNull(4, rid)
        stm.setInt(5, aid)
        stm.setDate(6, date?.let { getSQLDate(it) })
        stm.setString(7, duration?.toDTOString())
        stm.setIntOrNull(8, sid)
        stm.setIntOrNull(9, rid)

        return stm.executeUpdate() == 1
    }

    override fun getActivity(
        conn: ConnectionDB,
        aid: Int
    ): Activity {
        val pgConn = conn.getPostgresConnection()
        val rs = doActivityQuery(pgConn, aid)

        if (rs.next())
            return getActivityFromTable(rs)
        else
            throw NotFoundException("Activity with id $aid not found")
    }

    override fun deleteActivity(
        conn: ConnectionDB,
        aid: Int
    ) {
        if (!hasActivity(conn, aid))
            throw NotFoundException("Activity with id $aid not found")

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                """
                DELETE FROM activities
                WHERE id = ?
                """.trimIndent()
            )
        stm.setInt(1, aid)
        stm.executeUpdate()
    }

    override fun searchActivities(
        conn: ConnectionDB,
        sid: Int,
        orderBy: SortOrder,
        date: LocalDate?,
        rid: Int?,
        skip: Int,
        limit: Int
    ): ActivitiesResponse {
        val queryDate = if (date != null) "AND date = ?" else ""
        val queryRid = if (rid != null) "AND rid = ?" else ""

        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM activities
                WHERE sid = ? $queryDate  $queryRid
                ORDER BY duration ${orderBy.str}
                    """.trimIndent()
                )
            )
        stm.setInt(1, sid)

        var counter = 1

        if (date != null)
            stm.setDate(++counter, getSQLDate(date))

        if (rid != null)
            stm.setInt(++counter, rid)

        stm.setInt(++counter, skip)
        stm.setInt(++counter, limit)

        return getActivitiesResponse(stm)
    }

    override fun searchUsersByActivity(
        conn: ConnectionDB,
        sid: Int,
        rid: Int?,
        skip: Int,
        limit: Int
    ): UsersResponse {
        val queryRid = if (rid != null) "AND rid = ?" else "AND rid IS NULL"
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM users
                JOIN (
                SELECT uid
                FROM activities
                WHERE sid = ? $queryRid
                ORDER BY duration
                ) AS activityUids ON users.id = activityUids.uid
                    """.trimIndent()
                )
            )
        var counter = 1

        stm.setInt(counter++, sid)
        if (rid != null)
            stm.setInt(counter++, rid)

        stm.setInt(counter++, skip)
        stm.setInt(counter, limit)

        return getUsersResponse(stm)
    }

    override fun getSportActivities(
        conn: ConnectionDB,
        sid: Int,
        skip: Int,
        limit: Int
    ): ActivitiesResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM activities
                WHERE sid = ?
                    """.trimIndent()
                )
            )

        stm.setInt(1, sid)
        stm.setInt(2, skip)
        stm.setInt(3, limit)

        return getActivitiesResponse(stm)
    }

    override fun getUserActivities(
        conn: ConnectionDB,
        uid: Int,
        skip: Int,
        limit: Int
    ): ActivitiesResponse {
        val stm = conn
            .getPostgresConnection()
            .prepareStatement(
                getPaginatedQuery(
                    """
                SELECT *
                FROM activities
                WHERE uid = ?
                    """.trimIndent()
                )
            )
        stm.setInt(1, uid)
        stm.setInt(2, skip)
        stm.setInt(3, limit)

        return getActivitiesResponse(stm)
    }

    override fun hasActivity(
        conn: ConnectionDB,
        aid: Int
    ): Boolean {
        val pgConn = conn
            .getPostgresConnection()
        val rs = doActivityQuery(pgConn, aid)

        return rs.next()
    }

    companion object {

        /**
         * Gets a list of activities returned from the execution of the statement [stm].
         *
         * @param stm statement
         * @return [ActivitiesResponse] with the list of activities
         */
        private fun getActivitiesResponse(stm: PreparedStatement): ActivitiesResponse {
            val rs = stm.executeQuery()
            val activities = mutableListOf<Activity>()

            rs.next()
            val totalCount = rs.getInt("totalCount")

            if (rs.getObject("id") != null)
                do {
                    activities.add(getActivityFromTable(rs))
                } while (rs.next())

            return ActivitiesResponse(activities, totalCount)
        }

        /**
         * Gets an [Activity] from a ResultSet.
         *
         * @param rs table
         * @return activity object
         */
        private fun getActivityFromTable(rs: ResultSet) = Activity(
            id = rs.getInt(1),
            date = rs.getDate(2).toLocalDate().toKotlinLocalDate(),
            duration = rs.getString(3).toDuration(),
            uid = rs.getInt(4),
            sid = rs.getInt(5),
            rid = rs.getInt(6).let { if (rs.wasNull()) null else it }
        )

        /**
         * Executes a query on the "activities" table given the [aid].
         *
         * @param conn connection
         * @param aid activity's unique identifier
         * @return result set
         */
        private fun doActivityQuery(conn: Connection, aid: Int): ResultSet {
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, aid)

            return stm.executeQuery()
        }
    }
}
