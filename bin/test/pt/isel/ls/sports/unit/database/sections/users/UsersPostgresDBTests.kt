package pt.isel.ls.sports.unit.database.sections.users
/*
import pt.isel.ls.sports.database.exceptions.AlreadyExistsException
import pt.isel.ls.sports.database.exceptions.NotFoundException
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.tableAsserter
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UsersPostgresDBTests : AppPostgresDBTests(), UsersDBTests {

    // createNewUser

    @Test
    override fun `createNewUser creates user correctly in the database`() {
        val uid = db.execute { conn ->
            db.users.createNewUser(
                conn,
                "Paulão",
                "paulao@mail.com",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
        }

        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM users WHERE id = ?")
            stm.setInt(1, uid)
            val rs = stm.executeQuery()

            val mockTable: Array<Array<Any>> = arrayOf(
                arrayOf(
                    4,
                    "Paulão",
                    "paulao@mail.com",
                    "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
                )
            )

            tableAsserter(mockTable, rs) { mockRow, row ->
                assertEquals(mockRow[0], row.getInt("id"))
                assertEquals(mockRow[1], row.getString("name"))
                assertEquals(mockRow[2], row.getString("email"))
                assertEquals(mockRow[3], row.getString("password"))
            }
        }
    }

    @Test
    override fun `createNewUser returns correct identifier`() = db.execute { conn ->
        val uid1 = db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")
        val uid2 = db.users.createNewUser(conn, "André Jesus", "andrejesus@mail.com", "H42xS")
        val uid3 = db.users.createNewUser(conn, "André Páscoa", "andrepascoa@mail.com", "H42xS")

        assertEquals(4, uid1)
        assertEquals(5, uid2)
        assertEquals(6, uid3)
    }

    @Test
    override fun `createNewUser throws AlreadyExistsException if a user with the email already exists`(): Unit =
        db.execute { conn ->
            db.users.createNewUser(conn, "Nyckollas Brandão", "nyckollasbrandao@mail.com", "H42xS")

            assertFailsWith<AlreadyExistsException> {
                db.users.createNewUser(conn, "André Jesus", "nyckollasbrandao@mail.com", "H42xS")
            }
        }

    // getUser

    @Test
    override fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = db.users.getUser(conn, 1)

        assertEquals(
            User(
                1,
                "André Jesus",
                "A48280@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            user
        )
    }

    @Test
    override fun `getUser throws NotFoundException if the user with the uid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.users.getUser(conn, 0)
            }
        }

    // getAllUsers

    @Test
    override fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val users = listOf(
            User(
                1,
                "André Jesus",
                "A48280@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            User(
                2,
                "André Páscoa",
                "A48089@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            User(
                3,
                "Nyckollas Brandão",
                "A48287@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
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

    @Test
    override fun `getAllUsers with skip works`(): Unit = db.execute { conn ->
        val users = listOf(
            User(
                2,
                "André Páscoa",
                "A48089@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            User(
                3,
                "Nyckollas Brandão",
                "A48287@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
        )

        assertEquals(users, db.users.getAllUsers(conn, 1, 10).users)
    }

    @Test
    override fun `getAllUsers with limit works`(): Unit = db.execute { conn ->
        val users = listOf(
            User(
                1,
                "André Jesus",
                "A48280@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            ),
            User(
                2,
                "André Páscoa",
                "A48089@alunos.isel.pt",
                "31ffffffa91cffffffccffffffe008205748ffffffabffffffbaffffffea6a62"
            )
        )

        assertEquals(users, db.users.getAllUsers(conn, 0, 2).users)
    }

    // hasUserWithEmail

    @Test
    override fun `hasUserWithEmail returns true if a user with the given email exists`(): Unit = db.execute { conn ->
        assertTrue(db.users.hasUserWithEmail(conn, "A48280@alunos.isel.pt"))
    }

    @Test
    override fun `hasUserWithEmail returns false if a user with the given email does not exist`(): Unit =
        db.execute { conn ->
            assertFalse(db.users.hasUserWithEmail(conn, "dada@alunos.isel.pt"))
        }

    // hasUser

    @Test
    override fun `hasUser returns true if a user with the given uid exists`(): Unit = db.execute { conn ->
        assertTrue(db.users.hasUser(conn, 1))
    }

    @Test
    override fun `hasUser returns false if a user with the given uid does not exist`(): Unit = db.execute { conn ->
        assertFalse(db.users.hasUser(conn, 9999999))
    }
}
*/
