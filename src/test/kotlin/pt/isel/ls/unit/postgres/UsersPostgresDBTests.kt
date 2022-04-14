package pt.isel.ls.unit.postgres

import kotlinx.datetime.toLocalDate
import org.junit.Test
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.tableAsserter
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersPostgresDBTests : AppPostgresDBTests() {
    // createNewUser

    @Test
    fun `createNewUser creates user correctly in the database`() {
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

    // getUser

    @Test
    fun `getUser returns the user object`(): Unit = db.execute { conn ->
        val user = db.users.getUser(conn, 1)

        assertEquals(User(1, "André Jesus", "A48280@alunos.isel.pt"), user)
    }

    @Test
    fun `getUser throws SportsError (Not found) if the user with the uid doesn't exist`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppError> {
                db.users.getUser(conn, 0)
            }
        }

    // getAllUsers

    @Test
    fun `getAllUsers returns list of user objects`(): Unit = db.execute { conn ->
        val users = listOf(
            User(1, "André Jesus", "A48280@alunos.isel.pt"),
            User(2, "André Páscoa", "A48089@alunos.isel.pt"),
            User(3, "Nyckollas Brandão", "A48287@alunos.isel.pt")
        )

        assertEquals(users, db.users.getAllUsers(conn))
    }

    @Test
    fun `getAllUsers with no created users returns empty list`() {
        db.reset()
        db.execute { conn ->
            assertEquals(emptyList(), db.users.getAllUsers(conn))
        }
    }

    // createUserToken

    @Test
    fun `createUserToken creates token correctly in the database`() {
        val token = db.execute { conn ->
            db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        }
        dataSource.connection.use {
            val stm = it.prepareStatement("SELECT * FROM tokens WHERE token = ?")
            stm.setString(1, token)
            val rs = stm.executeQuery().also { rs -> rs.next() }

            assertEquals(token, rs.getString(1))
            assertEquals(1, rs.getInt(2))
        }
    }

// getUID

    @Test
    fun `getUID returns the uid correctly`(): Unit = db.execute { conn ->
        val uid = db.tokens.getUID(conn, "49698b60-12ca-4df7-8950-d783124f5fas")
        assertEquals(1, uid)
    }

    @Test
    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`(): Unit =
        db.execute { conn ->
            assertFailsWith<AppError> {
                db.tokens.getUID(conn, "T-o-k-e-n")
            }
        }
// getUserActivities

    @Test
    fun `getUserActivities returns the activities list`(): Unit = db.execute { conn ->
        val activities = db.activities.getUserActivities(conn, 1)
        assertEquals(
            listOf(Activity(1, "2022-11-20".toLocalDate(), "23:44:59.903".toDuration(), 1, 1, null)),
            activities
        )
    }
}
