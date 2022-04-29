package pt.isel.ls.sports.unit.database.sections.users

interface UsersDBTests {

    // createNewUser

    fun `createNewUser creates user correctly in the database`()

    fun `createNewUser returns correct identifier`()

    fun `createNewUser throws AlreadyExistsException if a user with the email already exists`()

    // getUser

    fun `getUser returns the user object`()

    fun `getUser throws NotFoundException if the user with the uid doesn't exist`()

    // getAllUsers

    fun `getAllUsers returns list of user objects`()

    fun `getAllUsers with no created users returns empty list`()

    fun `getAllUsers with skip works`()

    fun `getAllUsers with limit works`()

    // hasUserWithEmail

    fun `hasUserWithEmail returns true if a user with the given email exists`()

    fun `hasUserWithEmail returns false if a user with the given email does not exist`()

    // hasUser

    fun `hasUser returns true if a user with the given uid exists`()

    fun `hasUser returns false if a user with the given uid does not exist`()
}
