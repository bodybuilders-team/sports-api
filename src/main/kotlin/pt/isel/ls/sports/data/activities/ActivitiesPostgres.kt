package pt.isel.ls.sports.data.activities

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.data.Postgres
import pt.isel.ls.sports.data.SortOrder
import pt.isel.ls.sports.data.setIntOrNull
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.AppError
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement

class ActivitiesPostgres(dataSource: PGSimpleDataSource) : Postgres(dataSource), ActivitiesDatabase {

    override fun createNewActivity(uid: Int, date: String, duration: String, sid: Int, rid: Int?): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO activities(date, duration, uid, sid, rid)
                VALUES (?, ?, ?, ?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setDate(1, Date.valueOf(date))
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
                throw AppError.notFound("Activity with id $aid not found")
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
        date: String?,
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
}
