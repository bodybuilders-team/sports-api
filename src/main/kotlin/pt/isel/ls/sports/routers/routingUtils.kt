package pt.isel.ls.sports.routers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response

inline fun <reified T> Response.json(data: T): Response =
	this
		.header("Content-Type", "application/json")
		.body(Json.encodeToString(data))