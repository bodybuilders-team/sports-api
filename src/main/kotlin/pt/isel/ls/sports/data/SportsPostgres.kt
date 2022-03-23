package pt.isel.ls.sports.data

import pt.isel.ls.sports.Activity
import pt.isel.ls.sports.Route
import pt.isel.ls.sports.Sport
import pt.isel.ls.sports.User


class SportsPostgres : SportsDatabase {
	/**
	 * Creates a new user in the database.
	 *
	 * @param name user's name
	 * @param email user's email
	 *
	 * @return user's unique identifier
	 */
	override fun createNewUser(name: String, email: String): Int {
		TODO("Not yet implemented")
	}

	/**
	 * Gets the user object.
	 *
	 * @param uid user's identifier
	 *
	 * @return user object
	 */
	override fun getUser(uid: Int): User {
		TODO("Not yet implemented")
	}

	/**
	 * Get the list of users.
	 *
	 * @return list of user identifiers
	 */
	override fun getUsers(): List<Int> {
		TODO("Not yet implemented")
	}

	/**
	 * Creates a user token.
	 *
	 * @param uid user's unique identifier
	 *
	 * @return user's token
	 */
	override fun createUserToken(uid: Int): String {
		TODO("Not yet implemented")
	}

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
	override fun createNewRoute(uid: Int, startLocation: String, endLocation: String, distance: Int): Int {
		TODO("Not yet implemented")
	}

	/**
	 * Get the details of a route.
	 *
	 * @param rid route's unique identifier
	 */
	override fun getRoute(rid: Int): Route {
		TODO("Not yet implemented")
	}

	/**
	 * Get the list of routes.
	 *
	 * @return list of route identifiers
	 */
	override fun getListOfRoutes(): List<Int> {
		TODO("Not yet implemented")
	}

	/**
	 * Create a new sport.
	 *
	 * @param name the sport's name
	 * @param description the sport's description
	 *
	 * @return the sport's unique identifier
	 */
	override fun createNewSport(name: String, description: String) {
		TODO("Not yet implemented")
	}

	/**
	 * Get the list of all sports.
	 *
	 * @return list of identifiers of all sports
	 */
	override fun getAllSports(): List<Int> {
		TODO("Not yet implemented")
	}

	/**
	 * Get a sport.
	 *
	 * @param sportId sport's unique identifier
	 *
	 * @return the sport object
	 */
	override fun getSport(sportId: Int): Sport {
		TODO("Not yet implemented")
	}

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
	override fun createNewActivity(uid: Int, sid: String, duration: String, date: String, rid: Int?): Int {
		TODO("Not yet implemented")
	}

	/**
	 * Get all the activities of a sport.
	 *
	 * @param sid sport's unique identifier
	 *
	 * @return list of identifiers of activities of a sport
	 */
	override fun getSportActivities(sid: Int): List<Int> {
		TODO("Not yet implemented")
	}

	/**
	 * Get the detailed information of an activity.
	 *
	 * @param aid activity's unique identifier
	 */
	override fun getActivity(aid: Int): Activity {
		TODO("Not yet implemented")
	}

	/**
	 * Delete an activity.
	 *
	 * @param aid activity's unique identifier
	 */
	override fun deleteActivity(aid: Int) {
		TODO("Not yet implemented")
	}

	/**
	 * Get all the activities made from a user.
	 *
	 * @param uid user's unique identifier
	 *
	 * @return list of identifiers of activities made from a user
	 */
	override fun getUserActivities(uid: Int): List<Int> {
		TODO("Not yet implemented")
	}

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
	override fun getActivities(sid: Int, orderBy: String, date: String, rid: Int): List<Int> {
		TODO("Not yet implemented")
	}

}
