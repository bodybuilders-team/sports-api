package pt.isel.ls.sports.api.utils.errors

import kotlinx.serialization.Serializable
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.sports.api.utils.json

/**
 * Represents an error response of the api.
 *
 * @property name name of the error
 * @property description description of the error
 * @property extraInfo extra information about the error
 */
@Serializable
class AppError(val name: String, val description: String, val extraInfo: String? = null) {

    /**
     * Converts to a [Response], by serializing the [AppError] to json and putting it in the response's body.
     *
     * @param status the [Status] of the response
     * @return a [Response] which the body is the serialized [AppError]
     */
    fun toResponse(status: Status): Response =
        Response(status).json(this)
}
