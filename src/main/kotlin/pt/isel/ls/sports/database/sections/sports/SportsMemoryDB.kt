package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.memory.AppMemoryDBSource
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.errors.AppError

class SportsMemoryDB(private val source: AppMemoryDBSource) : SportsDB {

    override fun createNewSport(uid: Int, name: String, description: String?): Int {
        val id = source.nextSportId.getAndIncrement()

        if (source.users[uid] == null) throw AppError.NotFound("User with id $uid not found")

        source.sports[id] = Sport(id = id, name, uid, description)

        return id
    }

    override fun getSport(sid: Int): Sport {
        return source.sports[sid] ?: throw AppError.NotFound("Sport with id $sid not found")
    }

    override fun getAllSports(): List<Sport> {
        return source.sports.values.toList()
    }

    override fun hasSport(sid: Int): Boolean =
        source.sports.containsKey(sid)
}
