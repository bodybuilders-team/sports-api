package pt.isel.ls.sports.api.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.path
import pt.isel.ls.sports.services.AuthenticationException
import pt.isel.ls.sports.services.InvalidArgumentException
import pt.isel.ls.sports.utils.substringOrNull

/**
 * Changes the 'Content-Type' header to 'application/json' and adds [data] to the response body.
 *
 * @param data data to send in the response body
 * @return response with [data] as its JSON body
 */
inline fun <reified T> Response.json(data: T): Response =
    this
        .header("Content-Type", "application/json")
        .body(Json.encodeToString(data))

/**
 * Returns the value of the [param] in the request query.
 * If the [param] is not present in the query, an [InvalidArgumentException] is thrown.
 *
 * @param param query param to search for
 * @return value of the [param]
 * @throws InvalidArgumentException if the param is not present in the query
 */
fun Request.queryOrThrow(param: String): String =
    this.query(param) ?: throw InvalidArgumentException("Missing query parameter: $param")

/**
 * Returns the value of the [param] in the request path.
 * If the [param] is not present in the path, an [InvalidArgumentException] is thrown.
 *
 * @param param path param to search for
 * @return value of the [param]
 * @throws InvalidArgumentException if the param is not present in the path
 */
fun Request.pathOrThrow(param: String): String =
    this.path(param) ?: throw InvalidArgumentException("Missing path parameter: $param")

private const val TOKEN_START_INDEX = 7

/**
 * Returns the request token.
 * If the token is not present in the request, an [InvalidArgumentException] is thrown.
 *
 * @return token
 * @throws InvalidArgumentException if the token is not present in the request
 */
fun Request.tokenOrThrow(): String =
    this.header("Authorization")?.substringOrNull(TOKEN_START_INDEX)
        ?: throw AuthenticationException("Missing token in request")

/**
 * Decodes the request body as a [T] object.
 *
 * @return [T] object
 */
inline fun <reified T> Request.decodeBodyAs(): T =
    Json.decodeFromString(this.bodyString())

/**
 * Sets the 'Content-Type' header to 'application/json' and adds [requestBody] to the response body.
 *
 * @param requestBody data to send in the request body
 * @return request with [requestBody] as its JSON body
 */
fun Request.json(requestBody: String) =
    this.header("Content-Type", "application/json")
        .body(requestBody)

/**
 * Decodes the response body as a [T] object.
 *
 * @return [T] object
 */
inline fun <reified T> Response.decodeBodyAs(): T =
    Json.decodeFromString(this.bodyString())

/**
 * Sets request bearer token in authorization header.
 *
 * @param token bearer token
 * @return request with bearer token
 */
fun Request.token(token: String): Request =
    this.header("Authorization", "Bearer $token")

/**
 * Parses the string as an Int number and returns the result.
 *
 * @throws InvalidArgumentException if the string is not a valid representation of an integer.
 */
fun String.toIntOrThrow(errorInfo: (() -> String)? = null): Int =
    this.toIntOrNull()
        ?: throw InvalidArgumentException(errorInfo?.invoke())
