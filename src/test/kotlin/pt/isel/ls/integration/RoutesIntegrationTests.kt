package pt.isel.ls.integration

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteRequest
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteResponse
import pt.isel.ls.sports.api.routers.routes.dtos.RouteDTO
import pt.isel.ls.sports.api.routers.routes.dtos.RoutesResponse
import pt.isel.ls.sports.api.utils.AppErrorDTO
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.token
import pt.isel.ls.sports.errors.AppException
import pt.isel.ls.sports.services.utils.isValidId
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RoutesIntegrationTests : IntegrationTests() {

    // Create new route

    @Test
    fun `Create new route with valid data`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            object {
                val token = token
            }
        }

        val requestBody = """
            {
                "start_location": "Porto",
                "end_location": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CREATED, status)

                val rid = this.decodeBodyAs<CreateRouteResponse>().rid
                assertTrue(isValidId(rid))

                db.execute { conn ->
                    assertTrue(db.routes.hasRoute(conn, rid))
                }
            }
    }

    @Test
    fun `Create new route with no token`() {
        val requestBody = """
            {
                "start_location": "Porto",
                "end_location": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
                assertEquals(AppException.NoCredentials(), error)
            }
    }

    @Test
    fun `Create new route with invalid token`() {
        val requestBody = """
            {
                "start_location": "Porto",
                "end_location": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .token("invalid token")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.UNAUTHORIZED, status)

                val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
                assertEquals(AppException.InvalidCredentials(), error)
            }
    }

    @Test
    fun `Create new route with invalid data`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            token
        }
        val requestBody = """
            {
                "start_location": "Porto",
                "end_location": "Lisbon"
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .token(token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
                assertEquals(AppException.BadRequest(), error)
            }
    }

// Get all routes

    @Test
    fun `Get all routes`() {
        val mockRoutes = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")

            val mockRoutes = listOf(
                CreateRouteRequest(
                    "Russia",
                    "Lisbon",
                    10.0
                ),
                CreateRouteRequest(
                    "Dubai",
                    "Paris",
                    10.0
                )
            ).associateBy {
                db.routes.createNewRoute(conn, it.start_location, it.end_location, (it.distance * 1000).toInt(), uid)
            }

            mockRoutes
        }

        val request = Request(Method.GET, "$uriPrefix/routes")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val routes = this.decodeBodyAs<RoutesResponse>().routes
                assertEquals(mockRoutes.size, routes.size)

                routes.forEach { route ->
                    val mockRoute = mockRoutes[route.id]
                    assertNotNull(mockRoute)

                    assertEquals(mockRoute.start_location, route.startLocation)
                    assertEquals(mockRoute.end_location, route.endLocation)
                    assertEquals(mockRoute.distance, route.distance)
                }
            }
    }

    @Test
    fun `Get all routes but with empty database`() {
        val request = Request(Method.GET, "$uriPrefix/routes")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val routes = this.decodeBodyAs<RoutesResponse>().routes
                assertEquals(0, routes.size)
            }
    }

// Get route by id

    @Test
    fun `Get route by id`() {
        val mockData = db.execute { conn ->
            val route = CreateRouteRequest(
                "Russia",
                "Lisbon",
                10.0
            )

            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com")
            val rid = db.routes.createNewRoute(
                conn,
                route.start_location,
                route.end_location,
                (route.distance * 1000.0).toInt(),
                uid
            )
            object {
                val uid = uid
                val rid = rid
                val route = route
            }
        }
        val request = Request(Method.GET, "$uriPrefix/routes/${mockData.rid}")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val route = this.decodeBodyAs<RouteDTO>()
                assertEquals(mockData.rid, route.id)
                assertEquals(mockData.uid, route.uid)

                assertEquals(mockData.route.start_location, route.startLocation)
                assertEquals(mockData.route.end_location, route.endLocation)
                assertEquals(mockData.route.distance, route.distance)
            }
    }

    @Test
    fun `Get route by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/routes/$mockId")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
                assertEquals(AppException.InvalidArgument(), error)
            }
    }

    @Test
    fun `Get route by valid id but route does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/routes/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = this.decodeBodyAs<AppErrorDTO>().toAppException()
                assertEquals(AppException.NotFound(), error)
            }
    }
}
