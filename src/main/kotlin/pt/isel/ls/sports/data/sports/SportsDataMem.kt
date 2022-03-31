package pt.isel.ls.sports.data.sports

import pt.isel.ls.sports.data.AppDataMemSource
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppError

class SportsDataMem(private val source: AppDataMemSource) : SportsDatabase {

    override fun createNewSport(uid: Int, name: String, description: String?): Int {
        val id = source.nextSportId.getAndIncrement()

        if (source.users[uid] == null) throw AppError.notFound("User with id $uid not found")

        source.sports[id] = Sport(id = id, name, uid, description)

        return id
    }

    override fun getSport(sid: Int): Sport {
        return source.sports[sid] ?: throw AppError.notFound("Sport with id $sid not found")
    }

    override fun getAllSports(): List<Sport> {
        return source.sports.values.toList()
    }

    override fun getSportActivities(sid: Int): List<Activity> {
        return source.activities.filter { it.value.sid == sid }.values.toList()
    }

    override fun hasSport(sid: Int): Boolean =
        source.sports.containsKey(sid)
}
