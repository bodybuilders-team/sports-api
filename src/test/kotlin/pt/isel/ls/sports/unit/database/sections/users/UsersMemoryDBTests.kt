package pt.isel.ls.sports.unit.database.sections.users

import pt.isel.ls.sports.database.AlreadyExistsException
import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.database.AppMemoryDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UsersMemoryDBTests : AppMemoryDBTests(), UsersDBTests {

    // createNewUser

    @Test
    override fun `createNewUser creates user correctly in the database`(): Unit = db.execute { conn ->
        val uid = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        assertEquals(User(uid, "Nyckollas Brandão", "nyckollasbrandao@mail.com"), source.users[uid])
    }

    @Test
    override fun `createNewUser returns correct identifier`(): Unit = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        assertEquals(1, uid1)
        assertEquals(2, uid2)
        assertEquals(3, uid3)
    }

    @Test
    override fun `createNewUser throws AlreadyExistsException if a user with the email already exists`(): Unit =
        db.execute { conn ->
            db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

            assertFailsWith<AlreadyExistsException> {
                db.users.createNewUser(conn, "André Jesus", "nyckollasbrandao@mail.com")
            }
        }

    // getUser

    @Test
    override fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        source.users[1] = user

        assertEquals(user, db.users.getUser(conn, 1))
    }

    @Test
    override fun `getUser throws NotFoundException if the user with the uid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.users.getUser(conn, 1)
            }
        }

    // getAllUsers

    @Test
    override fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        source.users[1] = user1
        source.users[2] = user2
        source.users[3] = user3

        assertEquals(listOf(user1, user2, user3), db.users.getAllUsers(conn, 0, 10).users)
    }

    @Test
    override fun `getAllUsers with no created users returns empty list`(): Unit = db.execute { conn ->
        assertEquals(emptyList(), db.users.getAllUsers(conn, 0, 10).users)
    }

    @Test
    override fun `getAllUsers with skip works`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        source.users[1] = user1
        source.users[2] = user2
        source.users[3] = user3

        assertEquals(listOf(user2, user3), db.users.getAllUsers(conn, 1, 10).users)
    }

    @Test
    override fun `getAllUsers with limit works`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val user2 = User(2, "André Jesus", "andrejesus@mail.com")
        val user3 = User(3, "André Páscoa", "andrepascoa@mail.com")

        source.users[1] = user1
        source.users[2] = user2
        source.users[3] = user3

        assertEquals(listOf(user1, user2), db.users.getAllUsers(conn, 0, 2).users)
    }

    // hasUserWithEmail

    @Test
    override fun `hasUserWithEmail returns true if a user with the given email exists`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.users[1] = user1

        assertTrue(db.users.hasUserWithEmail(conn, "nyckollasbrandao@mail.com"))
    }

    @Test
    override fun `hasUserWithEmail returns false if a user with the given email does not exist`(): Unit =
        db.execute { conn ->
            assertFalse(db.users.hasUserWithEmail(conn, "dada@alunos.isel.pt"))
        }

    // hasUser

    @Test
    override fun `hasUser returns true if a user with the given uid exists`(): Unit = db.execute { conn ->
        val user1 = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        source.users[1] = user1

        assertTrue(db.users.hasUser(conn, 1))
    }

    @Test
    override fun `hasUser returns false if a user with the given uid does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.users.hasUser(conn, 1))
    }
}
