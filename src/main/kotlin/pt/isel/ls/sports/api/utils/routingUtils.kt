package pt.isel.ls.sports.api.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.path
import pt.isel.ls.sports.errors.AppError
import pt.isel.ls.sports.utils.substringOrNull

/**
 * Changes the 'Content-Type' header to 'application/json' and adds [data] to the response body.
 *
 * @param data data to send in the response
 *
 * @return response
 */
inline fun <reified T> Response.json(data: T): Response =
    this
        .header("Content-Type", "application/json")
        .body(Json.encodeToString(data))

/**
 * Returns the value of the [param] in the request query.
 *
 * @param param query param to search
 *
 * @return value of the [param]
 * @throws AppError.BadRequest if the param doesn't exist
 */
fun Request.queryOrThrow(param: String): String =
    this.query(param) ?: throw AppError.BadRequest("Missing query parameter: $param")

/**
 * Returns the value of the [param] in the request path.
 *
 * @param param path param to search
 *
 * @return value of the [param]
 * @throws AppError.BadRequest if the param doesn't exist
 */
fun Request.pathOrThrow(param: String): String =
    this.path(param) ?: throw AppError.BadRequest("Missing path parameter: $param")

private const val TOKEN_START_INDEX = 7

/**
 * Returns the request token.
 *
 * @return token
 * @throws AppError.NoCredentials if the token doesn't exist
 */
fun Request.tokenOrThrow(): String =
    this.header("Authorization")?.substringOrNull(TOKEN_START_INDEX)
        ?: throw AppError.NoCredentials()
