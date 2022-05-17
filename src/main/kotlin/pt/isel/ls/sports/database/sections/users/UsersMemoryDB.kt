package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.User

/**
 * Users database representation using memory.
 */
class UsersMemoryDB(private val source: AppMemoryDBSource) : UsersDB {

    override fun createNewUser(conn: ConnectionDB, name: String, email: String, hashedPassword: String): Int {
        val id = source.nextUserId.getAndIncrement()

        if (hasUserWithEmail(conn, email))
            throw AlreadyExistsException("Email already in use")

        source.users[id] = User(id, name, email, hashedPassword)

        return id
    }

    override fun getUser(conn: ConnectionDB, uid: Int): User =
        source.users[uid] ?: throw NotFoundException("User with id $uid not found")

    override fun getAllUsers(conn: ConnectionDB, skip: Int, limit: Int): UsersResponse =
        UsersResponse(
            users = source.users
                .values.toList()
                .run { subList(skip, if (lastIndex + 1 < limit) lastIndex + 1 else limit) },
            totalCount = source.users.size
        )

    override fun hasUserWithEmail(conn: ConnectionDB, email: String): Boolean =
        source.users.values.any { it.email == email }

    override fun hasUser(conn: ConnectionDB, uid: Int): Boolean =
        source.users.containsKey(uid)
}
