package pt.isel.ls.sports.database.tables.activities

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.database.postgres.AbstractPostgresDB
import pt.isel.ls.sports.database.utils.SortOrder
import pt.isel.ls.sports.database.utils.setIntOrNull
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import kotlin.time.Duration

class ActivitiesPostgresDB(dataSource: PGSimpleDataSource) : AbstractPostgresDB(dataSource), ActivitiesDB {

    override fun createNewActivity(uid: Int, date: LocalDateTime, duration: Duration, sid: Int, rid: Int?): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO activities(date, duration, uid, sid, rid)
                VALUES (?, ?, ?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )

            val sqlDate = Date.from(date.toInstant(TimeZone.UTC).toJavaInstant())

            stm.setDate(1, sqlDate as Date?)
            stm.setString(2, duration)
            stm.setInt(3, uid)
            stm.setInt(4, sid)
            stm.setIntOrNull(5, rid)

            if (stm.executeUpdate() == 0)
                throw SQLException("Creating activity failed, no rows affected.")

            val generatedKeys = stm.generatedKeys
            return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
        }

    override fun getActivity(aid: Int): Activity =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, aid)

            val rs = stm.executeQuery()

            if (rs.next())
                return getActivityFromTable(rs)
            else
                throw AppError.NotFound("Activity with id $aid not found")
        }

    override fun deleteActivity(aid: Int) {
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                DELETE FROM activities
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, aid)
            stm.executeUpdate()
        }
    }

    override fun getActivities(
        sid: Int,
        orderBy: SortOrder,
        date: LocalDateTime?,
        rid: Int?,
        skip: Int?,
        limit: Int?
    ): List<Activity> =
        useConnection { conn ->
            val queryDate = if (date != null) "AND date = ?" else ""
            val queryRid = if (date != null) "AND rid = ?" else ""

            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE sid = ? $queryDate  $queryRid
                ORDER BY duration ${orderBy.str} 
                LIMIT ?
                OFFSET ?
                """.trimIndent()
            )
            stm.setInt(1, sid)

            var counter = 1

            if (date != null)
                stm.setDate(++counter, Date.valueOf(date))

            if (rid != null)
                stm.setInt(++counter, rid)

            stm.setIntOrNull(++counter, skip)
            stm.setIntOrNull(++counter, limit)

            return getActivities(stm)
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

    override fun getUserActivities(uid: Int): List<Activity> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE uid = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            return getActivities(stm)
        }

    override fun hasActivity(aid: Int): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM activities
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, aid)

            val rs = stm.executeQuery()

            return rs.next()
        }

    companion object {
        /**
         * Gets a list of activities returned from the execution of the statement [stm]
         *
         * @param stm statement
         *
         * @return list of activities
         */
        private fun getActivities(stm: PreparedStatement): MutableList<Activity> {
            val rs = stm.executeQuery()
            val activities = mutableListOf<Activity>()

            while (rs.next()) {
                activities.add(
                    getActivityFromTable(rs)
                )
            }
            return activities
        }

        /**
         * Gets an Activity object from a ResultSet.
         * @param rs table
         * @return activity
         */
        private fun getActivityFromTable(rs: ResultSet) = Activity(
            id = rs.getInt(1),
            date = rs.getDate(2),
            duration = rs.getString(3),
            uid = rs.getInt(4),
            sid = rs.getInt(5),
            rid = rs.getInt(6).let { if (rs.wasNull()) null else it }
        )
    }
}
