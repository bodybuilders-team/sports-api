package pt.isel.ls.integration

import kotlinx.datetime.toLocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.json
import pt.isel.ls.sports.api.routers.activities.ActivitiesResponse
import pt.isel.ls.sports.api.routers.activities.CreateActivityRequest
import pt.isel.ls.sports.api.routers.users.CreateUserRequest
import pt.isel.ls.sports.api.routers.users.CreateUserResponse
import pt.isel.ls.sports.api.routers.users.UserDTO
import pt.isel.ls.sports.api.routers.users.UsersResponse
import pt.isel.ls.sports.api.utils.AppErrorDTO
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UsersIntegrationTests : IntegrationTests() {

    // Create new user

    @Test
    fun `Create new user with valid data`(): Unit = db.connection.use { conn ->
        val requestBody = """
            {
                "name": "John",
                "email": "JohnnyBoy@gmail.com"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/users")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CREATED, status)

                val uid = Json.decodeFromString<CreateUserResponse>(bodyString()).uid
                assertTrue(isValidId(uid))

                assertTrue(db.users.hasUser(conn, uid))
            }
    }

    @Test
    fun `Create new user with already existing email`(): Unit = db.connection.use { conn ->
        db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")

        val requestBody = """
            {
                "name": "John",
                "email": "JohnnyBoy@gmail.com"
            }
            
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/users")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CONFLICT, status)

                val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
                assertEquals(AppError.Conflict(), error)
            }
    }

    @Test
    fun `Create new user with invalid data`() {
        val requestBody = """
            {
                "name": "John",
                "email": "JohnnyBoy"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/users")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
                assertEquals(AppError.InvalidArgument(), error)
            }
    }

    // Get All users

    @Test
    fun `Get all users`(): Unit = db.connection.use { conn ->
        val mockUsers = listOf(
            CreateUserRequest("Johnny", "JohnnyBoy@gmail.com"),
            CreateUserRequest("Jesus", "JesusSenpai@gmail.com")
        ).associateBy {
            db.users.createNewUser(conn, it.name, it.email)
        }

        val request = Request(Method.GET, "$uriPrefix/users")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val users = Json.decodeFromString<UsersResponse>(bodyString()).users
                assertEquals(mockUsers.size, users.size)

                users.forEach { user ->
                    val mockUser = mockUsers[user.id]
                    assertNotNull(mockUser)

                    assertEquals(mockUser.name, user.name)
                    assertEquals(mockUser.email, user.email)
                }
            }
    }

    @Test
    fun `Get all users but with empty database`() {
        val request = Request(Method.GET, "$uriPrefix/users")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val users = Json.decodeFromString<UsersResponse>(bodyString()).users
                assertEquals(0, users.size)
            }
    }

    // Get user

    @Test
    fun `Get user by id`(): Unit = db.connection.use { conn ->
        val mockUser = CreateUserRequest("Johnny", "JohnnyBoy@gmail.com")
        val mockId = db.users.createNewUser(conn, mockUser.name, mockUser.email)

        val request = Request(Method.GET, "$uriPrefix/users/$mockId")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val user = Json.decodeFromString<UserDTO>(bodyString())
                assertEquals(mockId, user.id)
                assertEquals(mockUser.name, user.name)
                assertEquals(mockUser.email, user.email)
            }
    }

    @Test
    fun `Get user by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/users/$mockId")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
                assertEquals(AppError.InvalidArgument(), error)
            }
    }

    @Test
    fun `Get user by valid id but user does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/users/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
                assertEquals(AppError.NotFound(), error)
            }
    }

    // Get user activities

    @Test
    fun `Get user activities by valid id`(): Unit = db.connection.use { conn ->
        val mockUid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        val sid = db.sports.createNewSport(conn, mockUid, "Running", "Running")

        val mockActivities = listOf(
            CreateActivityRequest("2019-01-01", "23:59:59.555", sid),
            CreateActivityRequest("2019-01-02", "20:59:59.555", sid)
        ).associateBy {
            db.activities.createNewActivity(conn, mockUid, it.date.toLocalDate(), it.duration.toDuration(), it.sid)
        }

        val request = Request(Method.GET, "$uriPrefix/users/$mockUid/activities")

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
    fun `Get user activities by invalid id`() {
        val mockUid = -1

        val request = Request(Method.GET, "$uriPrefix/users/$mockUid/activities")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppErrorDTO>(bodyString()).toAppError()
                assertEquals(AppError.InvalidArgument(), error)
            }
    }
}
