package pt.isel.ls.sports.data

import pt.isel.ls.sports.Activity
import pt.isel.ls.sports.Route
import pt.isel.ls.sports.Sport
import pt.isel.ls.sports.User


/**
 * Sports API database representation.
 */
interface SportsDatabase {
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
	 * Gets the user object.
	 *
	 * @param uid user's identifier
	 *
	 * @return user object
	 */
	fun getUser(uid: Int): User

	/**
	 * Get the list of users.
	 *
	 * @return list of user identifiers
	 */
	fun getUsers(): List<Int>

	/**
	 * Creates a user token.
	 *
	 * @param uid user's unique identifier
	 *
	 * @return user's token
	 */
	fun createUserToken(uid: Int): String

	//----------- ROUTE MANAGEMENT -----------

	/**
	 * Creates a new route.
	 *
	 * @param uid user's unique identifier
	 * @param startLocation
	 * @param endLocation
	 * @param distance
	 *
	 * @return the route's unique identifier
	 */
	fun createNewRoute(uid: Int, startLocation: String, endLocation: String, distance: Int): Int

	/**
	 * Get the details of a route.
	 *
	 * @param rid route's unique identifier
	 */
	fun getRoute(rid: Int): Route

	/**
	 * Get the list of routes.
	 *
	 * @return list of route identifiers
	 */
	fun getListOfRoutes(): List<Int>

	// ------------ Sports and Activities Management ------------

	/**
	 * Create a new sport.
	 *
	 * @param name the sport's name
	 * @param description the sport's description
	 *
	 * @return the sport's unique identifier
	 */
	fun createNewSport(name: String, description: String)

	/**
	 * Get the list of all sports.
	 *
	 * @return list of identifiers of all sports
	 */
	fun getAllSports(): List<Int>

	/**
	 * Get a sport.
	 *
	 * @param sportId sport's unique identifier
	 *
	 * @return the sport object
	 */
	fun getSport(sportId: Int): Sport

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
	fun createNewActivity(uid: Int, sid: String, duration: String, date: String, rid: Int?): Int

	/**
	 * Get all the activities of a sport.
	 *
	 * @param sid sport's unique identifier
	 *
	 * @return list of identifiers of activities of a sport
	 */
	fun getSportActivities(sid: Int): List<Int>

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
	 * Get all the activities made from a user.
	 *
	 * @param uid user's unique identifier
	 *
	 * @return list of identifiers of activities made from a user
	 */
	fun getUserActivities(uid: Int): List<Int>

	/**
	 * Get a list with the activities, given the parameters.
	 *
	 * @param sid sport's identifier
	 * @param orderBy order by duration time, only has two possible values - "ascending" or "descending"
	 * @param date activity date
	 * @param rid route's unique identifier
	 *
	 * @return list of activities identifiers
	 */
	fun getActivities(sid: Int, orderBy: String, date: String, rid: Int): List<Int>
}
