package pt.isel.ls.sports.api.routers.activities.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.api.routers.users.dtos.UserDTO
import pt.isel.ls.sports.database.sections.activities.ActivitiesUser

/**
 * User data transfer object representation.
 *
 * @property user user
 * @property aid activity id
 */
@Serializable
data class ActivitiesUserDTO(
    val user: UserDTO,
    val aid: Int
) {
    companion object {

        /**
         * Converts a [ActivitiesUser] to a [ActivitiesUserDTO].
         *
         * @param activitiesUser user to be converted
         * @return [ActivitiesUserDTO] representation of the user
         */
        operator fun invoke(activitiesUser: ActivitiesUser): ActivitiesUserDTO =
            ActivitiesUserDTO(UserDTO(activitiesUser.user), activitiesUser.aid)
    }
}
