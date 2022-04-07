package pt.isel.ls.sports.database.sections.users

import pt.isel.ls.sports.database.AppMemoryDBSource
import pt.isel.ls.sports.database.connection.ConnectionDB
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError

class UsersMemoryDB(private val source: AppMemoryDBSource) : UsersDB {

    override fun createNewUser(conn: ConnectionDB, name: String, email: String): Int {
        val id = source.nextUserId.getAndIncrement()

        check(!source.users.containsKey(id)) { "Serial ID already exists" }

        if (source.users.values.any { it.email == email })
            throw AppError.Conflict("Email already in use")

        source.users[id] = User(id, name, email)

        return id
    }

    override fun getUser(conn: ConnectionDB, uid: Int): User =
        source.users[uid]
            ?: throw AppError.NotFound("User with id $uid not found")

    override fun getAllUsers(conn: ConnectionDB): List<User> {
        return source.users.values.toList()
    }

    override fun hasUserWithEmail(conn: ConnectionDB, email: String): Boolean =
        source.users.values.any { it.email == email }

    override fun hasUser(conn: ConnectionDB, uid: Int): Boolean =
        source.users.containsKey(uid)
}
