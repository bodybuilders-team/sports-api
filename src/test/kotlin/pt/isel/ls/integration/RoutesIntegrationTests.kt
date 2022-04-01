package pt.isel.ls.integration

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test
import pt.isel.ls.json
import pt.isel.ls.sports.api.routers.routes.CreateRouteRequest
import pt.isel.ls.sports.api.routers.routes.CreateRouteResponse
import pt.isel.ls.sports.api.routers.routes.RoutesResponse
import pt.isel.ls.sports.domain.Route
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.services.utils.isValidId
import pt.isel.ls.token
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RoutesIntegrationTests : IntegrationTests() {

    // Create new route

    @Test
    fun `Create new route with valid data`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)

        val requestBody = """
            {
                "start_location": "Porto",
                "end_location": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .token(token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.CREATED, status)

                val rid = Json.decodeFromString<CreateRouteResponse>(bodyString()).rid
                assertTrue(isValidId(rid))

                assertTrue(db.routes.hasRoute(rid))
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

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.NoCredentials(), error)
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

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidCredentials(), error)
            }
    }

    @Test
    fun `Create new route with invalid data`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val token = db.tokens.createUserToken(UUID.randomUUID(), uid)
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

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.BadRequest(), error)
            }
    }

    // Get all routes

    @Test
    fun `Get all routes`() {
        val uid = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")

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
            db.routes.createNewRoute(it.start_location, it.end_location, (it.distance * 1000).toInt(), uid)
        }

        val request = Request(Method.GET, "$uriPrefix/routes")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val routes = Json.decodeFromString<RoutesResponse>(bodyString()).routes
                assertEquals(mockRoutes.size, routes.size)

                routes.forEach { route ->
                    val mockRoute = mockRoutes[route.id]
                    assertNotNull(mockRoute)

                    assertEquals(mockRoute.start_location, route.start_location)
                    assertEquals(mockRoute.end_location, route.end_location)
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

                val routes = Json.decodeFromString<RoutesResponse>(bodyString()).routes
                assertEquals(0, routes.size)
            }
    }

    // Get route by id

    @Test
    fun `Get route by id`() {
        val mockRoute = CreateRouteRequest(
            "Russia",
            "Lisbon",
            10.0
        )

        val mockId = db.users.createNewUser("Johnny", "JohnnyBoy@gmail.com")
        val rid = db.routes.createNewRoute(
            mockRoute.start_location,
            mockRoute.end_location,
            (mockRoute.distance * 1000.0).toInt(),
            mockId
        )

        val request = Request(Method.GET, "$uriPrefix/routes/$rid")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val route = Json.decodeFromString<Route>(bodyString())
                assertEquals(rid, route.id)
                assertEquals(mockId, route.uid)

                assertEquals(mockRoute.start_location, route.start_location)
                assertEquals(mockRoute.end_location, route.end_location)
                assertEquals(mockRoute.distance, route.distance)
            }
    }

    @Test
    fun `Get route by invalid id`() {
        val mockId = -1
        val request = Request(Method.GET, "$uriPrefix/routes/$mockId")

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.InvalidArgument(), error)
            }
    }

    @Test
    fun `Get route by valid id but route does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/routes/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = Json.decodeFromString<AppError>(bodyString())
                assertEquals(AppError.NotFound(), error)
            }
    }
}
