package pt.isel.ls.sports.routers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.path
import pt.isel.ls.sports.AppError
import pt.isel.ls.sports.substringOrNull

inline fun <reified T> Response.json(data: T): Response =
    this
        .header("Content-Type", "application/json")
        .body(Json.encodeToString(data))

fun Request.queryOrThrow(param: String): String =
    this.query(param) ?: throw AppError.badRequest("Missing query parameter: $param")

fun Request.pathOrThrow(param: String): String =
    this.path(param) ?: throw AppError.badRequest("Missing path parameter: $param")

fun Request.tokenOrThrow(): String =
    this.header("Authorization")?.substringOrNull(7)
        ?: throw AppError.noCredentials()

