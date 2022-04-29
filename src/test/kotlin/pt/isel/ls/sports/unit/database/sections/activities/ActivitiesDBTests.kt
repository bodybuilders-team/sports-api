package pt.isel.ls.sports.unit.database.sections.activities

interface ActivitiesDBTests {

    // createNewActivity

    fun `createNewActivity creates activity correctly in the database`()

    // getActivity

    fun `getActivity returns the activity object`()

    fun `getActivity throws NotFoundException if the activity with the sid doesn't exist`()

    // deleteActivity

    fun `deleteActivity deletes an activity successfully`()

    fun `deleteActivity throws NotFoundException if there's no activity with the aid`()

    // getSportActivities

    fun `getSportActivities returns the activities list`()

    fun `getSportActivities with skip works`()

    fun `getSportActivities with limit works`()

    // getUserActivities

    fun `getUserActivities returns the activities list`()

    fun `getUserActivities with skip works`()

    fun `getUserActivities with limit works`()

    // searchActivities

    fun `searchActivities returns the activities list`()

    fun `searchActivities with descending order returns the activities list`()

    fun `searchActivities with ascending order returns the activities list`()

    fun `searchActivities with skip works`()

    fun `searchActivities with limit works`()

    // searchUsersByActivity

    fun `searchUsersByActivity returns a list of users`()

    fun `searchUsersByActivity with skip works`()

    fun `searchUsersByActivity with limit works`()

    // hasActivity

    fun `hasActivity returns true if the activity exists`()

    fun `hasActivity returns false if the activity does not exist`()
}
