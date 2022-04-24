package pt.isel.ls.sports.unit.database.sections.users

interface UsersDBTests {

    // createNewUser

    fun `createNewUser creates user correctly in the database`()

    fun `createNewUser returns correct identifier`()

    // getUser

    fun `getUser returns the user object`()

    fun `getUser throws SportsError (Not Found) if the user with the uid doesn't exist`()

    // getAllUsers

    fun `getAllUsers returns list of user objects`()

    fun `getAllUsers with no created users returns empty list`()
}
