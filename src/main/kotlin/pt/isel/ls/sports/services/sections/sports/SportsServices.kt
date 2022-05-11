package pt.isel.ls.sports.services.sections.sports

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.database.InvalidArgumentException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.sections.activities.ActivitiesResponse
import pt.isel.ls.sports.database.sections.sports.SportsResponse
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.services.AbstractServices
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.AuthorizationException

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

        fun validateName(name: String) {
            if (!Sport.isValidName(name))
                throw InvalidArgumentException("Name must be between ${Sport.MIN_NAME_LENGTH} and ${Sport.MAX_NAME_LENGTH} characters")
        }

        fun validateDescription(description: String) {
            if (!Sport.isValidDescription(description))
                throw InvalidArgumentException("Description must be between ${Sport.MIN_DESCRIPTION_LENGTH} and ${Sport.MAX_DESCRIPTION_LENGTH} characters")
        }
    }
}
