package pt.isel.ls.sports.unit.database.sections.tokens

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.unit.database.AppPostgresDBTests
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TokensDBPostgresTests : AppPostgresDBTests(), TokensDBTests {

    // createUserToken

    @Test
    override fun `createUserToken creates token correctly in the database`() {
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

    @Test
    override fun `Having multiple tokens for the same user is allowed`(): Unit = db.execute { conn ->
        val token0 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token1 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token2 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)
        val token3 = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)

        assertEquals(1, db.tokens.getUID(conn, token0))
        assertEquals(1, db.tokens.getUID(conn, token1))
        assertEquals(1, db.tokens.getUID(conn, token2))
        assertEquals(1, db.tokens.getUID(conn, token3))
    }

    // getUID

    @Test
    override fun `getUID returns the uid correctly`(): Unit = db.execute { conn ->
        val uid = db.tokens.getUID(conn, "49698b60-12ca-4df7-8950-d783124f5fas")
        assertEquals(1, uid)
    }

    @Test
    override fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.tokens.getUID(conn, "T-o-k-e-n")
            }
        }
}
