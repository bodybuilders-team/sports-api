package pt.isel.ls.sports.services.sections.sports

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.sports.SportsResponse
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.InvalidArgumentException

/**
 * Sports services. Implements methods regarding sports.
 */
class SportsServices(db: AppDB) : AbstractServices(db) {
    /**
     * Creates a new sport.
     *
     * @param token user's token
     * @param name name of the sport
     * @param description description of the sport
     *
     * @return sport's unique identifier
     * @throws InvalidArgumentException if the name is invalid
     * @throws InvalidArgumentException if the description is invalid
     * @throws AuthenticationException if a user with the [token] was not found
     */
    fun createNewSport(token: String, name: String, description: String?): Int {
        if (!Sport.isValidName(name))
            throw InvalidArgumentException("Name must be between ${Sport.MIN_NAME_LENGTH} and ${Sport.MAX_NAME_LENGTH} characters")

        if (description != null && !Sport.isValidDescription(description))
            throw InvalidArgumentException("Description must be between ${Sport.MIN_DESCRIPTION_LENGTH} and ${Sport.MAX_DESCRIPTION_LENGTH} characters")

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            db.sports.createNewSport(conn, uid, name, description)
        }
    }

    /**
     * Gets a specific sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     * @throws InvalidArgumentException if [sid] is negative
     * @throws NotFoundException if there's no sport with the [sid]
     */
    fun getSport(sid: Int): Sport {
        validateSid(sid)

        return db.execute { conn ->
            db.sports.getSport(conn, sid)
        }
    }

    /**
     * Gets all sports.
     *
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [SportsResponse] with the list of sports
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getAllSports(skip: Int, limit: Int): SportsResponse = db.execute { conn ->
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        db.sports.getAllSports(conn, skip, limit)
    }

    /**
     * Gets all activities of a specific sport.
     *
     * @param sid sport's unique identifier
     * @param skip number of elements to skip
     * @param limit number of elements to return
     *
     * @return [ActivitiesResponse] with the list of activities
     * @throws InvalidArgumentException if [sid] is negative
     * @throws InvalidArgumentException if [skip] is invalid
     * @throws InvalidArgumentException if [limit] is invalid
     */
    fun getSportActivities(sid: Int, skip: Int, limit: Int): ActivitiesResponse {
        validateSid(sid)
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.activities.getSportActivities(conn, sid, skip, limit)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100
    }
}
