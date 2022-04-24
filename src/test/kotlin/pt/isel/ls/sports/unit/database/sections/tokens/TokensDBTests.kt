package pt.isel.ls.sports.unit.database.sections.tokens

interface TokensDBTests {

    // createUserToken

    fun `createUserToken creates token correctly in the database`()

    fun `Having multiple tokens for the same user is allowed`()

    // getUID

    fun `getUID returns the uid correctly`()

    fun `getUID throws SportsError (Not Found) if the token isn't associated to any user`()
}
