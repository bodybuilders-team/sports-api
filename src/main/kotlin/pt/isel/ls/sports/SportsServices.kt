package pt.isel.ls.sports

import pt.isel.ls.sports.api.routers.users.CreateUserResponse
import pt.isel.ls.sports.data.SortOrder
import pt.isel.ls.sports.data.SportsDatabase
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError

class SportsServices(private val db: SportsDatabase) {

    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return a createUser response (user's token and the user's unique identifier)
     */
    fun createNewUser(name: String, email: String): CreateUserResponse {
        val uid = db.createNewUser(name, email)
        val token = db.createUserToken(uid)

        return CreateUserResponse(token, uid)
    }

    /**
     * Gets the user identified by [uid].
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(uid: Int): User {
        return db.getUser(uid)
    }

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    fun getAllUsers(): List<User> {
        return db.getAllUsers()
    }

    /**
     * Creates a new route.
     *
     * The [distance] is converted from km to m (meters).
     *
     * @param token user's token
     * @param startLocation
     * @param endLocation
     * @param distance distance of the route in km
     *
     *
     * @return the route's unique identifier
     */
    fun createNewRoute(token: String, startLocation: String, endLocation: String, distance: Double): Int {
        val uid = authenticate(token)

        return db.createNewRoute(
            startLocation,
            endLocation,
            distance = (distance * 1000).toInt(),
            uid
        )
    }

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     *
     * @return the route object
     */
    fun getRoute(rid: Int): Route {
        return db.getRoute(rid)
    }

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    fun getAllRoutes(): List<Route> {
        return db.getAllRoutes()
    }

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
        val uid = authenticate(token)

        return db.createNewSport(name, description ?: "", uid)
    }

    /**
     * Get a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    fun getSport(sid: Int): Sport {
        return db.getSport(sid)
    }

    /**
     * Get the list of all sports.
     *
     * @return list of identifiers of all sports
     */
    fun getAllSports(): List<Sport> {
        return db.getAllSports()
    }

    /**
     * Create a new activity.
     *
     * @param token user's token
     * @param date
     * @param duration
     * @param sid sport's unique identifier
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
    fun createNewActivity(token: String, date: String, duration: String, sid: Int, rid: Int?): Int {
        val uid = authenticate(token)

        return db.createNewActivity(date, duration, uid, sid, rid)
    }

    /**
     * Get an activity.
     *
     * @param aid activity's unique identifier
     *
     * @return the activity object
     */
    fun getActivity(aid: Int): Activity {
        return db.getActivity(aid)
    }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(aid: Int) {
        return db.deleteActivity(aid)
    }

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of activities of a sport
     */
    fun getSportActivities(sid: Int): List<Activity> {
        return db.getSportActivities(sid)
    }

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of activities made from a user
     */
    fun getUserActivities(uid: Int): List<Activity> {
        return db.getUserActivities(uid)
    }

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     * @param limit limits the number of results returned (optional)
     * @param skip skips the number of results provided (optional)
     *
     * @return list of activities identifiers
     */
    fun getActivities(sid: Int, orderBy: String, date: String?, rid: Int?, limit: Int?, skip: Int?): List<Activity> {
        val order =
            if (orderBy == "ascending")
                SortOrder.ASCENDING
            else
                SortOrder.DESCENDING

        return db.getActivities(sid, order, date, rid)
            .run { return@run subList(fromIndex = skip ?: 0, toIndex = (limit ?: lastIndex) + 1) }
    }

    /**
     * Gets the user's unique identifier associate with the [token]
     *
     * @param token user token
     *
     * @return user's unique identifier associated with the [token]
     *
     * @throws SportsError.invalidCredentials if a user with the [token] was not found
     */
    private fun authenticate(token: String) = runCatching {
        db.getUID(token)
    }.getOrElse { throw SportsError.invalidCredentials() }
}
