package pt.isel.ls.sports

/**
 * User representation
 *
 * @property id user's unique identifier
 * @property name name of the user
 * @property email email of the user
 */
data class User(
	val id: Int,
	val name: String,
	val email: String
)


/**
 * Sport representation
 *
 * @property id sport's unique identifier
 * @property name name of the sport
 * @property description description of the sport (optional)
 * @property uid unique identifier of the user who created the sport
 */
data class Sport(
	val id: Int,
	val name: String,
	val description: String = "",
	val uid: Int
)


/**
 * Route  representation
 *
 * @property id route unique identifier
 * @property start_location start location of the route
 * @property end_location end location of the route
 * @property distance distance between [start_location] and [end_location] in meters
 * @property uid unique identifier of the user who created the route
 */
data class Route(
	val id: Int,
	val start_location: String,
	val end_location: String,
	val distance: Int,
	val uid: Int
) {
	init {
		require(distance > 0) { "Distance in route must be positive" }
	}
}


/**
 * Activity representation
 *
 * @property id activity unique identifier
 * @property date activity date
 * @property duration duration of the activity
 * @property uid unique identifier of the user who created the activity
 * @property sid unique identifier of the activity sport
 * @property rid unique identifier of the activity route (optional)
 */
data class Activity(
	val id: Int,
	val date: String,
	val duration: String,
	val uid: Int,
	val sid: Int,
	val rid: Int?
)
