package pt.isel.ls.sports.unit.database.sections.sports

interface SportsDBTests {

    // createNewSport

    fun `createNewSport creates sport correctly in the database`()

    fun `createNewSport returns correct identifier`()

    // getSport

    fun `getSport returns the sport object`()

    fun `getSport throws SportsError (Not Found) if the sport with the sid doesn't exist`()

    // getAllSports

    fun `getAllSports returns list of all sport objects`()

    fun `getAllSports with no created sports returns empty list`()
}
