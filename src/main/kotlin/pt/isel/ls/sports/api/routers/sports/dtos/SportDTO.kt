package pt.isel.ls.sports.api.routers.sports.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.sports.domain.Sport
import pt.isel.ls.sports.services.utils.isValidId

/**
 * Sport data transfer object representation.
 *
 * @property id sport's unique identifier
 * @property name name of the sport
 * @property description description of the sport (optional)
 * @property uid unique identifier of the user who created the sport
 */
@Serializable
data class SportDTO(
    val id: Int,
    val name: String,
    val uid: Int,
    val description: String?
) {
    companion object {
        operator fun invoke(sport: Sport): SportDTO {
            return SportDTO(sport.id, sport.name, sport.uid, sport.description)
        }
    }

    init {
        require(isValidId(id)) { "Invalid sport id: $id" }
        require(Sport.isValidName(name)) { "Invalid name: $name" }
        if (description != null) require(Sport.isValidDescription(description)) { "Invalid description: $description" }
        require(isValidId(uid)) { "Invalid user id: $uid" }
    }
}
