package pt.isel.ls.sports.services

import pt.isel.ls.sports.database.AppDB
import pt.isel.ls.sports.services.sections.ActivitiesServices
import pt.isel.ls.sports.services.sections.RoutesServices
import pt.isel.ls.sports.services.sections.SportsServices
import pt.isel.ls.sports.services.sections.UsersServices

/**
 * Services, an aggregate of all services sections.
 */
class AppServices(db: AppDB) {
    val users: UsersServices = UsersServices(db)
    val sports: SportsServices = SportsServices(db)
    val routes: RoutesServices = RoutesServices(db)
    val activities: ActivitiesServices = ActivitiesServices(db)
}
