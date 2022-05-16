package pt.isel.ls.sports.unit.database.sections.sports

interface SportsDBTests {

    // createNewSport

    fun `createNewSport creates sport correctly in the database`()

    fun `createNewSport returns correct identifier`()

    // updateSport

    fun `updateSport updates a sport correctly`()

    fun `updateSport returns true if a sport was modified`()

    fun `updateSport returns false if a sport was not modified`()

    fun `updateSport throws NotFoundException if there's no sport with the sid`()

    fun `throws InvalidArgumentException if name and description are both null`()

    // getSport

    fun `getSport returns the sport object`()

    fun `getSport throws NotFoundException if the sport with the sid doesn't exist`()

    // searchSports

    fun `searchSports returns list of all sport objects`()

    fun `searchSports with no created sports returns empty list`()

    fun `searchSports with skip works`()

    fun `searchSports with limit works`()

    // hasSport

    fun `hasSport returns true if the sport exists`()

    fun `hasSport returns false if the sport does not exist`()
}
