package pt.isel.ls.sports.unit.database.sections.tokens

import pt.isel.ls.sports.database.NotFoundException
import pt.isel.ls.sports.domain.User
import pt.isel.ls.sports.unit.database.AppMemoryDBTests
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TokensDBMemoryTests : AppMemoryDBTests(), TokensDBTests {

    // createUserToken

    @Test
    override fun `createUserToken creates token correctly in the database`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), 1)

        assertEquals(1, source.tokens[token])
    }

    @Test
    override fun `Having multiple tokens for the same user is allowed`(): Unit = db.execute { conn ->
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
    override fun `getUID returns the uid correctly`(): Unit = db.execute { conn ->
        source.users[1] = User(1, "Nyckollas Brandão", "nyckollasbrandao@mail.com")

        val token = UUID.randomUUID().toString()
        source.tokens[token] = 1

        assertEquals(1, db.tokens.getUID(conn, token))
    }

    @Test
    override fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`(): Unit =
        db.execute { conn ->
            assertFailsWith<NotFoundException> {
                db.tokens.getUID(conn, "T-o-k-e-n")
            }
        }
}
