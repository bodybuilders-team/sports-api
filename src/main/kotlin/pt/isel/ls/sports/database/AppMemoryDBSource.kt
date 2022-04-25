package pt.isel.ls.sports.database

import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Memory source shared by all memory database modules.
 *
 * @property tokens memory for tokens
 * @property users memory for users
 * @property nextUserId stores the next uid (user's unique identifier)
 * @property routes memory for routes
 * @property nextRouteId stores the next rid (route's unique identifier)
 * @property sports memory for sports
 * @property nextSportId stores the next sid (sport's unique identifier)
 * @property activities memory for activities
 * @property nextActivityId stores the next aid (activity's unique identifier)
 */
class AppMemoryDBSource {
    val tokens = ConcurrentHashMap<String, Int>()

    val users = ConcurrentHashMap<Int, User>()
    var nextUserId = AtomicInteger(1)

    val routes = ConcurrentHashMap<Int, Route>()
    var nextRouteId = AtomicInteger(1)

    val sports = ConcurrentHashMap<Int, Sport>()
    var nextSportId = AtomicInteger(1)

    val activities = ConcurrentHashMap<Int, Activity>()
    var nextActivityId = AtomicInteger(1)

    fun reset() {
        tokens.clear()

        users.clear()
        nextUserId = AtomicInteger(1)

        routes.clear()
        nextRouteId = AtomicInteger(1)

        sports.clear()
        nextSportId = AtomicInteger(1)

        activities.clear()
        nextActivityId = AtomicInteger(1)
    }
}
