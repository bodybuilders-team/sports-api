package pt.isel.ls.sports.unit.database.sections.users

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.tableAsserter
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersPostgresDBTests : AppPostgresDBTests(), UsersDBTests {

    // createNewUser

    @Test
    override fun `createNewUser creates user correctly in the database`() {
        val uid = db.execute { conn ->
            db.users.createNewUser(conn, "Paulão", "paulao@mail.com")
        }

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM users WHERE id = ?")
            stm.setInt(1, uid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(4, "Paulão", "paulao@mail.com")
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getString("email"))
            }
        }
    }

    @Test
    override fun `createNewUser returns correct identifier`() = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com")

        assertEquals(4, uid1)
        assertEquals(5, uid2)
        assertEquals(6, uid3)
    }

    // getUser

    @Test
    override fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = db.users.getUser(conn, 1)

        assertEquals(User(1, "André Jesus", "A48280@alunos.isel.pt"), user)
    }

    @Test
    override fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.users.getUser(conn, 0)
            }
        }

    // getAllUsers

    @Test
    override fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val users = listOf(
            User(1, "André Jesus", "A48280@alunos.isel.pt"),
            User(2, "André Páscoa", "A48089@alunos.isel.pt"),
            User(3, "Nyckollas Brandão", "A48287@alunos.isel.pt")
        )

        assertEquals(users, db.users.getAllUsers(conn, 0, 10).users)
    }

    @Test
    override fun `getAllUsers with no created users returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.users.getAllUsers(conn, 0, 10).users)
        }
    }
}
