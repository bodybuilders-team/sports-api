package pt.isel.ls.sports.services.sections

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.AbstractServices

class SportsServices(db: AppDB) : AbstractServices(db) {
    /**
     * Create a new sport.
     *
     * @param token user's token
     * @param name the sport's name
     * @param description the sport's description
     *
     * @return the sport's unique identifier
     */
    fun createNewSport(token: String, name: String, description: String?): Int {
        if (!Sport.isValidName(name))
            throw AppException.InvalidArgument("Name must be between ${Sport.MIN_NAME_LENGTH} and ${Sport.MAX_NAME_LENGTH} characters")

        if (description != null && !Sport.isValidDescription(description))
            throw AppException.InvalidArgument("Description must be between ${Sport.MIN_DESCRIPTION_LENGTH} and ${Sport.MAX_DESCRIPTION_LENGTH} characters")

        return db.execute { conn ->
            val uid = authenticate(conn, token)

            db.sports.createNewSport(conn, uid, name, description)
        }
    }

    /**
     * Get a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    fun getSport(sid: Int): Sport {
        validateSid(sid)

        return db.execute { conn ->
            db.sports.getSport(conn, sid)
        }
    }

    /**
     * Get the list of all sports.
     *
     * @return list of identifiers of all sports
     */
    fun getAllSports(): List<Sport> = db.execute { conn ->
        db.sports.getAllSports(conn)
    }

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of activities of a sport
     */
    fun getSportActivities(sid: Int): List<Activity> {
        validateSid(sid)

        return db.execute { conn ->
            db.activities.getSportActivities(conn, sid)
        }
    }
}
