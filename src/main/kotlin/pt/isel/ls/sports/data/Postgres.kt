package pt.isel.ls.sports.data

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

abstract class Postgres(val dataSource: PGSimpleDataSource) {
    /**
     * Gets a connection and uses it with [block].
     * @param block a function to process in use
     * @return the result of block function invoked
     * @throws AppError.databaseError if an error occurs while accessing the database
     */
    protected inline fun <R> useConnection(block: (connection: Connection) -> R): R =
        runCatching {
            dataSource.connection
        }.getOrElse {
            when (it) {
                is SQLException ->
                    throw AppError.databaseError("Error accessing database")
                else -> throw it
            }
        }.use(block)

    /**
     * Gets a User object from a ResultSet.
     * @param rs table
     * @return user
     */
    protected fun getUserFromTable(rs: ResultSet) = User(
        id = rs.getInt(1),
        name = rs.getString(2),
        email = rs.getString(3)
    )

    /**
     * Gets a Route object from a ResultSet.
     * @param rs table
     * @return route
     */
    protected fun getRouteFromTable(rs: ResultSet) = Route(
        id = rs.getInt(1),
        start_location = rs.getString(2),
        end_location = rs.getString(3),
        distance = rs.getInt(4) / 1000.0,
        uid = rs.getInt(5)
    )

    /**
     * Gets a Sport object from a ResultSet.
     * @param rs table
     * @return sport
     */
    protected fun getSportFromTable(rs: ResultSet) = Sport(
        id = rs.getInt(1),
        name = rs.getString(2),
        uid = rs.getInt(4),
        description = rs.getString(3)
    )

    /**
     * Gets a list of activities returned from the execution of the statement [stm]
     *
     * @param stm statement
     *
     * @return list of activities
     */
    protected fun getActivities(stm: PreparedStatement): MutableList<Activity> {
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
    protected fun getActivityFromTable(rs: ResultSet) = Activity(
        id = rs.getInt(1),
        date = rs.getDate(2).toString(),
        duration = rs.getString(3),
        uid = rs.getInt(4),
        sid = rs.getInt(5),
        rid = rs.getInt(6).let { if (rs.wasNull()) null else it }
    )
}
