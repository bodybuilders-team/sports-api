package pt.isel.ls.sports.data

import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.SportsError
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Represents the Memory implementation of the Sports API database
 */
class SportsDataMem : SportsDatabase {
    val tokens = ConcurrentHashMap<String, Int>()

    val users = ConcurrentHashMap<Int, User>()
    private var usersLastValue: AtomicInteger = AtomicInteger(1)

    val routes = ConcurrentHashMap<Int, Route>()
    private var routesLastValue: AtomicInteger = AtomicInteger(1)

    val sports = ConcurrentHashMap<Int, Sport>()
    private var sportsLastValue: AtomicInteger = AtomicInteger(1)

    val activities = ConcurrentHashMap<Int, Activity>()
    private var activitiesLastValue: AtomicInteger = AtomicInteger(1)

    /**
     * Creates a new user in the database.
     *
     * @param name user's name
     * @param email user's email
     *
     * @return user's unique identifier
     */
    override fun createNewUser(name: String, email: String): Int {
        val id = usersLastValue.getAndIncrement()

        check(!users.containsKey(id)) { "Serial ID already exists" }

        if (users.values.any { it.email == email })
            throw SportsError.conflict("Email already in use")

        users[id] = User(id, name, email)

        return id
    }

    /**
     * Gets the user object.
     *
     * @param uid user's identifier
     *
     * @return user object
     */
    override fun getUser(uid: Int): User =
        users[uid]
            ?: throw SportsError.notFound("User with id $uid not found")

    override fun hasUserWithEmail(email: String): Boolean =
        users.values.any { it.email == email }

    override fun hasUser(uid: Int): Boolean =
        users.containsKey(uid)

    override fun hasSport(sid: Int): Boolean =
        sports.containsKey(sid)

    override fun hasRoute(rid: Int): Boolean =
        routes.containsKey(rid)

    override fun hasActivity(aid: Int): Boolean =
        activities.containsKey(aid)

    /**
     * Get the list of users.
     *
     * @return list of user objects
     */
    override fun getAllUsers(): List<User> {
        return users.values.toList()
    }

    /**
     * Associates a user [token] with the [uid].
     *
     * @param token user token
     * @param uid user's identifier
     *
     * @return user's token
     */
    override fun createUserToken(token: UUID, uid: Int): String {
        val stringToken = token.toString()
        tokens[stringToken] = uid
        return stringToken
    }

    /**
     * Gets the uid associated with the [token].
     *
     * @param token user's token
     *
     * @return uid
     */
    override fun getUID(token: String): Int {
        return tokens[token] ?: throw SportsError.notFound("Token $token isn't associated to any user")
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
        val id = routesLastValue.getAndIncrement()

        if (users[uid] == null) throw SportsError.notFound("User with id $uid not found")

        routes[id] =
            Route(id, start_location = startLocation, end_location = endLocation, distance / 1000.0, uid)

        return id
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
    override fun createNewSport(uid: Int, name: String, description: String?): Int {
        val id = sportsLastValue.getAndIncrement()

        if (users[uid] == null) throw SportsError.notFound("User with id $uid not found")

        sports[id] = Sport(id = id, name, uid, description)

        return id
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
    override fun createNewActivity(uid: Int, date: String, duration: String, sid: Int, rid: Int?): Int {
        val id = activitiesLastValue.getAndIncrement()

        activities[id] = Activity(id = id, date, duration, uid, sid, rid)

        return id
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
    override fun getActivities(sid: Int, orderBy: SortOrder, date: String?, rid: Int?, skip: Int?, limit: Int?) =
        activities
            .filter {
                it.value.sid == sid &&
                    it.value.date == date &&
                    it.value.rid == rid
            }
            .values.toList()
            .sortedWith(
                if (orderBy == SortOrder.ASCENDING)
                    compareBy { it.duration }
                else
                    compareByDescending { it.duration }
            )
}
