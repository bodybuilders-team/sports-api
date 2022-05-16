package pt.isel.ls.sports.integration.sections.sports

import kotlinx.datetime.toLocalDate
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityRequest
import pt.isel.ls.sports.api.routers.sports.dtos.CreateSportRequest
import pt.isel.ls.sports.api.routers.sports.dtos.CreateSportResponse
import pt.isel.ls.sports.api.routers.sports.dtos.SportDTO
import pt.isel.ls.sports.api.routers.sports.dtos.SportsResponseDTO
import pt.isel.ls.sports.api.routers.sports.dtos.UpdateSportResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.AppError
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.token
import pt.isel.ls.sports.integration.IntegrationTests
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SportsIntegrationTests : IntegrationTests() {

    // Create new sport

    @Test
    fun `Create new sport with valid data`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            token
        }

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

                val sid = this.decodeBodyAs<CreateSportResponse>().sid
                assertTrue(isValidId(sid))
                db.execute { conn ->
                    assertTrue(db.sports.hasSport(conn, sid))
                }
            }
    }

    @Test
    fun `Create new sport with empty description`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            token
        }

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

                val sid = this.decodeBodyAs<CreateSportResponse>().sid
                assertTrue(isValidId(sid))

                db.execute { conn ->
                    assertTrue(db.sports.hasSport(conn, sid))
                }
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("MISSING_TOKEN", error.name)
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("UNAUTHENTICATED", error.name)
            }
    }

    @Test
    fun `Create new sport with invalid data`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            token
        }
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    // Update sport

    @Test
    fun `Update sport with valid data`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val sid = db.sports.createNewSport(conn, uid, "Running", "Running")

            object {
                val token = token
                val sid = sid
            }
        }

        val requestBody = """
            {
                "name": "Sprint",
                "description": "100 Meters Sprint"
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/sports/${mockData.sid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val modified = this.decodeBodyAs<UpdateSportResponse>().modified
                assertTrue(modified)
            }
    }

    @Test
    fun `Update sport with valid data same as before`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val sid = db.sports.createNewSport(conn, uid, "Running", "Running")

            object {
                val token = token
                val sid = sid
            }
        }

        val requestBody = """
            {
                "name": "Running",
                "description": "Running"
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/sports/${mockData.sid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val modified = this.decodeBodyAs<UpdateSportResponse>().modified
                assertFalse(modified)
            }
    }

    @Test
    fun `Update sport with invalid sid`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            object {
                val token = token
                val sid = -99
            }
        }

        val requestBody = """
            {
                "name": "Sprint",
                "description": "100 Meters Sprint"
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/sports/${mockData.sid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    // Search sports

    @Test
    fun `Get all sports`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")

            val sports = listOf(
                CreateSportRequest("Sprint", "100 Meters Sprint"),
                CreateSportRequest("PowerLifting", "LIGHT WEIGHT BABY!"),
            ).associateBy {
                db.sports.createNewSport(conn, uid, it.name, it.description)
            }

            object {
                val uid = uid
                val sports = sports
            }
        }

        val request = Request(Method.GET, "$uriPrefix/sports")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sports = this.decodeBodyAs<SportsResponseDTO>().sports
                assertEquals(mockData.sports.size, sports.size)

                sports.forEach { sport ->
                    val mockSport = mockData.sports[sport.id]
                    assertNotNull(mockSport)

                    assertEquals(mockSport.name, sport.name)
                    assertEquals(mockSport.description, sport.description)
                    assertEquals(mockData.uid, sport.uid)
                }
            }
    }

    @Test
    fun `Get all sports but with empty database`() {
        val request = Request(Method.GET, "$uriPrefix/sports")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sports = this.decodeBodyAs<SportsResponseDTO>().sports
                assertEquals(0, sports.size)
            }
    }

    // Get sport by id

    @Test
    fun `Get sport by id`() {
        val mockData = db.execute { conn ->
            val sport = CreateSportRequest("Sprint", "100 Meters Sprint")

            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val sid = db.sports.createNewSport(conn, uid, sport.name, sport.description)
            object {
                val uid = uid
                val sid = sid
                val sport = sport
            }
        }

        val request = Request(Method.GET, "$uriPrefix/sports/${mockData.sid}")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sport = this.decodeBodyAs<SportDTO>()
                assertEquals(mockData.uid, sport.uid)
                assertEquals(mockData.sid, sport.id)

                assertEquals(mockData.sport.name, sport.name)
                assertEquals(mockData.sport.description, sport.description)
            }
    }

    @Test
    fun `Get sport with empty description by id`() {
        val mockData = db.execute { conn ->
            val sport = CreateSportRequest("Sprint")

            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val sid = db.sports.createNewSport(conn, uid, sport.name, sport.description)
            object {
                val uid = uid
                val sid = sid
                val sport = sport
            }
        }

        val request = Request(Method.GET, "$uriPrefix/sports/${mockData.sid}")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val sport = this.decodeBodyAs<SportDTO>()
                assertEquals(mockData.uid, sport.id)
                assertEquals(mockData.sport.name, sport.name)
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    @Test
    fun `Get sport by valid id but sport does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/sports/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("NOT_FOUND", error.name)
            }
    }

    // Get sport activities

    @Test
    fun `Get sport activities by valid id`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val sid = db.sports.createNewSport(conn, uid, "Running", "Running")

            val activities = listOf(
                CreateActivityRequest("2019-01-01", "23:59:59.555", sid),
                CreateActivityRequest("2019-01-02", "20:59:59.555", sid)
            ).associateBy {
                db.activities.createNewActivity(conn, uid, it.date.toLocalDate(), it.duration.toDuration(), it.sid)
            }
            object {
                val sid = sid
                val activities = activities
            }
        }

        val request = Request(Method.GET, "$uriPrefix/sports/${mockData.sid}/activities")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val activities = this.decodeBodyAs<ActivitiesResponseDTO>().activities
                assertEquals(mockData.activities.size, activities.size)

                activities.forEach { activity ->
                    val mockActivity = mockData.activities[activity.id]
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }
}
