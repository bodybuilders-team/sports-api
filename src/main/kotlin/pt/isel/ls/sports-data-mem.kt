package pt.isel.ls

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


class Mem : DB {
    val userTokens: MutableMap<Int, String> = mutableMapOf()

    val users: ConcurrentHashMap<Int, User> = ConcurrentHashMap<Int, User>()
    var usersLastValue: AtomicInteger = AtomicInteger(0)

    val routes: MutableMap<Int, Route> = mutableMapOf()
    var routesLastValue: AtomicInteger = AtomicInteger(0)


    override fun createNewUser(name: String, email: String): Int {
        val identifier = usersLastValue.getAndIncrement()

        users[identifier] = User(id = identifier, name, email)

        return identifier
    }

    override fun getUser(uid: Int): User {
        return users[uid] ?: throw NotFoundException("User with id $uid not found")
    }

    override fun getUsers(): List<Int> {
        return users.keys.toList()
    }

    override fun createUserToken(uid: Int): String {
        //generateToken
        return "token"
    }


    override fun createNewRoute(userId: Int, startLocation: String, endLocation: String, distance: Float): Int {
        val identifier = routesLastValue.getAndIncrement()

        routes[identifier] = Route(id = identifier, startLocation, endLocation, distance, userId)

        return identifier
    }

    override fun getRoute(rid: Int): Route {
        return routes[rid] ?: throw NotFoundException("Route with id $rid not found")
    }

    override fun getListOfRoutes(): List<Int> {
        return routes.keys.toList()
    }

    override fun createNewSport(name: String, description: String) {
        TODO("Not yet implemented")
    }

    override fun getAllSports(): List<Int> {
        TODO("Not yet implemented")
    }

    override fun getSport(sportId: Int): String {
        TODO("Not yet implemented")
    }

    override fun createNewActivity(userId: Int, sid: Int, duration: String, date: String, rid: Int?): Int {
        TODO("Not yet implemented")
    }

    override fun getSportActivities(sid: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override fun getActivity(aid: Int): String {
        TODO("Not yet implemented")
    }

    override fun deleteActivity(aid: Int) {
        TODO("Not yet implemented")
    }

    override fun getUserActivities(uid: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override fun getActivities(sid: Int, orderBy: String, date: String, rid: Int): List<Int> {
        TODO("Not yet implemented")
    }
}
