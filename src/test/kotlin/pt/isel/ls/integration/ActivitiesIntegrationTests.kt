package pt.isel.ls.integration

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.json
import pt.isel.ls.sports.api.routers.activities.CreateActivityRequest
import pt.isel.ls.sports.api.routers.activities.CreateActivityResponse
import pt.isel.ls.sports.api.routers.utils.MessageResponse
import pt.isel.ls.sports.domain.Activity
import pt.isel.ls.sports.errors.SportsError
import pt.isel.ls.sports.services.isValidId
import pt.isel.ls.token
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActivitiesIntegrationTests : IntegrationTests() {
    // Create new activity

    @Test
    fun `Create new activity with valid data`() {
        val uid = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.createUserToken(uid)

        val sid = db.createNewSport(uid, "Running")

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": $sid
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").token(token).json(requestBody)

        send(request).apply {
            assertEquals(Status.CREATED, status)

            val aid = Json.decodeFromString<CreateActivityResponse>(bodyString()).aid
            assertTrue(isValidId(aid))

            assertTrue(db.hasActivity(aid))
        }
    }

    @Test
    fun `Create new activity with no token`() {
        val uid = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val sid = db.createNewSport(uid, "Running")

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": $sid
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").json(requestBody)

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.noCredentials(), error)
        }
    }

    @Test
    fun `Create new activity with invalid token`() {
        val uid = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val sid = db.createNewSport(uid, "Running")

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": $sid
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").token("invalid token").json(requestBody)

        send(request).apply {
            assertEquals(Status.UNAUTHORIZED, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.invalidCredentials(), error)
        }
    }

    @Test
    fun `Create new activity with invalid data`() {
        val uid = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.createUserToken(uid)
        val sid = db.createNewSport(uid, "Running")

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "hello",
                "sid": $sid
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").token(token).json(requestBody)

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.invalidArgument(), error)
        }
    }

    // Get activity

    @Test
    fun `Get activity by id`() {
        val mockId = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val sid = db.createNewSport(mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val aid = db.createNewActivity(mockId, mockActivity.date, mockActivity.duration, mockActivity.sid)

        val request = Request(Method.GET, "$uriPrefix/activities/$aid")

        send(request).apply {
            assertEquals(Status.OK, status)

            val activity = Json.decodeFromString<Activity>(bodyString())
            assertEquals(mockId, activity.id)
            assertEquals(mockId, activity.uid)

            assertEquals(mockActivity.date, activity.date)
            assertEquals(mockActivity.duration, activity.duration)
            assertEquals(mockActivity.sid, activity.sid)
        }
    }

    @Test
    fun `Get activity by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/activities/$mockId")

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.invalidArgument(), error)
        }
    }

    @Test
    fun `Get activity by valid id but activity does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/activities/$mockId")

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.notFound(), error)
        }
    }

    // Delete activity

    @Test
    fun `Delete activity`() {
        val mockId = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.createUserToken(mockId)

        val sid = db.createNewSport(mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val aid = db.createNewActivity(mockId, mockActivity.date, mockActivity.duration, mockActivity.sid)

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.OK, status)

            Json.decodeFromString<MessageResponse>(bodyString())
        }

        assertFalse(db.hasActivity(aid))
    }

    @Test
    fun `Delete activity that does not exist`() {
        val mockId = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.createUserToken(mockId)

        val aid = 1

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.notFound(), error)
        }
    }

    @Test
    fun `Delete activity with invalid token`() {
        val mockId = db.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.createUserToken(mockId)

        val sid = db.createNewSport(mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val mockId2 = db.createNewUser("Johnny2", "JohnnyBoy2@gmail.com")
        val aid = db.createNewActivity(mockId2, mockActivity.date, mockActivity.duration, mockActivity.sid)

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.FORBIDDEN, status)

            val error = Json.decodeFromString<SportsError>(bodyString())
            assertEquals(SportsError.forbidden(), error)
        }
    }

    // Search activity
    // TODO - test search activity
}
