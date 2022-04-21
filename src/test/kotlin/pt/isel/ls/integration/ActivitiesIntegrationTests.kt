package pt.isel.ls.integration

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.activities.dtos.ActivityDTO
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityRequestDTO
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityResponseDTO
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserRequestDTO
import pt.isel.ls.sports.api.routers.users.dtos.UsersResponseDTO
import pt.isel.ls.sports.api.utils.AppErrorDTO
import pt.isel.ls.sports.api.utils.MessageResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.token
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ActivitiesIntegrationTests : IntegrationTests() {
    // Create new activity

    @Test
    fun `Create new activity with valid data`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            val sid = db.sports.createNewSport(conn, uid, "Running")
            object {
                val token = token
                val sid = sid
            }
        }

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": ${mockData.sid}
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").token(mockData.token).json(requestBody)

        send(request).apply {
            assertEquals(Status.CREATED, status)

            val aid = this.decodeBodyAs<CreateActivityResponseDTO>().aid
            assertTrue(isValidId(aid))

            db.execute { conn ->
                assertTrue(db.activities.hasActivity(conn, aid))
            }
        }
    }

    @Test
    fun `Create new activity with no token`() {

        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": 1
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").json(requestBody)

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.NoCredentials(), error)
        }
    }

    @Test
    fun `Create new activity with invalid token`() {
        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "01:00:00.000",
                "sid": 1
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/activities").token("invalid token").json(requestBody)

        send(request).apply {
            assertEquals(Status.UNAUTHORIZED, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.InvalidCredentials(), error)
        }
    }

    @Test
    fun `Create new activity with invalid data`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            token
        }
        val requestBody = """
            {
                "date": "2020-01-01",
                "duration": "hello",
                "sid": 1
            }
        """.trimIndent()

        val request =
            Request(Method.POST, "$uriPrefix/activities").token(token).json(requestBody)

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.InvalidArgument(), error)
        }
    }

    // Get activity

    @Test
    fun `Get activity by id`() {
        val mockData = db.execute { conn ->
            val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val sid = db.sports.createNewSport(conn, mockId, "Running")

            val mockActivity = CreateActivityRequestDTO(
                date = "2020-01-01", duration = "01:00:00.000", sid = sid
            )

            val aid = db.activities.createNewActivity(
                conn,
                mockId,
                mockActivity.date.toLocalDate(),
                mockActivity.duration.toDuration(),
                mockActivity.sid
            )

            object {
                val aid = aid
                val mockId = mockId
                val mockActivity = mockActivity
            }
        }

        val request = Request(Method.GET, "$uriPrefix/activities/${mockData.aid}")

        send(request).apply {
            assertEquals(Status.OK, status)

            val activity = this.decodeBodyAs<ActivityDTO>()
            assertEquals(mockData.mockId, activity.id)
            assertEquals(mockData.mockId, activity.uid)

            assertEquals(mockData.mockActivity.date, activity.date)
            assertEquals(mockData.mockActivity.duration, activity.duration)
            assertEquals(mockData.mockActivity.sid, activity.sid)
        }
    }

    @Test
    fun `Get activity by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/activities/$mockId")

        send(request).apply {
            assertEquals(Status.BAD_REQUEST, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.InvalidArgument(), error)
        }
    }

    @Test
    fun `Get activity by valid id but activity does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/activities/$mockId")

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()

            assertEquals(AppException.NotFound(), error)
        }
    }

    // Delete activity

    @Test
    fun `Delete activity`() {
        val mockData = db.execute { conn ->
            val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

            val sid = db.sports.createNewSport(conn, mockId, "Running")

            val mockActivity = CreateActivityRequestDTO(
                date = "2020-01-01", duration = "01:00:00.000", sid = sid
            )

            val aid = db.activities.createNewActivity(
                conn,
                mockId,
                mockActivity.date.toLocalDate(),
                mockActivity.duration.toDuration(),
                mockActivity.sid
            )
            object {
                val aid = aid
                val token = token
            }
        }

        val request = Request(Method.DELETE, "$uriPrefix/activities/${mockData.aid}").token(mockData.token)

        send(request).apply {
            assertEquals(Status.OK, status)

            this.decodeBodyAs<MessageResponse>()
        }

        db.execute { conn ->
            assertFalse(db.activities.hasActivity(conn, mockData.aid))
        }
    }

    @Test
    fun `Delete activity that does not exist`() {
        val token = db.execute { conn ->
            val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

            token
        }

        val aid = 1
        val request = Request(Method.DELETE, "$uriPrefix/activities/$aid").token(token)

        send(request).apply {
            assertEquals(Status.NOT_FOUND, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.NotFound(), error)
        }
    }

    @Test
    fun `Delete activity with invalid token`() {
        val mockData = db.execute { conn ->
            val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

            val sid = db.sports.createNewSport(conn, mockId, "Running")

            val mockActivity = CreateActivityRequestDTO(
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
            object {
                val aid = aid
                val token = token
            }
        }

        val request = Request(Method.DELETE, "$uriPrefix/activities/${mockData.aid}").token(mockData.token)

        send(request).apply {
            assertEquals(Status.FORBIDDEN, status)

            val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
            assertEquals(AppException.Forbidden(), error)
        }
    }

    // Delete activities

    @Test
    fun `Delete a set of activities`() {
        val mockData = db.execute { conn ->
            val mockId = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), mockId)

            val sid = db.sports.createNewSport(conn, mockId, "Running")

            val mockActivity1 = CreateActivityRequestDTO(
                date = "2020-01-01", duration = "01:00:00.000", sid = sid
            )

            val mockActivity2 = CreateActivityRequestDTO(
                date = "2020-01-01", duration = "01:00:00.000", sid = sid
            )

            val aid1 = db.activities.createNewActivity(
                conn,
                mockId,
                mockActivity1.date.toLocalDate(),
                mockActivity1.duration.toDuration(),
                mockActivity1.sid
            )

            val aid2 = db.activities.createNewActivity(
                conn,
                mockId,
                mockActivity2.date.toLocalDate(),
                mockActivity2.duration.toDuration(),
                mockActivity2.sid
            )

            object {
                val aid1 = aid1
                val aid2 = aid2
                val token = token
            }
        }

        val requestBody = """
            {
                "activityIds": [
                    ${mockData.aid1},
                    ${mockData.aid2}
                ]
            }
        """.trimIndent()

        val request = Request(Method.DELETE, "$uriPrefix/activities").token(mockData.token).json(requestBody)

        send(request).apply {
            assertEquals(Status.OK, status)

            this.decodeBodyAs<MessageResponse>()
        }

        db.execute { conn ->
            assertFalse(db.activities.hasActivity(conn, mockData.aid1))
            assertFalse(db.activities.hasActivity(conn, mockData.aid2))
        }
    }

    // Search activity

    @Test
    fun `Search activity by sid and orderBy`() {
        val mockData = db.execute { conn ->
            val mockUid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val mockSid = db.sports.createNewSport(conn, mockUid, "Running")
            val orderBy = "ascending"

            val mockActivities = listOf(
                CreateActivityRequestDTO("2019-01-01", "23:59:59.555", mockSid),
                CreateActivityRequestDTO("2019-01-02", "20:59:59.555", mockSid)
            ).associateBy {
                db.activities.createNewActivity(conn, mockUid, it.date.toLocalDate(), it.duration.toDuration(), it.sid)
            }

            object {
                val activities = mockActivities
                val orderBy = orderBy
                val sid = mockSid
            }
        }
        val request =
            Request(Method.GET, "$uriPrefix/activities?sid=${mockData.sid}&orderBy=${mockData.orderBy}")

        send(request).apply {
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

    // searchUsersByActivity

    @Test
    fun `Search users by activity`() {
        val mockData = db.execute { conn ->

            val mockUsers = listOf(
                CreateUserRequestDTO("Johnny", "JohnnyBoy@gmail.com"),
                CreateUserRequestDTO("Johnny2", "JackyBoy@gmail.com")
            ).associateBy {
                db.users.createNewUser(conn, it.name, it.email)
            }

            val mockSid = db.sports.createNewSport(conn, 1, "Running")
            val mockRid = db.routes.createNewRoute(conn, "Porto", "Evora", 201, 1)

            mockUsers.forEach {
                db.activities.createNewActivity(
                    conn,
                    it.key,
                    LocalDate(2020, 1, 1),
                    (6 * it.key).toDuration(DurationUnit.HOURS),
                    mockSid,
                    mockRid
                )
            }

            object {
                val users = mockUsers
                val sid = mockSid
                val rid = mockRid
            }
        }

        val request =
            Request(Method.GET, "$uriPrefix/activities/users?sid=${mockData.sid}&rid=${mockData.rid}")

        send(request).apply {
            assertEquals(Status.OK, status)

            val users = this.decodeBodyAs<UsersResponseDTO>().users

            assertEquals(mockData.users.size, users.size)

            var counter = 0

            mockData.users.keys.sorted().forEach {
                val mockUser = mockData.users[it]

                val user = users[counter++]

                assertNotNull(mockUser)

                assertEquals(user.id, it)
                assertEquals(mockUser.name, user.name)
                assertEquals(mockUser.email, user.email)
            }
        }
    }
}
