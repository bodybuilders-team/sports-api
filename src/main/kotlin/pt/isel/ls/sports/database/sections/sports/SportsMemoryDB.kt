package pt.isel.ls.sports.database.sections.sports

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.Sport

class SportsMemoryDB(private val source: AppMemoryDBSource) : SportsDB {

    override fun createNewSport(conn: ConnectionDB, uid: Int, name: String, description: String?): Int {
        val id = source.nextSportId.getAndIncrement()

        source.users[uid] ?: throw NotFoundException("User with id $uid not found")

        source.sports[id] = Sport(id, name, uid, description)

        return id
    }

    override fun getSport(conn: ConnectionDB, sid: Int): Sport =
        source.sports[sid] ?: throw NotFoundException("Sport with id $sid not found")

    override fun getAllSports(
        conn: ConnectionDB,
        skip: Int,
        limit: Int
    ): SportsResponse =
        SportsResponse(
            sports = source.sports
                .values.toList()
                .run { subList(skip, if (lastIndex + 1 < limit) lastIndex + 1 else limit) },
            totalCount = 0
        )

    override fun hasSport(conn: ConnectionDB, sid: Int): Boolean =
        source.sports.containsKey(sid)
}
