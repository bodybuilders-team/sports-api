package pt.isel.ls.sports.services.sections.sports

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.sports.SportsResponse
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.exceptions.AuthenticationException
import pt.isel.ls.sports.services.exceptions.AuthorizationException

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
        validateName(name)
        if (description != null)
            validateDescription(description)

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
     * Searches for sports.
     *
     * @param skip number of elements to skip
     * @param limit number of elements to return
     * @param name search query (optional)
     *
     * @return [SportsResponse] with a list of sports
     */
    fun searchSports(skip: Int, limit: Int, name: String? = null): SportsResponse {
        validateSkip(skip)
        validateLimit(limit, LIMIT_RANGE)

        return db.execute { conn ->
            db.sports.searchSports(conn, skip, limit, name)
        }
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

    /**
     * Updates a sport.
     *
     * @param sid sports unique identifier
     * @param name name of the sport
     * @param description description of the sport (optional)
     *
     * @return true if the sport was modified, false otherwise
     * @throws NotFoundException if there's no sport with the [sid]
     * @throws InvalidArgumentException if name or description are invalid
     * @throws AuthorizationException if the user is not the sport creater
     */
    fun updateSport(sid: Int, token: String, name: String?, description: String?): Boolean {
        validateSid(sid)

        if (name != null)
            validateName(name)

        if (description != null)
            validateDescription(description)

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            val sport = db.sports.getSport(conn, sid)
            if (sport.uid != uid)
                throw AuthorizationException("You are not allowed to update this sport.")

            db.sports.updateSport(conn, sid, name, description)
        }
    }

    companion object {
        private val LIMIT_RANGE = 0..100

        /**
         * Validates a sport name.
         *
         * @param name name of the sport
         * @throws InvalidArgumentException if the sport name is invalid
         */
        fun validateName(name: String) {
            if (!Sport.isValidName(name))
                throw InvalidArgumentException(
                    "Name must be between ${Sport.MIN_NAME_LENGTH} and ${Sport.MAX_NAME_LENGTH} characters"
                )
        }

        /**
         * Validates a sport description.
         *
         * @param description description of the sport
         * @throws InvalidArgumentException if the sport description is invalid
         */
        fun validateDescription(description: String) {
            if (!Sport.isValidDescription(description))
                throw InvalidArgumentException(
                    "Description must be between ${Sport.MIN_DESCRIPTION_LENGTH} and ${Sport.MAX_DESCRIPTION_LENGTH} characters"
                )
        }
    }
}
