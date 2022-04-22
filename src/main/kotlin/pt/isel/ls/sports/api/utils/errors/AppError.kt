package pt.isel.ls.sports.api.utils.errors

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Response
import org.http4k.core.Status

@Serializable
class AppError(val name: String, val description: String, val extraInfo: String? = null) {

    fun toResponse(status: Status): Response =
        Response(status)
            .body(Json.encodeToString(this))
}
