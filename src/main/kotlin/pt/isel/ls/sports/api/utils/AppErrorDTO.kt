package pt.isel.ls.sports.api.utils

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.errors.AppException

/**
 * Represents an application error data transfer object.
 * @property code error code
 * @property name error name
 * @property description short description of the error
 * @property extraInfo other info related to the error
 */
@Serializable
class AppErrorDTO(val code: Int, val name: String, val description: String, val extraInfo: String? = null) {
    companion object {
        operator fun invoke(error: AppException): AppErrorDTO =
            AppErrorDTO(error.code, error.name, error.description, error.extraInfo)
    }

    /**
     * Converts this DTO to an [AppException].
     * @return [AppException]
     */
    fun toAppException() =
        AppException(code, name, description, extraInfo)
}
