package pt.isel.ls.sports.data

import pt.isel.ls.sports.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


class SportsDataMem : SportsDatabase {
	private val userTokens: MutableMap<Int, String> = mutableMapOf()

	val users: ConcurrentHashMap<Int, User> = ConcurrentHashMap<Int, User>()
	private var usersLastValue: AtomicInteger = AtomicInteger(0)

	val routes: MutableMap<Int, Route> = mutableMapOf()
	private var routesLastValue: AtomicInteger = AtomicInteger(0)

	val sports: MutableMap<Int, Sport> = mutableMapOf()
	private var sportsLastValue: AtomicInteger = AtomicInteger(0)

	val activities: MutableMap<Int, Activity> = mutableMapOf()
	private var activitiesLastValue: AtomicInteger = AtomicInteger(0)


	/**
	 * Creates a new user in the database.
	 *
	 * @param name user's name
	 * @param email user's email
	 *
	 * @return user's unique identifier
	 */
	override fun createNewUser(name: String, email: String): Int {
		val identifier = usersLastValue.getAndIncrement()

		users[identifier] = User(id = identifier, name, email)

		return identifier
	}

	/**
	 * Gets the user object.
	 *
	 * @param uid user's identifier
	 *
	 * @return user object
	 */
	override fun getUser(uid: Int): User {
		return users[uid] ?: throw NotFoundException("User with id $uid not found")
	}

	/**
	 * Get the list of users.
	 *
	 * @return list of user identifiers
	 */
	override fun getAllUsers(): List<Int> {
		return users.keys.toList()
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
	override fun createNewRoute(startLocation: String, endLocation: String, distance: Int, uid: Int): Int {
		val identifier = routesLastValue.getAndIncrement()

		if (users[uid] == null) throw NotFoundException("User with id $uid not found") // TODO: 23/03/2022 this VS. getUser(uid)

		routes[identifier] =
			Route(id = identifier, start_location = startLocation, end_location = endLocation, distance, uid)

		return identifier
	}

	/**
	 * Get the details of a route.
	 *
	 * @param rid route's unique identifier
	 *
	 * @return the route
	 */
	override fun getRoute(rid: Int): Route {
		return routes[rid] ?: throw NotFoundException("Route with id $rid not found")
	}

	/**
	 * Get the list of routes.
	 *
	 * @return list of route identifiers
	 */
	override fun getAllRoutes(): List<Int> {
		return routes.keys.toList()
	}

	/**
	 * Create a new sport.
	 *
	 * @param name the sport's name
	 * @param description the sport's description
	 * @param uid user's unique identifier
	 *
	 * @return the sport's unique identifier
	 */
	override fun createNewSport(name: String, description: String, uid: Int): Int {
		val identifier = sportsLastValue.getAndIncrement()

		if (users[uid] == null) throw NotFoundException("User with id $uid not found") // TODO: 23/03/2022 this VS. getUser(uid)

		sports[identifier] = Sport(id = identifier, name, description, uid)

		return identifier
	}

	/**
	 * Get a sport.
	 *
	 * @param sid sport's unique identifier
	 *
	 * @return the sport object
	 */
	override fun getSport(sid: Int): Sport {
		return sports[sid] ?: throw NotFoundException("Sport with id $sid not found")
	}

	/**
	 * Get the list of all sports.
	 *
	 * @return list of identifiers of all sports
	 */
	override fun getAllSports(): List<Int> {
		return sports.keys.toList()
	}

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
	override fun createNewActivity(date: String, duration: String, uid: Int, sid: Int, rid: Int?): Int {
		val identifier = activitiesLastValue.getAndIncrement()

		activities[identifier] = Activity(id = identifier, date, duration, uid, sid, rid)

		return identifier
	}

	/**
	 * Get an activity.
	 *
	 * @param aid activity's unique identifier
	 *
	 * @return the activity object
	 */
	override fun getActivity(aid: Int): Activity {
		return activities[aid] ?: throw NotFoundException("Activity with id $aid not found")
	}

	/**
	 * Delete an activity.
	 *
	 * @param aid activity's unique identifier
	 */
	override fun deleteActivity(aid: Int) {
		activities.remove(aid) ?: throw NotFoundException("Activity with id $aid not found")
	}

	/**
	 * Get all the activities of a sport.
	 *
	 * @param sid sport's unique identifier
	 *
	 * @return list of identifiers of activities of a sport
	 */
	override fun getSportActivities(sid: Int): List<Int> {
		return activities.filter { it.value.sid == sid }.keys.toList()
	}

	/**
	 * Get all the activities made from a user.
	 *
	 * @param uid user's unique identifier
	 *
	 * @return list of identifiers of activities made from a user
	 */
	override fun getUserActivities(uid: Int): List<Int> {
		return activities.filter { it.value.uid == uid }.keys.toList()
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
	override fun getActivities(sid: Int, orderBy: String, date: String?, rid: Int?): List<Int> {
		// TODO: 23/03/2022 Check ifs
		// TODO: 23/03/2022 Order by
		return activities.filter {
			it.value.sid == sid &&
					(if (date != null) it.value.date == date else true) &&
					(if (rid != null) it.value.rid == rid else true)
		}.keys.toList()
	}
}
