package pt.isel.ls.sports.unit.database.sections.activities

interface ActivitiesDBTests {

    // createNewActivity

    fun `createNewActivity creates activity correctly in the database`()

    // getActivity

    fun `getActivity returns the activity object`()

    fun `getActivity throws SportsError (Not Found) if the activity with the sid doesn't exist`()

    // deleteActivity

    fun `deleteActivity deletes an activity successfully`()

    // getActivities

    fun `getActivities with descending order returns the activities list`()

    fun `getActivities with ascending order returns the activities list`()

    // getSportActivities

    fun `getSportActivities returns the activities list`()

    // getUserActivities

    fun `getUserActivities returns the activities list`()

    // searchActivities

    fun `searchActivities returns the activities list`()

    // searchUsersByActivity

    fun `searchUsersByActivity returns a list of users`()
}
