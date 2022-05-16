package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.InvalidArgumentException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.Sport

/**
 * Sports database representation using memory.
 */
class SportsMemoryDB(private val source: AppMemoryDBSource) : SportsDB {

    override fun createNewSport(conn: ConnectionDB, uid: Int, name: String, description: String?): Int {
        val id = source.nextSportId.getAndIncrement()

        source.users[uid] ?: throw NotFoundException("User with id $uid not found")

        source.sports[id] = Sport(id, name, uid, description)

        return id
    }

    override fun updateSport(conn: ConnectionDB, sid: Int, name: String?, description: String?): Boolean {
        val prevSport = source.sports[sid] ?: throw NotFoundException("Sport not found.")

        if (name == null && description == null)
            throw InvalidArgumentException("Name or description must be specified.")

        val newSport = prevSport.copy(
            name = name ?: prevSport.name,
            description = description ?: prevSport.description
        )
        source.sports[sid] = newSport

        return prevSport != newSport
    }

    override fun getSport(conn: ConnectionDB, sid: Int): Sport =
        source.sports[sid] ?: throw NotFoundException("Sport with id $sid not found")

    override fun searchSports(conn: ConnectionDB, skip: Int, limit: Int, name: String?): SportsResponse {
        val sports = source.sports.values.toList().run {
            if (name != null)
                this.filter { it.name.contains(name, true) }
            else
                this
        }

        return sports.run {
            SportsResponse(subList(skip, if (lastIndex + 1 < limit) lastIndex + 1 else limit), size)
        }
    }

    override fun hasSport(conn: ConnectionDB, sid: Int): Boolean =
        source.sports.containsKey(sid)
}
