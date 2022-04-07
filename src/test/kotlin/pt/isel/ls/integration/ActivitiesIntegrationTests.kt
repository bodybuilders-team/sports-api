package pt.isel.ls.integration

import kotlinx.datetime.toLocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.json
import pt.isel.ls.sports.api.routers.activities.ActivitiesResponse
import pt.isel.ls.sports.api.routers.activities.ActivityDTO
import pt.isel.ls.sports.api.routers.activities.CreateActivityRequest
import pt.isel.ls.sports.api.routers.activities.CreateActivityResponse
import pt.isel.ls.sports.api.utils.AppErrorDTO
import pt.isel.ls.sports.api.utils.MessageResponse
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.token
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ActivitiesIntegrationTests : IntegrationTests() {
    // Create new activity

    @Test
    fun `Create new activity with valid data`(): Unit = db.connection.use { conn ->
        val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

        val sid = db.sports.createNewSport(conn, uid, "Running")

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

            assertTrue(db.activities.hasActivity(conn, aid))
        }
        conn.connection.close()
    }

    @Test
    fun `Create new activity with no token`(): Unit = db.connection.use { conn ->
        val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(conn, uid, "Running")

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

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.NoCredentials(), error)
        }
        conn.connection.close()
    }

    @Test
    fun `Create new activity with invalid token`(): Unit = db.connection.use { conn ->
        val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(conn, uid, "Running")

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

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.InvalidCredentials(), error)
        }
    }

    @Test
    fun `Create new activity with invalid data`(): Unit = db.connection.use { conn ->
        val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
        val sid = db.sports.createNewSport(conn, uid, "Running")

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

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.InvalidArgument(), error)
        }
    }

    // Get activity

    @Test
    fun `Get activity by id`(): Unit = db.connection.use { conn ->
        val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(conn, mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val aid = db.activities.createNewActivity(
            conn,
            mockId,
            mockActivity.date.toLocalDate(),
            mockActivity.duration.toDuration(),
            mockActivity.sid
        )

        val request = Request(Method.GET, "$uriPrefix/activities/$aid")

        send(request).apply {
            assertEquals(Status.OK, status)

            val activity = Json.decodeFromString<ActivityDTO>(bodyString())
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

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.InvalidArgument(), error)
        }
    }

    @Test
    fun `Get activity by valid id but activity does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/activities/$mockId")

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()

            assertEquals(AppError.NotFound(), error)
        }
    }

    // Delete activity

    @Test
    fun `Delete activity`(): Unit = db.connection.use { conn ->
        val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

        val sid = db.sports.createNewSport(conn, mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val aid = db.activities.createNewActivity(
            conn,
            mockId,
            mockActivity.date.toLocalDate(),
            mockActivity.duration.toDuration(),
            mockActivity.sid
        )

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.OK, status)

            Json.decodeFromString<MessageResponse>(bodyString())
        }

        assertFalse(db.activities.hasActivity(conn, aid))
    }

    @Test
    fun `Delete activity that does not exist`(): Unit = db.connection.use { conn ->
        val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

        val aid = 1

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.NotFound(), error)
        }
    }

    @Test
    fun `Delete activity with invalid token`(): Unit = db.connection.use { conn ->
        val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

        val sid = db.sports.createNewSport(conn, mockId, "Running")

        val mockActivity = CreateActivityRequest(
            date = "2020-01-01", duration = "01:00:00.000", sid = sid
        )

        val mockId2 = db.users.createNewUser(conn, "Johnny2", "JohnnyBoy2@gmail.com")
        val aid = db.activities.createNewActivity(
            conn,
            mockId2,
            mockActivity.date.toLocalDate(),
            mockActivity.duration.toDuration(),
            mockActivity.sid
        )

        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.FORBIDDEN, status)

            val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
            assertEquals(AppError.Forbidden(), error)
        }
    }

    // Search activity
    @Test
    fun `Search activity by sid and orderBy`(): Unit = db.connection.use { conn ->
        val mockUid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val mockSid = db.sports.createNewSport(conn, mockUid, "Running")
        val orderBy = "ascending"

        val mockActivities = listOf(
            CreateActivityRequest("2019-01-01", "23:59:59.555", mockSid),
            CreateActivityRequest("2019-01-02", "20:59:59.555", mockSid)
        ).associateBy {
            db.activities.createNewActivity(conn, mockUid, it.date.toLocalDate(), it.duration.toDuration(), it.sid)
        }

        val request = Request(Method.GET, "$uriPrefix/activities/search?sid=$mockSid&orderBy=$orderBy")

        send(request).apply {
            assertEquals(Status.OK, status)

            val activities = Json.decodeFromString<ActivitiesResponse>(bodyString()).activities
            assertEquals(mockActivities.size, activities.size)

            activities.forEach { activity ->
                val mockActivity = mockActivities[activity.id]
                assertNotNull(mockActivity)

                assertEquals(mockActivity.date, activity.date)
                assertEquals(mockActivity.duration, activity.duration)
                assertEquals(mockActivity.sid, activity.sid)
            }
        }
    }
}
