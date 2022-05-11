package pt.isel.ls.sports.services

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.InvalidArgumentException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.services.utils.isValidId

/**
 * Abstract Services class that implements the common methods of all services.
 */
abstract class AbstractServices(protected val db: AppDB) {
    /**
     * Gets the user's unique identifier associated with the [token].
     *
     * @param conn database Connection
     * @param token user's token
     *
     * @return user's unique identifier associated with the [token]
     *
     * @throws AuthenticationException if a user with the [token] was not found
     */
    protected fun authenticate(conn: ConnectionDB, token: String) = try {
        db.tokens.getUID(conn, token)
    } catch (error: NotFoundException) {
        throw AuthenticationException("Invalid token")
    }

    /**
     * Validates a sport id.
     *
     * @param sid sport's unique identifier
     * @throws InvalidArgumentException if [sid] is negative
     */
    protected fun validateSid(sid: Int) {
        if (!isValidId(sid))
            throw InvalidArgumentException("Sport id must be positive")
    }

    /**
     * Validates a route id.
     *
     * @param rid route's unique identifier
     * @throws InvalidArgumentException if [rid] is negative
     */
    protected fun validateRid(rid: Int) {
        if (!isValidId(rid))
            throw InvalidArgumentException("Route id must be positive")
    }

    /**
     * Validates an activity id.
     *
     * @param aid activity's unique identifier
     * @throws InvalidArgumentException if [aid] is negative
     */
    protected fun validateAid(aid: Int) {
        if (!isValidId(aid))
            throw InvalidArgumentException("Activity id must be positive")
    }

    /**
     * Validates a user id.
     *
     * @param uid user's unique identifier
     * @throws InvalidArgumentException if [uid] is negative
     */
    protected fun validateUid(uid: Int) {
        if (!isValidId(uid))
            throw InvalidArgumentException("User id must be positive")
    }

    /**
     * Validates the existence of a user with the [uid].
     *
     * @param conn database Connection
     * @param uid user's unique identifier
     *
     * @throws NotFoundException if there's no user with the [uid]
     */
    protected fun validateUserExists(conn: ConnectionDB, uid: Int) {
        if (!db.users.hasUser(conn, uid))
            throw NotFoundException("User id not found")
    }

    /**
     * Validates the existence of a sport with the [sid].
     *
     * @param conn database Connection
     * @param sid sport's unique identifier
     *
     * @throws NotFoundException if there's no sport with the [sid]
     */
    protected fun validateSportExists(conn: ConnectionDB, sid: Int) {
        if (!db.sports.hasSport(conn, sid))
            throw NotFoundException("Sport id not found")
    }

    /**
     * Validates the existence of a route with the [rid].
     *
     * @param conn database Connection
     * @param rid route's unique identifier
     *
     * @throws NotFoundException if there's no route with the [rid]
     */
    protected fun validateRouteExists(conn: ConnectionDB, rid: Int) {
        if (!db.routes.hasRoute(conn, rid))
            throw NotFoundException("Route id not found")
    }

    /**
     * Validates the existence of an activity with the [aid].
     *
     * @param conn database Connection
     * @param aid activity's unique identifier
     *
     * @throws NotFoundException if there's no activity with the [aid]
     */
    protected fun validateActivityExists(conn: ConnectionDB, aid: Int) {
        if (!db.activities.hasActivity(conn, aid))
            throw NotFoundException("Activity id not found")
    }

    /**
     * Validates if a limit is between the given [range].
     *
     * @param limit limit to be validated
     * @param range range of valid values
     *
     * @throws InvalidArgumentException if [limit] is not between [range]
     */
    protected fun validateLimit(limit: Int, range: IntRange) {
        if (limit !in range)
            throw InvalidArgumentException("Limit must be between $range")
    }

    /**
     * Validates skip.
     *
     * @param skip skip to be validated
     *
     * @throws InvalidArgumentException if [skip] is invalid
     */
    protected fun validateSkip(skip: Int) {
        if (skip < 0)
            throw InvalidArgumentException("Skip must be higher or equal to 0")
    }
}
