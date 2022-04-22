package pt.isel.ls.unit.memory

import org.junit.Test
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.User
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersMemoryDBTests : AppMemoryDBTests() {
    @Test
    fun `createNewUser creates user correctly in the database`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"), source.users[uid])
    }

    @Test
    fun `createNewUser returns correct identifier`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        source.users[1] = user

        assertEquals(user, db.users.getUser(conn, 1))
    }

    @Test
    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.users.getUser(conn, 1)
            }
        }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        source.users[1] = user1
        source.users[2] = user2
        source.users[3] = user3

        assertEquals(listOf(user1, user2, user3), db.users.getAllUsers(conn, 0, 10).users)
    }

    @Test
    fun `getAllUsers with no created users returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.users.getAllUsers(conn, 0, 10).users)
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)

        assertEquals(1, source.tokens[token])
    }

    @Test
    fun `Having multiple tokens for the same user is allowed`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token0 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token1 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token2 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token3 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)

        assertEquals(1, source.tokens[token0])
        assertEquals(1, source.tokens[token1])
        assertEquals(1, source.tokens[token2])
        assertEquals(1, source.tokens[token3])
    }

    // getUID

    @Test
    fun `getUID returns the uid correctly`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = UUID.randomUUID().toString()
        source.tokens[token] = 1

        assertEquals(1, db.tokens.getUID(conn, token))
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.tokens.getUID(conn, "T-o-k-e-n")
            }
        }
}
