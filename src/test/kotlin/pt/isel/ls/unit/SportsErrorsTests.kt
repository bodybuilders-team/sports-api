package pt.isel.ls.unit

import org.http4k.core.Body
import org.http4k.core.Status
import pt.isel.ls.sports.errors.SportsError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SportsErrorsTests {

    @Test
    fun `throw SportsErrors badRequest works`() {
        val msg = "badRequest message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.badRequest(msg)
        }

        assertEquals(1000, error.code)
        assertEquals("BAD_REQUEST", error.name)
        assertEquals("The request was malformed", error.description)
        assertEquals(msg, error.extraInfo)
    }

    @Test
    fun `throw SportsErrors notFound works`() {
        val msg = "notFound message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.notFound(msg)
        }

        assertEquals(1001, error.code)
        assertEquals("NOT_FOUND", error.name)
        assertEquals("The requested resource was not found", error.description)
        assertEquals(msg, error.extraInfo)
    }

    @Test
    fun `throw SportsErrors databaseError works`() {
        val msg = "databaseError message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.databaseError(msg)
        }

        assertEquals(1002, error.code)
        assertEquals("DATABASE_ERROR", error.name)
        assertEquals("There was an error accessing the database", error.description)
        assertEquals(msg, error.extraInfo)
    }

    @Test
    fun `throw SportsErrors internalError works`() {
        val msg = "internalError message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.internalError(msg)
        }

        assertEquals(1003, error.code)
        assertEquals("INTERNAL_ERROR", error.name)
        assertEquals("There was an internal error", error.description)
        assertEquals(msg, error.extraInfo)
    }

    @Test
    fun `throw SportsErrors invalidCredentials works`() {
        val msg = "invalidCredentials message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.invalidCredentials(msg)
        }

        assertEquals(1004, error.code)
        assertEquals("INVALID_CREDENTIALS", error.name)
        assertEquals("The provided credentials are invalid", error.description)
        assertEquals(msg, error.extraInfo)
    }

    @Test
    fun `throw SportsErrors noCredentials works`() {
        val msg = "noCredentials message"
        val error = assertFailsWith<SportsError> {
            throw SportsError.noCredentials(msg)
        }

        assertEquals(1005, error.code)
        assertEquals("NO_CREDENTIALS", error.name)
        assertEquals("No credentials were provided", error.description)
        assertEquals(msg, error.extraInfo)
    }

    // toResponse

    @Test
    fun `badRequest toResponse`() {
        val res = SportsError.badRequest().toResponse()
        assertEquals(Status.BAD_REQUEST, res.status)
        assertEquals(
            Body("{\"code\":1000,\"name\":\"BAD_REQUEST\",\"description\":\"The request was malformed\"}"),
            res.body
        )
    }

    @Test
    fun `notFound toResponse`() {
        val res = SportsError.notFound().toResponse()
        assertEquals(Status.NOT_FOUND, res.status)
        assertEquals(
            Body("{\"code\":1001,\"name\":\"NOT_FOUND\",\"description\":\"The requested resource was not found\"}"),
            res.body
        )
    }

    @Test
    fun `invalidCredentials toResponse`() {
        val res = SportsError.invalidCredentials().toResponse()
        assertEquals(Status.UNAUTHORIZED, res.status)
        assertEquals(
            Body("{\"code\":1004,\"name\":\"INVALID_CREDENTIALS\",\"description\":\"The provided credentials are invalid\"}"),
            res.body
        )
    }

    @Test
    fun `databaseError toResponse`() {
        val res = SportsError.databaseError().toResponse()
        assertEquals(Status.INTERNAL_SERVER_ERROR, res.status)
        assertEquals(
            Body("{\"code\":1002,\"name\":\"DATABASE_ERROR\",\"description\":\"There was an error accessing the database\"}"),
            res.body
        )
    }
}
