package pt.isel.ls.sports.data

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.UUID

class SportsPostgres(databaseUrl: String) : SportsDatabase {

    private val dataSource = PGSimpleDataSource().apply {
        setURL(databaseUrl)
    }

    /**
     * Gets a connection and uses it with [block].
     * @param block a function to process in use
     * @return the result of block function invoked
     * @throws SportsError.databaseError if an error occurs while acessing the database
     */
    private inline fun <R> useConnection(block: (connection: Connection) -> R): R =
        runCatching {
            dataSource.connection
        }.getOrElse {
            when (it) {
                is SQLException ->
                    throw SportsError.databaseError("Error accessing database")
                else -> throw it
            }
        }.use(block)

    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
    override fun createNewUser(name: String, email: String): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO users(name, email)
                VALUES (?, ?)
                """.trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            stm.setString(1, name)
            stm.setString(2, email)

            if (stm.executeUpdate() == 0)
                throw SQLException("Creating user failed, no rows affected.")

            val generatedKeys = stm.generatedKeys
            return if (generatedKeys.next()) generatedKeys.getInt(1) else -1
        }

    /**
     * Gets the user object.
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    override fun getUser(uid: Int): User =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            val rs = stm.executeQuery()

            if (rs.next())
                return getUserFromTable(rs)
            else
                throw SportsError.notFound("User with id $uid not found")
        }

    override fun hasUserWithEmail(email: String): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE email = ?
                """.trimIndent()
            )
            stm.setString(1, email)

            val rs = stm.executeQuery()

            return rs.next()
        }

    override fun hasUser(uid: Int): Boolean =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                WHERE id = ?
                """.trimIndent()
            )
            stm.setInt(1, uid)

            val rs = stm.executeQuery()

            return rs.next()
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

    /**
     * Get the list of users.
     *
     * @return list of user identifiers
     */
    override fun getAllUsers(): List<User> =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT *
                FROM users
                """.trimIndent()
            )

            val rs = stm.executeQuery()
            val users = mutableListOf<User>()

            while (rs.next())
                users.add(getUserFromTable(rs))

            return users
        }

    /**
     * Gets a User object from a ResultSet.
     * @param rs table
     * @return user
     */
    private fun getUserFromTable(rs: ResultSet) = User(
        id = rs.getInt(1),
        name = rs.getString(2),
        email = rs.getString(3)
    )

    /**
     * Associates a user [token] with the [uid].
     *
     * @param token user token
     * @param uid user's identifier
     *
     * @return user's token
     */
    override fun createUserToken(token: UUID, uid: Int): String =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                INSERT INTO tokens(token, uid)
                VALUES (?, ?)
                """.trimIndent()
            )
            val stringToken = token.toString()

            stm.setString(1, stringToken)
            stm.setInt(2, uid)

            if (stm.executeUpdate() == 0)
                throw SQLException("Creating token failed, no rows affected.")

            return stringToken
        }

    /**
     * Gets the uid associated with the [token].
     *
     * @param token user's token
     *
     * @return uid
     */
    override fun getUID(token: String): Int =
        useConnection { conn ->
            val stm = conn.prepareStatement(
                """
                SELECT uid
                FROM tokens
                WHERE token = ?
                """.trimIndent()
            )
            stm.setString(1, token)

            val rs = stm.executeQuery()

            if (rs.next())
                return rs.getInt(1)
            else
                throw SportsError.notFound("Token $token isn't associated to any user")
        }

    /**
     * Creates a new route.
     *
     * @param startLocation
     * @param endLocation
     * @param distance
     * @param uid user's unique identifier
     *
     * @return the route's unique identifier
     */
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

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     *
     * @return the route
     */
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
                throw SportsError.notFound("Route with id $rid not found")
        }

    /**
     * Get the list of routes.
     *
     * @return list of route identifiers
     */
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
                throw SportsError.notFound("Sport with id $sid not found")
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

    /**
     * Gets a Sport object from a ResultSet.
     * @param rs table
     * @return sport
     */
    private fun getSportFromTable(rs: ResultSet) = Sport(
        id = rs.getInt(1),
        name = rs.getString(2),
        uid = rs.getInt(4),
        description = rs.getString(3)
    )

    /**
     * Create a new activity.
     *
     * @param date
     * @param duration
     * @param uid user's unique identifier
     * @param sid sport's unique identifier
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
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

    /**
     * Get an activity.
     *
     * @param aid activity's unique identifier
     *
     * @return the activity object
     */
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
                throw SportsError.notFound("Activity with id $aid not found")
        }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
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

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of identifiers of activities of a sport
     */
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

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of identifiers of activities made from a user
     */
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

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     *
     * @return list of activities identifiers
     */
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
        date = rs.getDate(2).toString(),
        duration = rs.getString(3),
        uid = rs.getInt(4),
        sid = rs.getInt(5),
        rid = rs.getInt(6).let { if (rs.wasNull()) null else it }
    )
}
