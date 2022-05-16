package pt.isel.ls.sports.unit.database.sections.activities

interface ActivitiesDBTests {

    // createNewActivity

    fun `createNewActivity creates activity correctly in the database`()

    fun `createNewActivity returns correct identifier`()

    // updateActivity

    fun `updateActivity updates an activity correctly`()

    fun `updateActivity returns true if an activity was modified`()

    fun `updateActivity returns false if an activity was not modified`()

    fun `updateActivity throws NotFoundException if there's no activity with the aid`()

    fun `throws InvalidArgumentException if date, duration, sid and rid are both null`()

    // getActivity

    fun `getActivity returns the activity object`()

    fun `getActivity throws NotFoundException if there's no activity with the sid`()

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
