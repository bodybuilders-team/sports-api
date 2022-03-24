package pt.isel.ls.sports

import pt.isel.ls.sports.data.SportsDatabase

class SportsServices(private val db: SportsDatabase) {
    fun createUser(token: String, name: String, email: String): Int {
        TODO("Not yet implemented")
    }

    fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

    fun getUser(uid: Int): User {
        TODO("Not yet implemented")
    }

    fun getActivitiesMadeByUser(uid: Int): List<Activity> {
        TODO("Not yet implemented")
    }

    fun createRoute(token: String, startLocation: String, endLocation: String, distance: Double): Int {
        TODO("Not yet implemented")
    }

    fun getRoutes(): List<Route> {
        TODO("Not yet implemented")
    }

    fun getRoute(rid: Int): Route {
        TODO("Not yet implemented")
    }

    fun createSport(token: String, name: String, description: String?, uid: Int): Int {
        TODO("Not yet implemented")
    }

    fun getSports(): List<Sport> {
        TODO("Not yet implemented")
    }

    fun getSport(sid: Int): Sport {
        TODO("Not yet implemented")
    }

    fun getActivitiesOfSport(sid: Int): Activity {
        TODO("Not yet implemented")
    }

    fun createActivity(token: String, date: String, duration: String, sid: Int, rid: Int?): Int {
        TODO("Not yet implemented")
    }

    fun getActivity(aid: Int): Activity {
        TODO("Not yet implemented")
    }

    fun deleteActivity(aid: Int) {
        TODO("Not yet implemented")
    }

    fun searchActivities(sid: Int, orderBy: Int, date: String?, rid: String?): List<Activity> {
        TODO("Not yet implemented")
    }

}