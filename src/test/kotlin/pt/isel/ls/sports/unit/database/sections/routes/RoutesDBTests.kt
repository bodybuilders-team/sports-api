package pt.isel.ls.sports.unit.database.sections.routes

interface RoutesDBTests {

    // createNewRoute

    fun `createNewRoute creates route correctly in the database`()

    fun `createNewRoute returns correct identifier`()

    // updateRoute

    fun `updateRoute updates a route correctly`()

    fun `updateRoute returns true if a route was modified`()

    fun `updateRoute returns false if a route was not modified`()

    fun `updateRoute throws NotFoundException if there's no route with the rid`()

    fun `throws InvalidArgumentException if startLocation, endLocation and distance are both null`()

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
