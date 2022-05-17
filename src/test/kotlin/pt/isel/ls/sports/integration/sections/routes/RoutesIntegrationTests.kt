package pt.isel.ls.sports.integration.sections.routes

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteRequest
import pt.isel.ls.sports.api.routers.routes.dtos.CreateRouteResponse
import pt.isel.ls.sports.api.routers.routes.dtos.RouteDTO
import pt.isel.ls.sports.api.routers.routes.dtos.RoutesResponseDTO
import pt.isel.ls.sports.api.routers.routes.dtos.UpdateRouteResponse
import pt.isel.ls.sports.api.utils.decodeBodyAs
import pt.isel.ls.sports.api.utils.errors.AppError
import pt.isel.ls.sports.api.utils.json
import pt.isel.ls.sports.api.utils.token
import pt.isel.ls.sports.integration.IntegrationTests
import pt.isel.ls.sports.services.utils.isValidId
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RoutesIntegrationTests : IntegrationTests() {

    // Create new route

    @Test
    fun `Create new route with valid data`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            object {
                val token = token
            }
        }

        val requestBody = """
            {
                "startLocation": "Porto",
                "endLocation": "Lisbon",
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
                "startLocation": "Porto",
                "endLocation": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("MISSING_TOKEN", error.name)
            }
    }

    @Test
    fun `Create new route with invalid token`() {
        val requestBody = """
            {
                "startLocation": "Porto",
                "endLocation": "Lisbon",
                "distance": 10
            }
        """.trimIndent()

        val request = Request(Method.POST, "$uriPrefix/routes")
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
    fun `Create new route with invalid data`() {
        val token = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    // Update route

    @Test
    fun `Update route with valid data`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val rid = db.routes.createNewRoute(conn, "Lisbon", "Lisbon", 10.0, uid)

            object {
                val token = token
                val rid = rid
            }
        }

        val requestBody = """
            {
                "startLocation": "Porto",
                "endLocation": "Porto",
                "distance": 5.0
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/routes/${mockData.rid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val modified = this.decodeBodyAs<UpdateRouteResponse>().modified
                assertTrue(modified)
            }
    }

    @Test
    fun `Update route with valid data same as before`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)
            val rid = db.routes.createNewRoute(conn, "Lisbon", "Lisbon", 10.0, uid)

            object {
                val token = token
                val rid = rid
            }
        }

        val requestBody = """
            {
                "startLocation": "Lisbon",
                "endLocation": "Lisbon",
                "distance": 10.0
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/routes/${mockData.rid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val modified = this.decodeBodyAs<UpdateRouteResponse>().modified
                assertFalse(modified)
            }
    }

    @Test
    fun `Update route with invalid rid`() {
        val mockData = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
            val token = db.tokens.createUserToken(conn, UUID.randomUUID(), uid)

            object {
                val token = token
                val rid = -99
            }
        }

        val requestBody = """
            {
                "startLocation": "Porto",
                "endLocation": "Porto",
                "distance": 5.0
            }
        """.trimIndent()

        val request = Request(Method.PATCH, "$uriPrefix/routes/${mockData.rid}")
            .token(mockData.token)
            .json(requestBody)

        send(request)
            .apply {
                assertEquals(Status.BAD_REQUEST, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    // Get all routes

    @Test
    fun `Get all routes`() {
        val mockRoutes = db.execute { conn ->
            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")

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
                db.routes.createNewRoute(conn, it.startLocation, it.endLocation, it.distance, uid)
            }

            mockRoutes
        }

        val request = Request(Method.GET, "$uriPrefix/routes")

        send(request)
            .apply {
                assertEquals(Status.OK, status)

                val routes = this.decodeBodyAs<RoutesResponseDTO>().routes
                assertEquals(mockRoutes.size, routes.size)

                routes.forEach { route ->
                    val mockRoute = mockRoutes[route.id]
                    assertNotNull(mockRoute)

                    assertEquals(mockRoute.startLocation, route.startLocation)
                    assertEquals(mockRoute.endLocation, route.endLocation)
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

                val routes = this.decodeBodyAs<RoutesResponseDTO>().routes
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

            val uid = db.users.createNewUser(conn, "Johnny", "JohnnyBoy@gmail.com", "H42xS")
            val rid = db.routes.createNewRoute(
                conn,
                route.startLocation,
                route.endLocation,
                route.distance,
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

                assertEquals(mockData.route.startLocation, route.startLocation)
                assertEquals(mockData.route.endLocation, route.endLocation)
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

                val error = this.decodeBodyAs<AppError>()
                assertEquals("BAD_REQUEST", error.name)
            }
    }

    @Test
    fun `Get route by valid id but route does not exist`() {
        val mockId = 1
        val request = Request(Method.GET, "$uriPrefix/routes/$mockId")

        send(request)
            .apply {
                assertEquals(Status.NOT_FOUND, status)

                val error = this.decodeBodyAs<AppError>()
                assertEquals("NOT_FOUND", error.name)
            }
    }
}
