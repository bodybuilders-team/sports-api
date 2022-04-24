package pt.isel.ls.sports.integration.sections.users

import kotlinx.datetime.toLocalDate
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.sports.api.routers.activities.dtos.ActivitiesResponseDTO
import pt.isel.ls.sports.api.routers.activities.dtos.CreateActivityRequest
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserRequest
import pt.isel.ls.sports.api.routers.users.dtos.CreateUserResponseDTO
import pt.isel.ls.sports.api.routers.users.dtos.UserDTO
import pt.isel.ls.sports.api.routers.users.dtos.UsersResponseDTO
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.AppError
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.integration.IntegrationTests
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.sports.utils.toDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UsersIntegrationTests : IntegrationTests() {

    // Create new user

    @Test
    fun `Create new user with valid data`() {
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

                val uid = this.decodeBodyAs<CreateUserResponseDTO>().uid
                assertTrue(isValidId(uid))

                db.execute { conn ->
                    assertTrue(db.users.hasUser(conn, uid))
                }
            }
    }

    @Test
    fun `Create new user with already existing email`() {
        db.execute { conn ->
            db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
        }
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("ALREADY_EXISTS", error.name)
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    // Get All users

    @Test
    fun `Get all users`() {
        val mockUsers = db.execute { conn ->
            val users = listOf(
                CreateUserRequest("Johnny", "JohnnyBoy@gmail.com"),
                CreateUserRequest("Jesus", "JesusSenpai@gmail.com")
            ).associateBy {
                db.users.createNewUser(conn, it.name, it.email)
            }
            users
        }

        val request = Request(Method.GET, "$uriPrefix/users")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val users = this.decodeBodyAs<UsersResponseDTO>().users
                assertEquals(users.size, users.size)

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

                val users = this.decodeBodyAs<UsersResponseDTO>().users
                assertEquals(0, users.size)
            }
    }

    // Get user

    @Test
    fun `Get user by id`() {
        val mockData = db.execute { conn ->
            val user = CreateUserRequest("Johnny", "JohnnyBoy@gmail.com")
            val uid = db.users.createNewUser(conn, user.name, user.email)
            object {
                val uid = uid
                val user = user
            }
        }

        val request = Request(Method.GET, "$uriPrefix/users/${mockData.uid}")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val user = this.decodeBodyAs<UserDTO>()
                assertEquals(mockData.uid, user.id)
                assertEquals(mockData.user.name, user.name)
                assertEquals(mockData.user.email, user.email)
            }
    }

    @Test
    fun `Get user by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/users/$mockId")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    @Test
    fun `Get user by valid id but user does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/users/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("NOT_FOUND", error.name)
            }
    }

    // Get user activities

    @Test
    fun `Get user activities by valid id`() {
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
                val uid = uid
                val activities = activities
            }
        }

        val request = Request(Method.GET, "$uriPrefix/users/${mockData.uid}/activities")

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
    fun `Get user activities by invalid id`() {
        val mockUid = -1

        val request = Request(Method.GET, "$uriPrefix/users/$mockUid/activities")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }
}
