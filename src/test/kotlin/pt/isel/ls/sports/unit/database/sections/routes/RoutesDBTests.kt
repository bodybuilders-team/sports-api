package pt.isel.ls.sports.unit.database.sections.routes

interface RoutesDBTests {

    // createNewRoute

    fun `createNewRoute creates route correctly in the database`()

    fun `createNewRoute returns correct identifier`()

    // getRoute

    fun `getRoute returns the route object`()

    fun `getRoute throws NotFoundException if there's no route with the rid`()

    // getAllRoutes

    fun `getAllRoutes returns list of all route objects`()

    fun `getAllRoutes with no created routes returns empty list`()

    fun `getAllRoutes with skip works`()

    fun `getAllRoutes with limit works`()

    // hasRoute

    fun `hasRoute returns true if the route exists`()

    fun `hasRoute returns false if the route does not exist`()
}
