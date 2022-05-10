package pt.isel.ls.sports.unit.database.sections.sports

interface SportsDBTests {

    // createNewSport

    fun `createNewSport creates sport correctly in the database`()

    fun `createNewSport returns correct identifier`()

    // getSport

    fun `getSport returns the sport object`()

    fun `getSport throws NotFoundException if the sport with the sid doesn't exist`()

    // getAllSports

    fun `getAllSports returns list of all sport objects`()

    fun `getAllSports with no created sports returns empty list`()

    fun `getAllSports with skip works`()

    fun `getAllSports with limit works`()

    // hasSport

    fun `hasSport returns true if the sport exists`()

    fun `hasSport returns false if the sport does not exist`()
}
