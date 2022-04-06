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
import pt.isel.ls.sports.api.routers.activities.CreateActivityRequest
import pt.isel.ls.sports.api.routers.sports.CreateSportRequest
import pt.isel.ls.sports.api.routers.sports.CreateSportResponse
import pt.isel.ls.sports.api.routers.sports.SportDTO
import pt.isel.ls.sports.api.routers.sports.SportsResponse
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import pt.isel.ls.token
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SportsIntegrationTests : IntegrationTests() {
    // Create new sport

    @Test
    fun `Create new sport with valid data`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val requestBody = """
            {
                "name": "Sprint",
                "description": "100 Meters Sprint"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/sports")
            .token(token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CREATED, status)

                val sid = Json.decodeFromString<CreateSportResponse>(bodyString()).sid
                assertTrue(isValidId(sid))

                assertTrue(db.sports.hasSport(sid))
            }
    }

    @Test
    fun `Create new sport with empty description`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val requestBody = """
            {
                "name": "Sprint"
            }
            
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/sports")
            .token(token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CREATED, status)

                val sid = Json.decodeFromString<CreateSportResponse>(bodyString()).sid
                assertTrue(isValidId(sid))

                assertTrue(db.sports.hasSport(sid))
            }
    }

    @Test
    fun `Create new sport with no token`() {
        val requestBody = """
            {
                "name": "Sprint",
                "description": "100 Meters Sprint"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/sports")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.NoCredentials(), error)
            }
    }

    @Test
    fun `Create new sport with invalid token`() {
        val requestBody = """
            {
                "name": "Sprint",
                "description": "100 Meters Sprint"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/sports")
            .token("invalid token")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.UNAUTHORIZED, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidCredentials(), error)
            }
    }

    @Test
    fun `Create new sport with invalid data`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)
        val requestBody = """
            {
                "name": "S"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/sports")
            .token(token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidArgument(), error)
            }
    }

    // Get all sports

    @Test
    fun `Get all sports`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")

        val mockSports = listOf(
            CreateSportRequest("Sprint", "100 Meters Sprint"),
            CreateSportRequest("PowerLifting", "LIGHT WEIGHT BABY!"),
        ).associateBy {
            db.sports.createNewSport(uid, it.name, it.description)
        }

        val request = Request(Method.GET, "$uriPrefix/sports")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sports = Json.decodeFromString<SportsResponse>(bodyString()).sports
                assertEquals(mockSports.size, sports.size)

                sports.forEach { sport ->
                    val mockSport = mockSports[sport.id]
                    assertNotNull(mockSport)

                    assertEquals(mockSport.name, sport.name)
                    assertEquals(mockSport.description, sport.description)
                    assertEquals(uid, sport.uid)
                }
            }
    }

    @Test
    fun `Get all sports but with empty database`() {
        val request = Request(Method.GET, "$uriPrefix/sports")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sports = Json.decodeFromString<SportsResponse>(bodyString()).sports
                assertEquals(0, sports.size)
            }
    }

    // Get sport by id

    @Test
    fun `Get sport by id`() {
        val mockSport = CreateSportRequest("Sprint", "100 Meters Sprint")

        val mockId = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(mockId, mockSport.name, mockSport.description)

        val request = Request(Method.GET, "$uriPrefix/sports/$sid")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sport = Json.decodeFromString<SportDTO>(bodyString())
                assertEquals(mockId, sport.uid)
                assertEquals(sid, sport.id)

                assertEquals(mockSport.name, sport.name)
                assertEquals(mockSport.description, sport.description)
            }
    }

    @Test
    fun `Get sport with empty description by id`() {
        val mockSport = CreateSportRequest("Sprint")

        val mockId = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(mockId, mockSport.name, mockSport.description)

        val request = Request(Method.GET, "$uriPrefix/sports/$sid")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sport = Json.decodeFromString<SportDTO>(bodyString())
                assertEquals(mockId, sport.id)
                assertEquals(mockSport.name, sport.name)
                assertNull(sport.description)
            }
    }

    @Test
    fun `Get sport by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/sports/$mockId")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidArgument(), error)
            }
    }

    @Test
    fun `Get sport by valid id but sport does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/sports/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.NotFound(), error)
            }
    }

    // Get sport activities

    @Test
    fun `Get sport activities by valid id`() {
        val mockUid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val mockSid = db.sports.createNewSport(mockUid, "Running", "Running")

        val mockActivities = listOf(
            CreateActivityRequest("2019-01-01", "23:59:59.555", mockSid),
            CreateActivityRequest("2019-01-02", "20:59:59.555", mockSid)
        ).associateBy {
            db.activities.createNewActivity(mockUid, it.date.toLocalDate(), it.duration.toDuration(), it.sid)
        }

        val request = Request(Method.GET, "$uriPrefix/sports/$mockSid/activities")

        send(request)
            .apply {
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

    @Test
    fun `Get sport activities by invalid id`() {
        val mockUid = -1

        val request = Request(Method.GET, "$uriPrefix/sports/$mockUid/activities")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidArgument(), error)
            }
    }
}
