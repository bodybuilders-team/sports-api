package pt.isel.ls.sports.unit.database.sections.routes

interface RoutesDBTests {

    // createNewRoute

    fun `createNewRoute creates route correctly in the database`()

    fun `createNewRoute returns correct identifier`()

    // getRoute

    fun `getRoute returns the route object`()

    fun `getRoute throws SportsError (Not Found) if the route with the rid doesn't exist`()

    // getAllRoutes

    fun `getAllRoutes returns list of all route objects`()

    fun `getAllRoutes with no created routes returns empty list`()
}
