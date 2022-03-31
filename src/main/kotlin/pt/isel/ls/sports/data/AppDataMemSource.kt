package pt.isel.ls.sports.data

import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.domain.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Source (memory) shared by all DataMem modules.
 *
 * @property tokens memory for tokens
 * @property users memory for users
 * @property nextUserId stores the next uid (user id)
 * @property routes memory for routes
 * @property nextRouteId stores the next rid (route id)
 * @property sports memory for sports
 * @property nextSportId stores the next sid (sport id)
 * @property activities memory for activities
 * @property nextActivityId stores the next aid (activity id)
 */
class AppDataMemSource {
    val tokens = ConcurrentHashMap<String, Int>()

    val users = ConcurrentHashMap<Int, User>()
    var nextUserId = AtomicInteger(1)

    val routes = ConcurrentHashMap<Int, Route>()
    var nextRouteId = AtomicInteger(1)

    val sports = ConcurrentHashMap<Int, Sport>()
    var nextSportId = AtomicInteger(1)

    val activities = ConcurrentHashMap<Int, Activity>()
    var nextActivityId = AtomicInteger(1)
}
