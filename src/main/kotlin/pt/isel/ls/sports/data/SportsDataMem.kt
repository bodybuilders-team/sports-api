package pt.isel.ls.sports.data

import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class SportsDataMem : SportsDatabase {
    private val userTokens = ConcurrentHashMap<String, Int>()

    val users = ConcurrentHashMap<Int, User>()
    private var usersLastValue: AtomicInteger = AtomicInteger(0)

    val routes = ConcurrentHashMap<Int, Route>()
    private var routesLastValue: AtomicInteger = AtomicInteger(0)

    val sports = ConcurrentHashMap<Int, Sport>()
    private var sportsLastValue: AtomicInteger = AtomicInteger(0)

    val activities = ConcurrentHashMap<Int, Activity>()
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
        return users[uid] ?: throw SportsError.notFound("User with id $uid not found")
    }

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    override fun getAllUsers(): List<User> {
        return users.values.toList()
    }

    /**
     * Creates a user token and associates it with the [uid].
     *
     * @param uid user's identifier
     *
     * @return user's token
     */
    override fun createUserToken(uid: Int): String {
        val token = UUID.randomUUID().toString()
        userTokens[token] = uid
        return token
    }

    /**
     * Gets the uid associated with the [token].
     *
     * @param token user's token
     *
     * @return uid
     */
    override fun getUID(token: String): Int {
        return userTokens[token] ?: throw SportsError.notFound("User with the token $token not found")
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

        if (users[uid] == null) throw SportsError.notFound("User with id $uid not found") // TODO: 23/03/2022 this VS. getUser(uid)

        routes[identifier] =
            Route(id = identifier, start_location = startLocation, end_location = endLocation, distance, uid)

        return identifier
    }

    /**
     * Get the details of a route.
     *
     * @param rid route's unique identifier
     *
     * @return the route object
     */
    override fun getRoute(rid: Int): Route {
        return routes[rid] ?: throw SportsError.notFound("Route with id $rid not found")
    }

    /**
     * Get the list of routes.
     *
     * @return list of route objects
     */
    override fun getAllRoutes(): List<Route> {
        return routes.values.toList()
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

        if (users[uid] == null) throw SportsError.notFound("User with id $uid not found") // TODO: 23/03/2022 this VS. getUser(uid)

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
        return sports[sid] ?: throw SportsError.notFound("Sport with id $sid not found")
    }

    /**
     * Get the list of all sports.
     *
     * @return list of identifiers of all sports
     */
    override fun getAllSports(): List<Sport> {
        return sports.values.toList()
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
        return activities[aid] ?: throw SportsError.notFound("Activity with id $aid not found")
    }

    /**
     * Delete an activity.
     *
     * @param aid activity's unique identifier
     */
    override fun deleteActivity(aid: Int) {
        activities.remove(aid) ?: throw SportsError.notFound("Activity with id $aid not found")
    }

    /**
     * Get all the activities of a sport.
     *
     * @param sid sport's unique identifier
     *
     * @return list of activities of a sport
     */
    override fun getSportActivities(sid: Int): List<Activity> {
        return activities.filter { it.value.sid == sid }.values.toList()
    }

    /**
     * Get all the activities made from a user.
     *
     * @param uid user's unique identifier
     *
     * @return list of activities made from a user
     */
    override fun getUserActivities(uid: Int): List<Activity> {
        return activities.filter { it.value.uid == uid }.values.toList()
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
    override fun getActivities(sid: Int, orderBy: String, date: String?, rid: Int?): List<Activity> {
        // TODO: 23/03/2022 Check ifs
        // TODO: 23/03/2022 Order by
        return activities.filter {
            it.value.sid == sid &&
                (if (date != null) it.value.date == date else true) &&
                (if (rid != null) it.value.rid == rid else true)
        }.values.toList()
    }
}
