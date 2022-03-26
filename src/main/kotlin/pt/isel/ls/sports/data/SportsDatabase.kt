package pt.isel.ls.sports.data

import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User

/**
 * Sports API database representation.
 */
interface SportsDatabase {

    // ------------ Users ------------

    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
    fun createNewUser(name: String, email: String): Int

    /**
     * Gets the user identified by [uid].
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    fun getUser(uid: Int): User

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    fun getAllUsers(): List<User>

    // ----------- User Tokens -----------

    /**
     * Creates a user token and associates it with the [uid].
     *
     * @param uid user's identifier
     *
     * @return user's token
     */
    fun createUserToken(uid: Int): String

    /**
     * Gets the uid associated with the [token].
     *
     * @param token user's token
     *
     * @return uid
     */
    fun getUID(token: String): Int

    // ----------- Routes -----------

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
    fun createNewRoute(startLocation: String, endLocation: String, distance: Int, uid: Int): Int

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     */
    fun getRoute(rid: Int): Route

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    fun getAllRoutes(): List<Route>

    // ------------ Sports ------------

    /**
     * Create a new sport.
     *
     * @param name the sport's name
     * @param description the sport's description
     * @param uid user's unique identifier
     *
     * @return the sport's unique identifier
     */
    fun createNewSport(name: String, description: String, uid: Int): Int

    /**
     * Get a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return the sport object
     */
    fun getSport(sid: Int): Sport

    /**
     * Get the list of all sports.
     *
     * @return list of sport objects
     */
    fun getAllSports(): List<Sport>

    // ------------ Activities ------------

    /**
     * Create a new activity.
     *
     * @param uid user's unique identifier
     * @param sid sport's unique identifier
     * @param duration
     * @param date
     * @param rid route's unique identifier
     *
     * @return activity's unique identifier
     */
    fun createNewActivity(date: String, duration: String, uid: Int, sid: Int, rid: Int?): Int

    /**
     * Get the detailed information of an activity.
     *
     * @param aid activity's unique identifier
     */
    fun getActivity(aid: Int): Activity

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    fun deleteActivity(aid: Int)

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of identifiers of activities of a sport
     */
    fun getSportActivities(sid: Int): List<Activity>

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of identifiers of activities made from a user
     */
    fun getUserActivities(uid: Int): List<Activity>

    /**
     * Get a list with the activities, given the parameters.
     *
     * @param sid sport's identifier
     * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
     * @param date activity date (optional)
     * @param rid route's unique identifier (optional)
     *
     * @return list of activities
     */
    fun getActivities(sid: Int, orderBy: String, date: String?, rid: Int?): List<Activity>
    // TODO: 26/03/2022 Maybe change orderBy type to Boolean or Enum
}